package com.example.smartpantry.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartpantry.MainActivity;
import com.example.smartpantry.R;
import com.example.smartpantry.adapter.RecipeAdapter;
import com.example.smartpantry.database.PantryRepo;
import com.example.smartpantry.database.RecipeRepo;
import com.example.smartpantry.model.PantryItem;
import com.example.smartpantry.model.Recipe;
import com.example.smartpantry.util.RecipeMatcher;

import java.util.List;

public class SuggestedRecipesActivity extends AppCompatActivity {
    private RecyclerView rvRecipes;
    private TextView tvEmptyState;
    private RecipeAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.suggested_recipes);

        rvRecipes = findViewById(R.id.rvSuggestedRecipes);
        tvEmptyState = findViewById(R.id.tvEmptyState);

        rvRecipes.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecipeAdapter(recipe -> {
            Intent intent = new Intent(SuggestedRecipesActivity.this, RecipeDetailActivity.class);
            intent.putExtra(RecipeDetailActivity.EXTRA_RECIPE, recipe);
            startActivity(intent);
        });
        rvRecipes.setAdapter(adapter);

        initNavigation();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadMatchingRecipes();
    }

    private void loadMatchingRecipes() {
        new PantryRepo().getAll(new PantryRepo.ListCallback() {
            @Override
            public void onSuccess(List<PantryItem> pantryItems) {
                new RecipeRepo().getAll(new RecipeRepo.ListCallback() {
                    @Override
                    public void onSuccess(List<Recipe> allRecipes) {
                        List<Recipe> matches = RecipeMatcher.getMatchingRecipes(allRecipes, pantryItems);

                        if (matches.isEmpty()) {
                            rvRecipes.setVisibility(View.GONE);
                            tvEmptyState.setVisibility(View.VISIBLE);
                        } else {
                            tvEmptyState.setVisibility(View.GONE);
                            rvRecipes.setVisibility(View.VISIBLE);
                            adapter.setRecipes(matches);
                        }
                    }

                    @Override
                    public void onError(Exception e) {
                        Toast.makeText(SuggestedRecipesActivity.this, "Failed to load recipes", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onError(Exception e) {
                Toast.makeText(SuggestedRecipesActivity.this, "Failed to load pantry", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initNavigation() {
        Button btnPantry = findViewById(R.id.btnNavPantry);
        Button btnRecipe = findViewById(R.id.btnNavRecipes);
        Button btnSettings = findViewById(R.id.btnNavBarSettings);

        if (btnRecipe != null) {
            btnRecipe.setEnabled(false); // Active tab
        }

        if (btnPantry != null) {
            btnPantry.setOnClickListener(v -> {
                Intent intent = new Intent(SuggestedRecipesActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            });
        }

        if (btnSettings != null) {
            btnSettings.setOnClickListener(v -> {
                Intent intent = new Intent(SuggestedRecipesActivity.this, SettingsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            });
        }
    }
}
