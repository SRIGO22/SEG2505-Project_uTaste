/*
* Author: Sara Rigotti
*/

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class ChefService {
    private DatabaseHelper dbHelper;

    public ChefService(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void createRecipe(Recipe recipe) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", recipe.name);
        values.put("imagePath", recipe.imagePath);
        values.put("description", recipe.description);
        db.insert("recipes", null, values);
    }

    public void updateRecipe(int recipeId, String newName, String newDescription) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", newName);
        values.put("description", newDescription);
        db.update("recipes", values, "id = ?", new String[]{String.valueOf(recipeId)});
    }

    public void deleteRecipe(int recipeId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete("ingredients", "recipeId = ?", new String[]{String.valueOf(recipeId)});
        db.delete("recipes", "id = ?", new String[]{String.valueOf(recipeId)});
    }

    public void addIngredient(Ingredient ingredient) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("recipeId", ingredient.recipeId);
        values.put("title", ingredient.title);
        values.put("qrCode", ingredient.qrCode);
        values.put("quantityPercent", ingredient.quantityPercent);
        db.insert("ingredients", null, values);
    }

    public void updateIngredientQuantity(int ingredientId, double newPercent) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("quantityPercent", newPercent);
        db.update("ingredients", values, "id = ?", new String[]{String.valueOf(ingredientId)});
    }

    public void deleteIngredient(int ingredientId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete("ingredients", "id = ?", new String[]{String.valueOf(ingredientId)});
    }
}
