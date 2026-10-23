package com.example.smartpantry.database;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.smartpantry.model.Recipe;
import com.example.smartpantry.model.RecipeIngredient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Tracks whether the app has already seeded recipes.
 * If "is_seeded" is false, it sends the recipes to Firestore
 * in a single batch operation. Also cleans up any duplicate recipes.
 */
public class RecipeSeeder {

    private static final String TAG = "RecipeSeeder";
    private static final String PREFS_NAME = "SmartPantryPrefs";
    private static final String KEY_IS_SEEDED = "is_recipes_seeded";

    public static void seedRecipesIfNecessary(Context context) {
        try {
            RecipeRepo repo = new RecipeRepo();
            // Clean up any duplicate recipes in Firestore
            repo.cleanupDuplicates(new RecipeRepo.ActionCallback() {
                @Override
                public void onSuccess() {
                    SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
                    boolean isSeeded = prefs.getBoolean(KEY_IS_SEEDED, false);

                    if (isSeeded) {
                        Log.d(TAG, "Recipes already seeded. Skipping batch seed.");
                        return;
                    }

                    List<Recipe> recipes = getSeedData();

                    repo.seedBatch(recipes, new RecipeRepo.ActionCallback() {
                        @Override
                        public void onSuccess() {
                            Log.d(TAG, "Successfully seeded 15 recipes to Cloud Firestore!");
                            prefs.edit().putBoolean(KEY_IS_SEEDED, true).apply();
                        }

                        @Override
                        public void onError(Exception e) {
                            Log.e(TAG, "Failed to seed recipes to Firestore", e);
                        }
                    });
                }

                @Override
                public void onError(Exception e) {
                    Log.e(TAG, "Failed to cleanup duplicate recipes", e);
                }
            });
        } catch (Exception e) {
            Log.e(TAG, "Error in seedRecipesIfNecessary", e);
        }
    }

