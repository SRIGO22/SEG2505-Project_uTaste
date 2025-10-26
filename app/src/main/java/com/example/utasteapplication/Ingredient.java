package com.example.utasteapplication;

/*
 * Author: Othmane El Moutaouakkil
 */

public class Ingredient {
    private int id;
    private int recipeId;
    private String title;
    private String qrCode;
    private double quantityPercent;

    // Constructor for new ingredient (before DB insert)
    public Ingredient(int recipeId, String title, String qrCode, double quantityPercent) {
        this.recipeId = recipeId;
        this.title = title;
        this.qrCode = qrCode;
        this.quantityPercent = quantityPercent;
    }

    // Constructor for ingredient loaded from database
    public Ingredient(int id, int recipeId, String title, String qrCode, double quantityPercent) {
        this.id = id;
        this.recipeId = recipeId;
        this.title = title;
        this.qrCode = qrCode;
        this.quantityPercent = quantityPercent;
    }

    // Getters
    public int getId() {
        return id;
    }

    public int getRecipeId() {
        return recipeId;
    }

    public String getTitle() {
        return title;
    }

    public String getQrCode() {
        return qrCode;
    }

    public double getQuantityPercent() {
        return quantityPercent;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setRecipeId(int recipeId) {
        this.recipeId = recipeId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public void setQuantityPercent(double quantityPercent) {
        this.quantityPercent = quantityPercent;
    }

    // Validate data
    public boolean isValid() {
        return (title != null && !title.trim().isEmpty()) && quantityPercent > 0;
    }

    @Override
    public String toString() {
        return title + " (" + quantityPercent + "%)";
    }
}
