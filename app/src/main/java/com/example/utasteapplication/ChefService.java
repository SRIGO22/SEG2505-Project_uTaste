package com.example.utasteapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

// Classe qui gère les actions du chef dans la base de données
// Elle permet de créer, modifier, supprimer des recettes et leurs ingrédients
public class ChefService {
    private final DatabaseHelper dbHelper;

    // Le constructeur reçoit un contexte Android pour initialiser le helper SQLite
    public ChefService(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    // Crée une nouvelle recette dans la base de données
    // Retourne l’ID de la recette ajoutée, ou -1 si la recette est invalide
    public long createRecipe(Recipe recipe) {
        if (!recipe.isValid()) return -1;

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long id = -1;

        try {
            ContentValues values = new ContentValues();
            values.put("name", recipe.getName());
            values.put("imagePath", recipe.getImagePath());
            values.put("description", recipe.getDescription());
            values.put("createdAt", recipe.getCreatedAt());
            values.put("modifiedAt", recipe.getModifiedAt());
            id = db.insert("recipes", null, values);
        } finally {
            db.close();
        }

        return id;
    }

    // Met à jour le nom et la description d’une recette existante
    // Retourne le nombre de lignes affectées
    public int updateRecipe(int recipeId, String newName, String newDescription) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rows;

        try {
            ContentValues values = new ContentValues();
            values.put("name", newName);
            values.put("description", newDescription);
            values.put("modifiedAt", System.currentTimeMillis());
            rows = db.update("recipes", values, "id = ?", new String[]{String.valueOf(recipeId)});
        } finally {
            db.close();
        }

        return rows;
    }

    // Supprime une recette et tous ses ingrédients liés
    public void deleteRecipe(int recipeId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        try {
            db.beginTransaction();
            db.delete("ingredients", "recipeId = ?", new String[]{String.valueOf(recipeId)});
            db.delete("recipes", "id = ?", new String[]{String.valueOf(recipeId)});
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    // Ajoute un nouvel ingrédient dans la base de données
    // Retourne son ID généré
    public long addIngredient(Ingredient ingredient) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long id;

        try {
            ContentValues values = new ContentValues();
            values.put("recipeId", ingredient.getRecipeId());
            values.put("title", ingredient.getTitle());
            values.put("qrCode", ingredient.getQrCode());
            values.put("quantityPercent", ingredient.getQuantityPercent());
            id = db.insert("ingredients", null, values);
        } finally {
            db.close();
        }

        return id;
    }

    // Met à jour la quantité (en pourcentage) d’un ingrédient
    // Retourne le nombre de lignes affectées
    public int updateIngredientQuantity(int ingredientId, double newPercent) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rows;

        try {
            ContentValues values = new ContentValues();
            values.put("quantityPercent", newPercent);
            rows = db.update("ingredients", values, "id = ?", new String[]{String.valueOf(ingredientId)});
        } finally {
            db.close();
        }

        return rows;
    }

    // Supprime un ingrédient de la base de données à partir de son ID
    public void deleteIngredient(int ingredientId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        try {
            db.delete("ingredients", "id = ?", new String[]{String.valueOf(ingredientId)});
        } finally {
            db.close();
        }
    }
}
