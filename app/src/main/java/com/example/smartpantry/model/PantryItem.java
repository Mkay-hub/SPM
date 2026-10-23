package com.example.smartpantry.model;

import com.google.firebase.firestore.DocumentId;
import java.io.Serializable;

/**
 * One ingredient the user has at home.
 * Stored as a document in pantry collection.
 **/
public class PantryItem implements Serializable {
    private String id;      // Firestore document ID; Automatically generated.
    private String name;
    private double quantity;
    private String unit;    // unit of measurement.
    private String expirydate;      // optional, "yyyy-MM-dd" or null

    public PantryItem() {}

    public PantryItem(String name, double quantity, String unit, String expirydate) {
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
        this.expirydate = expirydate;
    }

    @DocumentId
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public double getQuantity() { return quantity; }
    public void setQuantity(double quantity) { this.quantity = quantity; }

    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }

    public String getExpirydate() { return expirydate; }
    public void setExpirydate(String expirydate) { this.expirydate = expirydate; }
}
