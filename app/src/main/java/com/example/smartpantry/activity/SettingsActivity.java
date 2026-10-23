package com.example.smartpantry.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.smartpantry.MainActivity;
import com.example.smartpantry.R;
import com.google.android.material.switchmaterial.SwitchMaterial;

public class SettingsActivity extends AppCompatActivity {

    private static final String PREFS_SETTINGS = "SmartPantrySettings";
    private static final String KEY_EXPIRY_ALERTS = "key_expiry_alerts";
    private static final String KEY_UNIT_SYSTEM = "key_unit_system";

    private SwitchMaterial switchExpiry;
    private RadioGroup rgUnits;
    private RadioButton rbMetric, rbImperial;
    private Button btnSave;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        prefs = getSharedPreferences(PREFS_SETTINGS, Context.MODE_PRIVATE);

        switchExpiry = findViewById(R.id.switchExpiryAlerts);
        rgUnits = findViewById(R.id.rgUnitPreference);
        rbMetric = findViewById(R.id.rbMetric);
        rbImperial = findViewById(R.id.rbImperial);
        btnSave = findViewById(R.id.btnSaveSettings);

        // Restore saved settings
        boolean alertEnabled = prefs.getBoolean(KEY_EXPIRY_ALERTS, true);
        String unitSystem = prefs.getString(KEY_UNIT_SYSTEM, "Metric");

        switchExpiry.setChecked(alertEnabled);
        if ("Imperial".equalsIgnoreCase(unitSystem)) {
            rbImperial.setChecked(true);
        } else {
            rbMetric.setChecked(true);
        }

        btnSave.setOnClickListener(v -> saveSettings());

        initNavigation();
    }

    private void saveSettings() {
        boolean alertEnabled = switchExpiry.isChecked();
        String selectedUnit = rbImperial.isChecked() ? "Imperial" : "Metric";

        prefs.edit()
                .putBoolean(KEY_EXPIRY_ALERTS, alertEnabled)
                .putString(KEY_UNIT_SYSTEM, selectedUnit)
                .apply();

        Toast.makeText(this, "Preferences saved!", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void initNavigation() {
        Button btnPantry = findViewById(R.id.btnNavPantry);
        Button btnRecipes = findViewById(R.id.btnNavRecipes);
        Button btnSettings = findViewById(R.id.btnNavBarSettings);

        if (btnSettings != null) {
            btnSettings.setEnabled(false); // Active tab
        }

        if (btnPantry != null) {
            btnPantry.setOnClickListener(v -> {
                Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            });
        }

        if (btnRecipes != null) {
            btnRecipes.setOnClickListener(v -> {
                Intent intent = new Intent(SettingsActivity.this, SuggestedRecipesActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            });
        }
    }
}