    public static List<Recipe> getSeedData() {
        List<Recipe> list = new ArrayList<>();

        // 1. Tomato Pasta
        list.add(new Recipe("Tomato Pasta", Arrays.asList(
                new RecipeIngredient("Tomatoes", 3, "pcs"),
                new RecipeIngredient("Pasta", 200, "g"),
                new RecipeIngredient("Garlic", 2, "cloves"),
                new RecipeIngredient("Olive Oil", 2, "tbsp")
        ), "Boil pasta. Saute minced garlic and chopped tomatoes in olive oil. Mix with pasta and serve warm."));

        // 2. Classic Omelet
        list.add(new Recipe("Classic Omelet", Arrays.asList(
                new RecipeIngredient("Eggs", 3, "pcs"),
                new RecipeIngredient("Butter", 1, "tbsp"),
                new RecipeIngredient("Salt", 1, "tsp")
        ), "Whisk eggs with salt. Melt butter in a non-stick pan. Pour in eggs, cook until firm, fold and serve."));

        // 3. Grilled Cheese Sandwich
        list.add(new Recipe("Grilled Cheese Sandwich", Arrays.asList(
                new RecipeIngredient("Bread", 2, "slices"),
                new RecipeIngredient("Cheese", 2, "slices"),
                new RecipeIngredient("Butter", 1, "tbsp")
        ), "Butter outer bread sides. Place cheese between slices. Grill on skillet until golden brown and cheese melts."));

        // 4. Garlic Fried Rice
        list.add(new Recipe("Garlic Fried Rice", Arrays.asList(
                new RecipeIngredient("Rice", 2, "cups"),
                new RecipeIngredient("Garlic", 4, "cloves"),
                new RecipeIngredient("Cooking Oil", 2, "tbsp"),
                new RecipeIngredient("Salt", 1, "tsp")
        ), "Saute minced garlic in oil until golden. Add cooked cold rice and salt. Stir fry on high heat for 5 minutes."));

        // 5. Basic Pancake
        list.add(new Recipe("Basic Pancake", Arrays.asList(
                new RecipeIngredient("Flour", 1, "cup"),
                new RecipeIngredient("Milk", 1, "cup"),
                new RecipeIngredient("Eggs", 1, "pcs"),
                new RecipeIngredient("Sugar", 2, "tbsp"),
                new RecipeIngredient("Baking Powder", 2, "tsp")
        ), "Whisk all ingredients to a smooth batter. Pour onto hot oiled skillet, flip when bubbles form, cook until golden."));

        // 6. Simple Tomato Soup
        list.add(new Recipe("Simple Tomato Soup", Arrays.asList(
                new RecipeIngredient("Tomatoes", 4, "pcs"),
                new RecipeIngredient("Onions", 1, "pcs"),
                new RecipeIngredient("Garlic", 2, "cloves"),
                new RecipeIngredient("Butter", 2, "tbsp"),
                new RecipeIngredient("Water", 2, "cups")
        ), "Saute onions and garlic in butter. Add chopped tomatoes and water. Simmer 20 min and blend until smooth."));

        // 7. French Toast
        list.add(new Recipe("French Toast", Arrays.asList(
                new RecipeIngredient("Bread", 4, "slices"),
                new RecipeIngredient("Eggs", 2, "pcs"),
                new RecipeIngredient("Milk", 0.5, "cup"),
                new RecipeIngredient("Butter", 1, "tbsp")
        ), "Whisk eggs and milk. Dip bread into egg mixture on both sides. Fry in melted butter until browned."));

        // 8. Rice and Beans
        list.add(new Recipe("Rice and Beans", Arrays.asList(
                new RecipeIngredient("Rice", 2, "cups"),
                new RecipeIngredient("Beans", 1, "can"),
                new RecipeIngredient("Onions", 1, "pcs"),
                new RecipeIngredient("Garlic", 2, "cloves"),
                new RecipeIngredient("Olive Oil", 1, "tbsp")
        ), "Saute onions and garlic in olive oil. Add canned beans, rice, and water. Cover and simmer until tender."));

        // 9. Cheesy Scrambled Eggs
        list.add(new Recipe("Cheesy Scrambled Eggs", Arrays.asList(
                new RecipeIngredient("Eggs", 3, "pcs"),
                new RecipeIngredient("Cheese", 0.5, "cup"),
                new RecipeIngredient("Butter", 1, "tbsp"),
                new RecipeIngredient("Milk", 2, "tbsp")
        ), "Whisk eggs with milk. Melt butter in a skillet over medium-low heat. Pour in eggs, gently fold, and top with cheese until melted."));

        // 10. Crispy Potato Hash
        list.add(new Recipe("Crispy Potato Hash", Arrays.asList(
                new RecipeIngredient("Potatoes", 3, "pcs"),
                new RecipeIngredient("Onions", 1, "pcs"),
                new RecipeIngredient("Cooking Oil", 2, "tbsp"),
                new RecipeIngredient("Salt", 1, "tsp")
        ), "Dice potatoes and onions. Fry in hot oil until potatoes are crispy and cooked through. Season with salt."));

        // 11. Avocado Toast
        list.add(new Recipe("Avocado Toast", Arrays.asList(
                new RecipeIngredient("Bread", 2, "slices"),
                new RecipeIngredient("Avocados", 1, "pcs"),
                new RecipeIngredient("Salt", 0.5, "tsp")
        ), "Toast bread slices. Mash avocado with salt. Spread evenly onto warm toast and serve immediately."));

        // 12. Cucumber Salad
        list.add(new Recipe("Cucumber Salad", Arrays.asList(
                new RecipeIngredient("Cucumber", 2, "pcs"),
                new RecipeIngredient("Olive Oil", 2, "tbsp"),
                new RecipeIngredient("Salt", 1, "tsp")
        ), "Thinly slice cucumber. Toss with olive oil and salt. Serve chilled."));

        // 13. Garlic Butter Pasta
        list.add(new Recipe("Garlic Butter Pasta", Arrays.asList(
                new RecipeIngredient("Pasta", 250, "g"),
                new RecipeIngredient("Butter", 3, "tbsp"),
                new RecipeIngredient("Garlic", 4, "cloves"),
                new RecipeIngredient("Salt", 1, "tsp")
        ), "Boil pasta. Melt butter in pan, fry minced garlic until fragrant. Toss cooked pasta in garlic butter with salt."));

        // 14. Classic Mashed Potatoes
        list.add(new Recipe("Classic Mashed Potatoes", Arrays.asList(
                new RecipeIngredient("Potatoes", 4, "pcs"),
                new RecipeIngredient("Butter", 2, "tbsp"),
                new RecipeIngredient("Milk", 0.5, "cup"),
                new RecipeIngredient("Salt", 1, "tsp")
        ), "Boil peeled potatoes until tender. Drain and mash with butter, warm milk, and salt until smooth and creamy."));

        // 15. Sautéed Vegetables
        list.add(new Recipe("Sautéed Vegetables", Arrays.asList(
                new RecipeIngredient("Onions", 1, "pcs"),
                new RecipeIngredient("Tomatoes", 2, "pcs"),
                new RecipeIngredient("Cucumber", 1, "pcs"),
                new RecipeIngredient("Olive Oil", 2, "tbsp"),
                new RecipeIngredient("Salt", 1, "tsp")
        ), "Chop vegetables into bite-sized pieces. Sauté in olive oil over medium-high heat until tender. Season with salt."));

        return list;
    }
}
