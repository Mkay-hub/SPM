package com.example.smartpantry.model;

import com.google.firebase.firestore.DocumentId;
import java.util.ArrayList;
import java.util.List;

/**
 * A recipe with its ingredient lines and method.
 * Stored in the "recipes" collection.
 **/

public class Recipe {

    private String id;
    private String name;
    private String steps;
    private List<RecipeIngredient> ingredients = new ArrayList<>();

    public Recipe(){}

    public Recipe(String name, String steps, List<RecipeIngredient> ingredients){
        this.name = name;
        this.steps = steps;
        this.ingredients = ingredients;
    }

    @DocumentId
    public String getId(){return id;}
    public void setId(String id){this.id = id;}

    public String getName(){return name;}
    public void setName(String name){this.name = name;}

    public String getSteps(){return steps;}
    public void setSteps(String steps){this.steps = steps;}

    public List<RecipeIngredient> getIngredients() { return ingredients; }
}
