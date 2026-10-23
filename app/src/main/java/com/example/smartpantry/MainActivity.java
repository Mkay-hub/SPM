package com.example.smartpantry;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartpantry.activity.AddEditPantryActivity;
import com.example.smartpantry.adapter.PantryAdapter;
import com.example.smartpantry.database.PantryRepo;
import com.example.smartpantry.model.PantryItem;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "SPM";

    private PantryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Inflates screen layout
        setContentView(R.layout.activity_main);

        // Recycler setup
        RecyclerView rvPantry = findViewById(R.id.rvPantry);
        rvPantry.setLayoutManager(new LinearLayoutManager(this));

        adapter = new PantryAdapter(new PantryAdapter.onPantryItemClickListener() {
            @Override
            public void onEditClick(PantryItem item) {
                Intent intent = new Intent(MainActivity.this, AddEditPantryActivity.class);
                intent.putExtra(AddEditPantryActivity.EXTRA_ITEM, item);
                startActivity(intent);
            }

            @Override
            public void onDeleteClick(PantryItem item) {
                showDeleteConfirmationDialog(item);
            }
        });

        rvPantry.setAdapter(adapter);

        loadPantryItems();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadPantryItems();
    }

    public void loadPantryItems() {
        // Fetches data from Firestore.
        new PantryRepo().getAll(new PantryRepo.ListCallback() {
            @Override
            public void onSuccess(List<PantryItem> items) {
                adapter.setPantryItems(items);
            }

            @Override
            public void onError(Exception e) {
                Log.e("PantryList", "Error fetching pantry items", e);
            }
        });
    }

    // Ensure user is signed in (anonymously) before attempting Firestore reads/writes
    private void ensureAuthenticatedAndInit() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            Log.d(TAG, "Already authenticated as: " + auth.getCurrentUser().getUid());
            performDatabaseOperations();
        } else {
            auth.signInAnonymously()
                    .addOnSuccessListener(authResult -> {
                        String uid = authResult.getUser() != null ? authResult.getUser().getUid() : "anonymous";
                        Log.d(TAG, "Anonymous auth successful: " + uid);
                        performDatabaseOperations();
                    })
                    .addOnFailureListener(e -> {
                        Log.w(TAG, "Anonymous auth failed or disabled in Firebase Console. Attempting database operation anyway.", e);
                        performDatabaseOperations();
                    });
        }
    }

    private void performDatabaseOperations() {
        new PantryRepo().add(new PantryItem("Tomatoes", 3, "pcs", ""),
                new PantryRepo.ActionCallBack() {
                    @Override
                    public void onSuccess() {
                        Log.d(TAG, "Saved to database");
                    }

                    @Override
                    public void onError(Exception e) {
                        Log.e(TAG, "Failed to save to database. Please check Firestore Security Rules in Firebase Console.", e);
                    }
                });
    }

    private void showDeleteConfirmationDialog(PantryItem item) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Ingredient")
                .setMessage("Are you sure you want to delete '" + item.getName() + "' from your pantry?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    // Delete item from Firestore
                    new PantryRepo().delete(item.getId(), new PantryRepo.ActionCallBack() {
                        @Override
                        public void onSuccess() {
                            Toast.makeText(MainActivity.this, item.getName() + " Deleted", Toast.LENGTH_SHORT).show();
                            loadPantryItems(); // Refresh RecyclerView
                        }

                        @Override
                        public void onError(Exception e) {
                            Toast.makeText(MainActivity.this, "Failed to delete item.", Toast.LENGTH_SHORT).show();
                        }
                    });
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}
