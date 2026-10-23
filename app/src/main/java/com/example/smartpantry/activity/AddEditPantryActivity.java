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

import java.util.Locale;

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
        setContentView(R.layout.add_edit_pantry);

        pantryRepo = new PantryRepo();

        tvTitle = findViewById(R.id.tvFormTitle);
        etName = findViewById(R.id.etName);
        etQuantity = findViewById(R.id.etQuantity);
        etUnit = findViewById(R.id.etUnit);
        etExpiry = findViewById(R.id.etExpiry);
        btnSave = findViewById(R.id.btnSave);

        // Checks if passing an item for editing via intent
        if (getIntent() != null && getIntent().hasExtra(EXTRA_ITEM)) {
            try {
                existingItem = (PantryItem) getIntent().getSerializableExtra(EXTRA_ITEM);
            } catch (Exception e) {
                // Handle deserialization or cast exception
            }

            if (existingItem != null) {
                if (tvTitle != null) {
                    tvTitle.setText(R.string.edit_pantry_ingredient);
                }
                if (existingItem.getName() != null) {
                    etName.setText(existingItem.getName());
                }
                etQuantity.setText(formatQuantity(existingItem.getQuantity()));
                if (existingItem.getUnit() != null) {
                    etUnit.setText(existingItem.getUnit());
                }
                if (existingItem.getExpirydate() != null) {
                    etExpiry.setText(existingItem.getExpirydate());
                }
            }
        }

        btnSave.setOnClickListener(v -> saveIngredient());
    }

    private String formatQuantity(double val) {
        if (val == (long) val) {
            return String.format(Locale.US, "%d", (long) val);
        } else {
            return String.valueOf(val);
        }
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
            isValid = false;
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
            etUnit.setError("Unit is required (e.g. pcs, g, cups)");
            isValid = false;
        }

        if (!isValid) return; // Stop if inputs are invalid.

        btnSave.setEnabled(false); // Disable during async network call

        // Save or update to Firestore
        if (existingItem != null) {
            existingItem.setName(name);
            existingItem.setQuantity(quantity);
            existingItem.setUnit(unit);
            existingItem.setExpirydate(expiry);

            pantryRepo.update(existingItem, new PantryRepo.ActionCallBack() {
                @Override
                public void onSuccess() {
                    Toast.makeText(AddEditPantryActivity.this, "Ingredient updated!", Toast.LENGTH_SHORT).show();
                    finish();
                }

                @Override
                public void onError(Exception e) {
                    btnSave.setEnabled(true);
                    Toast.makeText(AddEditPantryActivity.this, "Error updating item: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            // Add new record
            PantryItem newItem = new PantryItem(name, quantity, unit, expiry);
            pantryRepo.add(newItem, new PantryRepo.ActionCallBack() {
                @Override
                public void onSuccess() {
                    Toast.makeText(AddEditPantryActivity.this, "Ingredient saved!", Toast.LENGTH_SHORT).show();
                    finish();
                }

                @Override
                public void onError(Exception e) {
                    btnSave.setEnabled(true);
                    Toast.makeText(AddEditPantryActivity.this, "Error saving item: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
