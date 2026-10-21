package com.example.smartpantry.database;

import com.example.smartpantry.model.PantryItem;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.List;

/**
 All database access for pantry items.
    Screen call this class instead of touching Firestore directly.
 **/

public class PantryRepo {

    private static final String COLLECTION = "SPM-System";
    private final CollectionReference pantry = FirebaseFirestore.getInstance().collection(COLLECTION);

    // Async Firestore calls:
    public interface ListCallback{
        void onSuccess(List<PantryItem> items);
        void onError(Exception e);
    }

    public interface ActionCallBack{
        void onSuccess();
        void onError(Exception e);
    }

    // Create: Firestore generates the document Id.
    public void add(PantryItem item, ActionCallBack callback){
        pantry.add(item)
                .addOnSuccessListener(ref -> callback.onSuccess())
                .addOnFailureListener(callback::onError);
    }

    // Read: all items, sorted by name.
    public void getAll(ListCallback callback){
        pantry.orderBy("name").get()
                .addOnSuccessListener(snap -> callback.onSuccess(snap.toObjects(PantryItem.class)))
                .addOnFailureListener(callback::onError);
    }

    // Update: overwrites the document that has this item's Id.
    public void update(PantryItem item, ActionCallBack callback){
        pantry.document(item.getId()).set(item)
                .addOnSuccessListener(v->callback.onSuccess())
                .addOnFailureListener(callback::onError);
    }

    // Delete: removes the document with this Id.
    public void delete(String id, ActionCallBack callBack){
        pantry.document(id).delete()
                .addOnSuccessListener(v->callBack.onSuccess())
                .addOnFailureListener(callBack::onError);
    }
}
