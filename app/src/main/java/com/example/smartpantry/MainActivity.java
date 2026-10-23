package com.example.smartpantry;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartpantry.activity.AddEditPantryActivity;
import com.example.smartpantry.activity.SettingsActivity;
import com.example.smartpantry.activity.SuggestedRecipesActivity;
import com.example.smartpantry.adapter.PantryAdapter;
import com.example.smartpantry.database.PantryRepo;
import com.example.smartpantry.database.PantrySeeder;
import com.example.smartpantry.database.RecipeSeeder;
import com.example.smartpantry.model.PantryItem;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private PantryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Seed default recipes and pantry items into Cloud Firestore if not already done
        RecipeSeeder.seedRecipesIfNecessary(this);
        PantrySeeder.seedPantryIfNecessary(this);

        // Recycler setup
        RecyclerView rvPantry = findViewById(R.id.rvPantry);
        rvPantry.setLayoutManager(new LinearLayoutManager(this));

        adapter = new PantryAdapter(new PantryAdapter.onPantryItemClickListener() {
            @Override
            public void onEditClick(PantryItem item) {
                Intent intent = new Intent(MainActivity.this, AddEditPantryActivity.class);
                intent.putExtra(AddEditPantryActivity.EXTRA_ITEM, item);
                startActivity(intent);
            }

            @Override
            public void onDeleteClick(PantryItem item) {
                showDeleteConfirmationDialog(item);
            }
        });

        rvPantry.setAdapter(adapter);

        // FAB to add new ingredient
        FloatingActionButton fabAdd = findViewById(R.id.fabAdd);
        if (fabAdd != null) {
            fabAdd.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, AddEditPantryActivity.class);
                startActivity(intent);
            });
        }

        // Initialize Bottom Navigation Bar
        initNavigationBar();

        loadPantryItems();
    }

    /*
        Initializes click listeners for the bottom navigation bar.
     */
    private void initNavigationBar() {
        Button btnPantry = findViewById(R.id.btnNavPantry);
        Button btnRecipes = findViewById(R.id.btnNavRecipes);
        Button btnSettings = findViewById(R.id.btnNavBarSettings);

        // Disable current screen's button to signal active state
        if (btnPantry != null) {
            btnPantry.setEnabled(false);
        }

        if (btnRecipes != null) {
            btnRecipes.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, SuggestedRecipesActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            });
        }

        if (btnSettings != null) {
            btnSettings.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadPantryItems();
    }

    public void loadPantryItems() {
        // Fetches data from Firestore.
        new PantryRepo().getAll(new PantryRepo.ListCallback() {
            @Override
            public void onSuccess(List<PantryItem> items) {
                adapter.setPantryItems(items);
            }

            @Override
            public void onError(Exception e) {
                Log.e("PantryList", "Error fetching pantry items", e);
            }
        });
    }

    private void showDeleteConfirmationDialog(PantryItem item) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Ingredient")
                .setMessage("Are you sure you want to delete '" + item.getName() + "' from your pantry?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    // Delete item from Firestore
                    new PantryRepo().delete(item.getId(), new PantryRepo.ActionCallBack() {
                        @Override
                        public void onSuccess() {
                            Toast.makeText(MainActivity.this, item.getName() + " Deleted", Toast.LENGTH_SHORT).show();
                            loadPantryItems(); // Refresh RecyclerView
                        }

                        @Override
                        public void onError(Exception e) {
                            Toast.makeText(MainActivity.this, "Failed to delete item.", Toast.LENGTH_SHORT).show();
                        }
                    });
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}
