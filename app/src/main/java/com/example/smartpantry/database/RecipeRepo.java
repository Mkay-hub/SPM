package com.example.smartpantry.database;

import com.example.smartpantry.model.Recipe;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
                .addOnSuccessListener(snap -> {
                    try {
                        List<Recipe> recipes = snap.toObjects(Recipe.class);
                        callback.onSuccess(recipes);
                    } catch (Exception e) {
                        callback.onError(e);
                    }
                })
                .addOnFailureListener(callback::onError);
    }

    // Bulk Seed Helper using Firebase WriteBatch with deterministic IDs (prevents duplicates):
    public void seedBatch(List<Recipe> recipes, ActionCallback callback) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        WriteBatch batch = db.batch();

        for (Recipe recipe : recipes) {
            String id = recipe.getName() != null 
                    ? recipe.getName().toLowerCase().replaceAll("[^a-z0-9]", "_") 
                    : recipeCollection.document().getId();
            recipe.setId(id);
            batch.set(recipeCollection.document(id), recipe);
        }

        batch.commit()
                .addOnSuccessListener(v -> callback.onSuccess())
                .addOnFailureListener(callback::onError);
    }

    // Cleanup existing duplicate recipes in Firestore
    public void cleanupDuplicates(ActionCallback callback) {
        recipeCollection.get().addOnSuccessListener(snap -> {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            WriteBatch batch = db.batch();
            Map<String, String> seenNames = new HashMap<>();
            boolean hasDeletions = false;

            for (DocumentSnapshot doc : snap.getDocuments()) {
                Recipe r = doc.toObject(Recipe.class);
                if (r != null && r.getName() != null) {
                    String normName = r.getName().trim().toLowerCase();
                    if (seenNames.containsKey(normName)) {
                        // Duplicate found, delete it
                        batch.delete(doc.getReference());
                        hasDeletions = true;
                    } else {
                        seenNames.put(normName, doc.getId());
                    }
                }
            }

            if (hasDeletions) {
                batch.commit()
                        .addOnSuccessListener(v -> callback.onSuccess())
                        .addOnFailureListener(callback::onError);
            } else {
                callback.onSuccess();
            }
        }).addOnFailureListener(callback::onError);
    }
}
