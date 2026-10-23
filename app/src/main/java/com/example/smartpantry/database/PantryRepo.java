package com.example.smartpantry.database;

import com.example.smartpantry.model.PantryItem;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.List;

/**
 * All database access for pantry items.
 * Screens call this class instead of touching Firestore directly.
 **/
public class PantryRepo {

    private static final String COLLECTION = "pantry";
    private final CollectionReference pantry = FirebaseFirestore.getInstance().collection(COLLECTION);

    // Async Firestore calls:
    public interface ListCallback {
        void onSuccess(List<PantryItem> items);
        void onError(Exception e);
    }

    public interface ActionCallBack {
        void onSuccess();
        void onError(Exception e);
    }

    // Create: Firestore generates the document ID and updates item.
    public void add(PantryItem item, ActionCallBack callback) {
        pantry.add(item)
                .addOnSuccessListener(ref -> {
                    if (item != null) {
                        item.setId(ref.getId());
                    }
                    callback.onSuccess();
                })
                .addOnFailureListener(callback::onError);
    }

    // Bulk Seed Helper using Firebase WriteBatch:
    public void seedBatch(List<PantryItem> items, ActionCallBack callback) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        com.google.firebase.firestore.WriteBatch batch = db.batch();

        for (PantryItem item : items) {
            String id = pantry.document().getId();
            item.setId(id);
            batch.set(pantry.document(id), item);
        }

        batch.commit()
                .addOnSuccessListener(v -> callback.onSuccess())
                .addOnFailureListener(callback::onError);
    }

    // Read: all items, sorted by name.
    public void getAll(ListCallback callback) {
        pantry.orderBy("name").get()
                .addOnSuccessListener(snap -> {
                    try {
                        List<PantryItem> items = snap.toObjects(PantryItem.class);
                        callback.onSuccess(items);
                    } catch (Exception e) {
                        callback.onError(e);
                    }
                })
                .addOnFailureListener(callback::onError);
    }

    // Update: overwrites the document that has this item's ID.
    public void update(PantryItem item, ActionCallBack callback) {
        if (item == null || item.getId() == null || item.getId().trim().isEmpty()) {
            callback.onError(new IllegalArgumentException("Item document ID is missing for update."));
            return;
        }
        pantry.document(item.getId()).set(item)
                .addOnSuccessListener(v -> callback.onSuccess())
                .addOnFailureListener(callback::onError);
    }

    // Delete: removes the document with this ID.
    public void delete(String id, ActionCallBack callBack) {
        if (id == null || id.trim().isEmpty()) {
            callBack.onError(new IllegalArgumentException("Item document ID is missing for delete."));
            return;
        }
        pantry.document(id).delete()
                .addOnSuccessListener(v -> callBack.onSuccess())
                .addOnFailureListener(callBack::onError);
    }
}
