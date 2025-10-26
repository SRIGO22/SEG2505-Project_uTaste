package com.example.utasteapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class ChefService {
    private final DatabaseHelper dbHelper;

    public ChefService(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

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

    public void deleteIngredient(int ingredientId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        try {
            db.delete("ingredients", "id = ?", new String[]{String.valueOf(ingredientId)});
        } finally {
            db.close();
        }
    }
}
