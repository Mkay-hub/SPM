package com.example.smartpantry.database;

import com.example.smartpantry.model.PantryItem;
import com.example.smartpantry.model.Recipe;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Collection;
import java.util.List;

// All access for recipe.
public class RecipeRepo {

    private static final String COLLECTION = "recipes";
    private final CollectionReference recipeCollection = FirebaseFirestore.getInstance().collection(COLLECTION);

    public interface ListCallback {
        void onSuccess(List<Recipe> recipes);

        void onError(Exception e);
    }

    public interface ActionCallback {
        void onSuccess();

        void onError(Exception e);
    }

    // Read: Fetch all seeded recipes
    public void getAll(ListCallback callback) {
        recipeCollection.orderBy("name").get()
                .addOnSuccessListener(snap -> callback.onSuccess(snap.toObjects(Recipe.class)))
                .addOnFailureListener(callback::onError);
    }

    // Bulk Seed Helper using Firebase WriteBatch:
    public void seedBatch(List<Recipe> recipes, ActionCallback callback) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        com.google.firebase.firestore.WriteBatch batch = db.batch();

        for (Recipe recipe : recipes) {
            String id = recipeCollection.document().getId();
            recipe.setId(id);
            batch.set(recipeCollection.document(id), recipe);
        }

        batch.commit()
                .addOnSuccessListener(v -> callback.onSuccess())
                .addOnFailureListener(callback::onError);
    }


}
