package com.example.smartpantry.util;

import com.example.smartpantry.model.*;

import java.util.ArrayList;
import java.util.List;

public class RecipeMatcher {
    /*
        Filters a list of recipes
        Every single ingredient required by the recipe must be present in user's pantry.
        In at least the required quantity.
     */
    public static List<Recipe> getMatchingRecipes(List<Recipe> allRecipes, List<PantryItem> pantryItems) {
        List<Recipe> matchingRecipes = new ArrayList<>();

        if (allRecipes == null || pantryItems == null || pantryItems.isEmpty()) {
            return matchingRecipes; // Returns empty list if pantry is empty.
        }

        for (Recipe recipe : allRecipes) {
            if (canMakeRecipe(recipe, pantryItems)) {
                matchingRecipes.add(recipe);
            }
        }
        return matchingRecipes;
    }

    /*
        Checks if all required ingredient of a recipe are available
        and in sufficient quantities.
     */
    private static boolean canMakeRecipe(Recipe recipe, List<PantryItem> pantryItems) {
        if (recipe.getIngredients() == null || recipe.getIngredients().isEmpty()) {
            return false;
        }
        for (RecipeIngredient required : recipe.getIngredients()) {
            boolean ingredientFound = false;

            for (PantryItem pantry : pantryItems) {
                if (isIngredientMatch(required.getName(), pantry.getName())) {
                    // Ingredient name match:
                    ingredientFound = true;
                    if (pantry.getQuantity() >= required.getQuantity()) {
                        break;  // Move to next required ingredient
                    }
                }
            }

            // Excludes recipe is required ingredient or quantity is insufficient.
            if (!ingredientFound) {
                return false;
            }
        }
        return true; // All required ingredients are meet!
    }
    /*
        Robust ingredient name matching:
        Handling data validation
     */

    public static boolean isIngredientMatch(String name1, String name2) {
        if (name1 == null || name2 == null) return false;

        String norm1 = normalize(name1);
        String norm2 = normalize(name2);

        return norm1.equals(norm2);
    }

    /*
        Normalizes ingredient names using various data validation rules
     */

    private static String normalize(String input) {
        String s = input.trim().toLowerCase();

        //Handle simple plural forms
        if (s.endsWith("es") && s.length() > 3) {
            s = s.substring(0, s.length() - 2);
        } else if (s.endsWith("s") && s.length() > 2) {
            s = s.substring(0, s.length() - 1);
        }
        return s;
    }
}








