package com.example.utasteapplication;
/*
 * Author: Othmane El Moutaouakkil
 */

/**
 * RecipeIngredient Model Class
 * Represents an ingredient added to a recipe with QR code and quantity percentage
 */
public class RecipeIngredient {
    private int id;
    private int recipeId;
    private String qrCode;
    private String name; // User-entered Name for the ingredient
    private double quantityPercentage; // Percentage of the ingredient (e.g., 20%, 25%)
    private String addedAt;

    /**
     * Constructor for new ingredient (before database insertion)
     * @param recipeId The ID of the recipe this ingredient belongs to
     * @param qrCode The QR code scanned for this ingredient
     * @param Name User-entered name/Name for the ingredient
     * @param quantityPercentage Percentage quantity (0-100)
     */
    public RecipeIngredient(int recipeId, String qrCode, String Name, double quantityPercentage) {
        this.recipeId = recipeId;
        this.qrCode = qrCode;
        this.name = Name;
        this.quantityPercentage = quantityPercentage;
    }

    /**
     * Constructor for existing ingredient (from database)
     * @param id Database ID
     * @param recipeId The ID of the recipe this ingredient belongs to
     * @param qrCode The QR code for this ingredient
     * @param Name The ingredient name/Name
     * @param quantityPercentage Percentage quantity (0-100)
     * @param addedAt Timestamp when ingredient was added
     */
    public RecipeIngredient(int id, int recipeId, String qrCode, String Name,
                            double quantityPercentage, String addedAt) {
        this.id = id;
        this.recipeId = recipeId;
        this.qrCode = qrCode;
        this.name = Name;
        this.quantityPercentage = quantityPercentage;
        this.addedAt = addedAt;
    }

    // Getters
    public int getId() {
        return id;
    }

    public int getRecipeId() {
        return recipeId;
    }

    public String getQrCode() {
        return qrCode;
    }

    public String getName() {
        return name;
    }

    public double getQuantityPercentage() {
        return quantityPercentage;
    }

    public String getAddedAt() {
        return addedAt;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setRecipeId(int recipeId) {
        this.recipeId = recipeId;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public void setname(String name) {
        this.name = name;
    }

    public void setQuantityPercentage(double quantityPercentage) {
        if (quantityPercentage < 0) {
            this.quantityPercentage = 0;
        } else if (quantityPercentage > 100) {
            this.quantityPercentage = 100;
        } else {
            this.quantityPercentage = quantityPercentage;
        }
    }

    public void setAddedAt(String addedAt) {
        this.addedAt = addedAt;
    }

    /**
     * Validates the ingredient data
     * @return true if ingredient is valid, false otherwise
     */
    public boolean isValid() {
        if (name == null || name.trim().isEmpty()) {
            return false;
        }
        if (qrCode == null || qrCode.trim().isEmpty()) {
            return false;
        }
        if (quantityPercentage <= 0 || quantityPercentage > 100) {
            return false;
        }
        if (recipeId <= 0) {
            return false;
        }
        return true;
    }

    /**
     * Returns a formatted string representation of the ingredient
     * @return String with ingredient details
     */
    @Override
    public String toString() {
        return name + " - " + String.format("%.1f", quantityPercentage) + "%";
    }

    /**
     * Returns detailed information about the ingredient
     * @return Detailed string representation
     */
    public String toDetailedString() {
        return "Ingredient{" +
                "id=" + id +
                ", recipeId=" + recipeId +
                ", name='" + name + '\'' +
                ", qrCode='" + qrCode + '\'' +
                ", quantityPercentage=" + quantityPercentage +
                "%, addedAt='" + addedAt + '\'' +
                '}';
    }

    /**
     * Creates a copy of this ingredient
     * @return A new RecipeIngredient with the same values
     */
    public RecipeIngredient copy() {
        return new RecipeIngredient(id, recipeId, qrCode, name, quantityPercentage, addedAt);
    }

    /**
     * Compare two ingredients for equality based on QR code and recipe ID
     * @param obj Object to compare
     * @return true if ingredients are equal
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        RecipeIngredient that = (RecipeIngredient) obj;

        if (recipeId != that.recipeId) return false;
        return qrCode != null ? qrCode.equals(that.qrCode) : that.qrCode == null;
    }

    /**
     * Generate hash code based on recipe ID and QR code
     * @return hash code
     */
    @Override
    public int hashCode() {
        int result = recipeId;
        result = 31 * result + (qrCode != null ? qrCode.hashCode() : 0);
        return result;
    }
}