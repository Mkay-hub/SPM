package com.example.smartpantry.activity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.smartpantry.R;
import com.example.smartpantry.model.Recipe;
import com.example.smartpantry.model.RecipeIngredient;

public class RecipeDetailActivity extends AppCompatActivity {

    public static final String EXTRA_RECIPE = "extra_recipe_object";

    private TextView tvTitle, tvIngredients, tvInstructions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_detail);

        tvTitle = findViewById(R.id.tvRecipeDetailTitle);
        tvIngredients = findViewById(R.id.tvIngredientList);
        tvInstructions = findViewById(R.id.tvInstructions);

        Recipe recipe = null;
        if (getIntent() != null && getIntent().hasExtra(EXTRA_RECIPE)) {
            try {
                recipe = (Recipe) getIntent().getSerializableExtra(EXTRA_RECIPE);
            } catch (Exception e) {
                // handle exception
            }
        }

        if (recipe != null) {
            if (tvTitle != null && recipe.getName() != null) {
                tvTitle.setText(recipe.getName());
            }

            StringBuilder ingBuilder = new StringBuilder();
            if (recipe.getIngredients() != null) {
                for (RecipeIngredient ing : recipe.getIngredients()) {
                    if (ing != null) {
                        ingBuilder.append("• ")
                                .append(ing.getName() != null ? ing.getName() : "")
                                .append(" - ")
                                .append(ing.getQuantity())
                                .append(" ")
                                .append(ing.getUnit() != null ? ing.getUnit() : "")
                                .append("\n");
                    }
                }
            }
            if (tvIngredients != null) {
                tvIngredients.setText(ingBuilder.toString().trim());
            }
            if (tvInstructions != null && recipe.getSteps() != null) {
                tvInstructions.setText(recipe.getSteps());
            }
        } else {
            Toast.makeText(this, "Error loading recipe details", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}













