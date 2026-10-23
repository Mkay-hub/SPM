package com.example.smartpantry.model;

import com.google.firebase.firestore.DocumentId;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * A recipe with its ingredient lines and method.
 * Stored in the "recipes" collection.
 **/
public class Recipe implements Serializable {

    private String id;
    private String name;
    private List<RecipeIngredient> ingredients = new ArrayList<>();
    private String steps;

    public Recipe() {}

    public Recipe(String name, List<RecipeIngredient> ingredients, String steps) {
        this.name = name;
        this.ingredients = ingredients;
        this.steps = steps;
    }

    @DocumentId
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSteps() { return steps; }
    public void setSteps(String steps) { this.steps = steps; }

    public List<RecipeIngredient> getIngredients() { return ingredients; }
    public void setIngredients(List<RecipeIngredient> ingredients) { this.ingredients = ingredients; }
}
