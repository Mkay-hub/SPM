package com.example.smartpantry.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.smartpantry.R;
import com.example.smartpantry.database.PantryRepo;
import com.example.smartpantry.model.PantryItem;

public class AddEditPantryActivity extends AppCompatActivity {

    public static final String EXTRA_ITEM = "extra_pantry_item";

    public EditText etName, etQuantity, etUnit, etExpiry;
    private TextView tvTitle;
    private Button btnSave;
    private PantryRepo pantryRepo;
    private PantryItem existingItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_pantry);

        pantryRepo = new PantryRepo();

        tvTitle = findViewById(R.id.tvFormTitle);
        etName = findViewById(R.id.etName);
        etQuantity = findViewById(R.id.etQuantity);
        etUnit = findViewById(R.id.etUnit);
        etExpiry = findViewById(R.id.etExpiry);
        btnSave = findViewById(R.id.btnSave);

        // Checks if passing an item for editing via intent
        if (getIntent().hasExtra(EXTRA_ITEM)) {
            existingItem = (PantryItem) getIntent().getSerializableExtra(EXTRA_ITEM);

            if (existingItem != null) {
                tvTitle.setText("Edit Pantry Ingredient");
                etName.setText(existingItem.getName());
                etQuantity.setText(String.valueOf(existingItem.getQuantity()));
                etUnit.setText(existingItem.getUnit());
                etExpiry.setText(existingItem.getExpirydate());
            }
        }
        btnSave.setOnClickListener(v -> saveIngredient());
    }

    public void saveIngredient() {
        String name = etName.getText().toString().trim();
        String quantityStr = etQuantity.getText().toString().trim();
        String unit = etUnit.getText().toString().trim();
        String expiry = etExpiry.getText().toString().trim();

        // Validates with visible errors
        boolean isValid = true;

        if (name.isEmpty()) {
            etName.setError("Ingredient name is required");
            isValid = false;
        }

        double quantity = 0;
        if (quantityStr.isEmpty()) {
            etQuantity.setError("Quantity is required");
        } else {
            try {
                quantity = Double.parseDouble(quantityStr);
                if (quantity <= 0) {
                    etQuantity.setError("Quantity must be greater than 0");
                    isValid = false;
                }
            } catch (NumberFormatException e) {
                etQuantity.setError("Enter a valid number");
                isValid = false;
            }
        }
        if (unit.isEmpty()) {
            etUnit.setError("Unit is required ( e.g. pcs, g, cups");
            isValid = false;
        }
        if (!isValid) return; // Stop if inputs are invalid.

        // Save or update to Firestore
        if (existingItem != null) {
            existingItem.setName(name);
            existingItem.setQuantity(quantity);
            existingItem.setUnit(unit);
            existingItem.setExpirydate(expiry);

            pantryRepo.update(existingItem, new PantryRepo.ActionCallBack() {
                @Override
                public void onSuccess() {
                    Toast.makeText(AddEditPantryActivity.this, "Ingredient/s updated!", Toast.LENGTH_SHORT).show();
                    finish(); // Close screen and returns to Ingredient list.
                }

                @Override
                public void onError(Exception e) {
                    Toast.makeText(AddEditPantryActivity.this, "Error updating item/s", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            // Add new record
            PantryItem newItem = new PantryItem(name, quantity, unit, expiry);
            pantryRepo.add(newItem, new PantryRepo.ActionCallBack() {
                @Override
                public void onSuccess() {
                    Toast.makeText(AddEditPantryActivity.this, "Ingredient/s saved!", Toast.LENGTH_SHORT).show();
                    finish();
                }

                @Override
                public void onError(Exception e) {
                    Toast.makeText(AddEditPantryActivity.this, "Error saving item/s.", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}




















