package com.example.smartpantry.util;

import com.example.smartpantry.model.PantryItem;
import com.example.smartpantry.model.Recipe;
import com.example.smartpantry.model.RecipeIngredient;

import java.util.ArrayList;
import java.util.List;

public class RecipeMatcher {

    /**
     * Filters a list of recipes.
     * Every required ingredient must be present in user's pantry
     * in at least the required quantity.
     */
    public static List<Recipe> getMatchingRecipes(List<Recipe> allRecipes, List<PantryItem> pantryItems) {
        List<Recipe> matchingRecipes = new ArrayList<>();

        if (allRecipes == null || pantryItems == null || pantryItems.isEmpty()) {
            return matchingRecipes;
        }

        for (Recipe recipe : allRecipes) {
            if (canMakeRecipe(recipe, pantryItems)) {
                matchingRecipes.add(recipe);
            }
        }
        return matchingRecipes;
    }

    /**
     * Checks if all required ingredients of a recipe are available
     * in sufficient quantities.
     */
    private static boolean canMakeRecipe(Recipe recipe, List<PantryItem> pantryItems) {
        if (recipe.getIngredients() == null || recipe.getIngredients().isEmpty()) {
            return false;
        }

        for (RecipeIngredient required : recipe.getIngredients()) {
            boolean hasSufficient = false;

            for (PantryItem pantry : pantryItems) {
                if (isIngredientMatch(required.getName(), pantry.getName())) {
                    if (pantry.getQuantity() >= required.getQuantity()) {
                        hasSufficient = true;
                        break;
                    }
                }
            }

            if (!hasSufficient) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if two ingredient names match (normalized).
     */
    public static boolean isIngredientMatch(String name1, String name2) {
        if (name1 == null || name2 == null) return false;

        String norm1 = normalize(name1);
        String norm2 = normalize(name2);

        return norm1.equals(norm2);
    }

    /**
     * Normalizes ingredient names for fuzzy comparison.
     */
    private static String normalize(String input) {
        String s = input.trim().toLowerCase();

        // Handle simple plural forms
        if (s.endsWith("es") && s.length() > 3) {
            s = s.substring(0, s.length() - 2);
        } else if (s.endsWith("s") && s.length() > 2) {
            s = s.substring(0, s.length() - 1);
        }
        return s;
    }
}
