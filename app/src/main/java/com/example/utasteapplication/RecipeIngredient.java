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
    private String name; // User-entered name for the ingredient
    private double quantityPercentage; // Percentage of the ingredient (0-100)
    // Making addedAt final if loaded from DB, assuming it's set once.
    private final String addedAt;

    /**
     * Helper to defensively trim strings.
     * @param str The input string.
     * @return The trimmed string or null.
     */
    private String cleanString(String str) {
        return str != null ? str.trim() : null;
    }

    /**
     * Constructor for new ingredient (before database insertion)
     * Timestamps will be null and set by the database
     * @param recipeId The ID of the recipe this ingredient belongs to
     * @param qrCode The QR code scanned for this ingredient
     * @param name User-entered name/Name for the ingredient
     * @param quantityPercentage Percentage quantity (0-100)
     */
    // Fix: Parameter 'Name' changed to 'name' for convention.
    public RecipeIngredient(int recipeId, String qrCode, String name, double quantityPercentage) {
        this.recipeId = recipeId;
        // Improvement: Apply defensive trimming
        this.qrCode = cleanString(qrCode);
        this.name = cleanString(name);
        this.quantityPercentage = quantityPercentage; // The setter logic handles boundaries
        this.addedAt = null; // DB will set this
    }

    /**
     * Constructor for existing ingredient (from database)
     * @param id Database ID
     * @param recipeId The ID of the recipe this ingredient belongs to
     * @param qrCode The QR code for this ingredient
     * @param name The ingredient name/Name
     * @param quantityPercentage Percentage quantity (0-100)
     * @param addedAt Timestamp when ingredient was added
     */
    // Fix: Parameter 'Name' changed to 'name' for convention.
    public RecipeIngredient(int id, int recipeId, String qrCode, String name,
                            double quantityPercentage, String addedAt) {
        this.id = id;
        this.recipeId = recipeId;
        // Improvement: Apply defensive trimming
        this.qrCode = cleanString(qrCode);
        this.name = cleanString(name);
        this.quantityPercentage = quantityPercentage;
        this.addedAt = addedAt; // Loaded as final
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
        // Improvement: Apply defensive trimming
        this.qrCode = cleanString(qrCode);
    }

    /**
     * Fix: Corrected typo from setname to setName
     * Improvement: Apply defensive trimming
     */
    public void setName(String name) {
        this.name = cleanString(name);
    }

    public void setQuantityPercentage(double quantityPercentage) {
        // Your boundary-checking logic is good and is kept.
        if (quantityPercentage < 0) {
            this.quantityPercentage = 0;
        } else if (quantityPercentage > 100) {
            this.quantityPercentage = 100;
        } else {
            this.quantityPercentage = quantityPercentage;
        }
    }

    // Removed setAddedAt because it's managed as a 'final' field or by the database insert logic.

    /**
     * Validates the ingredient data
     * @return true if ingredient is valid, false otherwise
     */
    public boolean isValid() {
        if (name == null || name.isEmpty()) { // .isEmpty() is sufficient if cleanString() is used
            return false;
        }
        if (qrCode == null || qrCode.isEmpty()) {
            return false;
        }
        // Fix/Clarification: Allows 0% quantity, as quantityPercentage >= 0.
        // It's up to the user to decide if 0% is meaningful, but this is safer than disabling it.
        if (quantityPercentage < 0 || quantityPercentage > 100) {
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
        // Improvement: Use cleanString() on current object's field just in case it wasn't trimmed.
        return cleanString(qrCode) != null ? cleanString(qrCode).equals(cleanString(that.qrCode)) : cleanString(that.qrCode) == null;
    }

    /**
     * Generate hash code based on recipe ID and QR code
     * @return hash code
     */
    @Override
    public int hashCode() {
        int result = recipeId;
        // Improvement: Use cleanString() on qrCode for consistent hashing.
        String cleanedQrCode = cleanString(qrCode);
        result = 31 * result + (cleanedQrCode != null ? cleanedQrCode.hashCode() : 0);
        return result;
    }
}