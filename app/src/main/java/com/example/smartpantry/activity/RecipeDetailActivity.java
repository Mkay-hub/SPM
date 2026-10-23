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

        Recipe recipe = (Recipe) getIntent().getSerializableExtra(EXTRA_RECIPE);

        if (recipe != null) {
            tvTitle.setText(recipe.getName());

            StringBuilder ingBuilder = new StringBuilder();
            if (recipe.getIngredients() != null) {
                for (RecipeIngredient ing : recipe.getIngredients()) {
                    ingBuilder.append("• ")
                            .append(ing.getName())
                            .append(" - ")
                            .append(ing.getQuantity())
                            .append(" ")
                            .append(ing.getUnit())
                            .append("\n");
                }
            }
            tvIngredients.setText(ingBuilder.toString().trim());
            tvInstructions.setText(recipe.getSteps());
        } else {
            Toast.makeText(this, "Error loading recipe details", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}













