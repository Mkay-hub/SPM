package com.example.smartpantry.database;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.smartpantry.model.PantryItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Tracks whether the app has already seeded initial pantry items.
 * If "is_pantry_seeded" is false, it sends sample pantry items to Firestore
 * in a single batch operation so that recipes can match and display immediately.
 */
public class PantrySeeder {

    private static final String TAG = "PantrySeeder";
    private static final String PREFS_NAME = "SmartPantryPrefs";
    private static final String KEY_IS_PANTRY_SEEDED = "is_pantry_seeded";

    public static void seedPantryIfNecessary(Context context) {
        try {
            SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
            boolean isSeeded = prefs.getBoolean(KEY_IS_PANTRY_SEEDED, false);

            if (isSeeded) {
                Log.d(TAG, "Pantry already seeded. Skipping.");
                return;
            }

            List<PantryItem> initialItems = getSeedPantryItems();

            new PantryRepo().seedBatch(initialItems, new PantryRepo.ActionCallBack() {
                @Override
                public void onSuccess() {
                    Log.d(TAG, "Successfully seeded initial pantry items to Cloud Firestore!");
                    prefs.edit().putBoolean(KEY_IS_PANTRY_SEEDED, true).apply();
                }

                @Override
                public void onError(Exception e) {
                    Log.e(TAG, "Failed to seed pantry items to Firestore", e);
                }
            });
        } catch (Exception e) {
            Log.e(TAG, "Error in seedPantryIfNecessary", e);
        }
    }

    public static List<PantryItem> getSeedPantryItems() {
        List<PantryItem> list = new ArrayList<>();
        list.add(new PantryItem("Tomatoes", 5.0, "pcs", "2026-11-05"));
        list.add(new PantryItem("Pasta", 500.0, "g", "2027-01-01"));
        list.add(new PantryItem("Garlic", 10.0, "cloves", "2026-11-15"));
        list.add(new PantryItem("Olive Oil", 500.0, "ml", "2027-06-01"));
        list.add(new PantryItem("Eggs", 12.0, "pcs", "2026-10-30"));
        list.add(new PantryItem("Butter", 200.0, "g", "2026-11-10"));
        list.add(new PantryItem("Salt", 1000.0, "g", "2028-01-01"));
        list.add(new PantryItem("Bread", 1.0, "loaf", "2026-10-26"));
        list.add(new PantryItem("Cheese", 300.0, "g", "2026-11-02"));
        list.add(new PantryItem("Rice", 2000.0, "g", "2027-05-01"));
        list.add(new PantryItem("Cooking Oil", 1000.0, "ml", "2027-06-01"));
        list.add(new PantryItem("Flour", 1000.0, "g", "2027-03-01"));
        list.add(new PantryItem("Milk", 1000.0, "ml", "2026-10-28"));
        list.add(new PantryItem("Sugar", 1000.0, "g", "2028-01-01"));
        list.add(new PantryItem("Baking Powder", 100.0, "g", "2027-08-01"));
        list.add(new PantryItem("Onions", 6.0, "pcs", "2026-11-10"));
        list.add(new PantryItem("Potatoes", 10.0, "pcs", "2026-11-15"));
        list.add(new PantryItem("Avocados", 3.0, "pcs", "2026-10-27"));
        list.add(new PantryItem("Cucumber", 4.0, "pcs", "2026-11-01"));
        list.add(new PantryItem("Beans", 4.0, "can", "2027-12-31"));
        return list;
    }
}
