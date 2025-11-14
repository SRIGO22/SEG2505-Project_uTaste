package com.example.utasteapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/*
 * Author: Othmane El Moutaouakkil
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "utaste.db";
    private static final int DATABASE_VERSION = 3; // bumped version

    // Users table
    private static final String TABLE_USERS = "users";
    private static final String COL_USER_ID = "id";
    private static final String COL_USER_EMAIL = "email";
    private static final String COL_USER_PASSWORD = "password";
    private static final String COL_USER_FIRST_NAME = "first_name";
    private static final String COL_USER_LAST_NAME = "last_name";
    private static final String COL_USER_ROLE = "role";
    private static final String COL_USER_CREATED_AT = "created_at";
    private static final String COL_USER_MODIFIED_AT = "modified_at";

    // Recipes table
    private static final String TABLE_RECIPES = "recipes";
    private static final String COL_RECIPE_ID = "id";
    private static final String COL_RECIPE_NAME = "name";
    private static final String COL_RECIPE_IMAGE = "image_path";
    private static final String COL_RECIPE_DESCRIPTION = "description";
    private static final String COL_RECIPE_CREATED_AT = "created_at";
    private static final String COL_RECIPE_MODIFIED_AT = "modified_at";
    private static final String COL_RECIPE_IS_DEFAULT = "is_default";

    // Ingredients table
    private static final String TABLE_INGREDIENTS = "recipe_ingredients";
    private static final String COL_INGREDIENT_ID = "id";
    private static final String COL_INGREDIENT_RECIPE_ID = "recipe_id";
    private static final String COL_INGREDIENT_QR_CODE = "qr_code";
    private static final String COL_INGREDIENT_NAME = "name";
    private static final String COL_INGREDIENT_QUANTITY = "quantity_percentage";
    private static final String COL_INGREDIENT_ADDED_AT = "added_at";
    private static final String COL_INGREDIENT_IS_DEFAULT = "is_default";

    private static DatabaseHelper instance;

    DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static synchronized DatabaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseHelper(context.getApplicationContext());
        }
        return instance;
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Users table
        db.execSQL("CREATE TABLE " + TABLE_USERS + " (" +
                COL_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_USER_EMAIL + " TEXT UNIQUE NOT NULL, " +
                COL_USER_PASSWORD + " TEXT NOT NULL, " +
                COL_USER_FIRST_NAME + " TEXT, " +
                COL_USER_LAST_NAME + " TEXT, " +
                COL_USER_ROLE + " TEXT NOT NULL, " +
                COL_USER_CREATED_AT + " TEXT NOT NULL, " +
                COL_USER_MODIFIED_AT + " TEXT NOT NULL)");

        // Recipes table
        db.execSQL("CREATE TABLE " + TABLE_RECIPES + " (" +
                COL_RECIPE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_RECIPE_NAME + " TEXT UNIQUE NOT NULL, " +
                COL_RECIPE_IMAGE + " TEXT, " +
                COL_RECIPE_DESCRIPTION + " TEXT, " +
                COL_RECIPE_CREATED_AT + " TEXT NOT NULL, " +
                COL_RECIPE_MODIFIED_AT + " TEXT NOT NULL, " +
                COL_RECIPE_IS_DEFAULT + " INTEGER DEFAULT 0)");

        // Ingredients table
        db.execSQL("CREATE TABLE " + TABLE_INGREDIENTS + " (" +
                COL_INGREDIENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_INGREDIENT_RECIPE_ID + " INTEGER NOT NULL, " +
                COL_INGREDIENT_QR_CODE + " TEXT NOT NULL, " +
                COL_INGREDIENT_NAME + " TEXT NOT NULL, " +
                COL_INGREDIENT_QUANTITY + " REAL NOT NULL, " +
                COL_INGREDIENT_ADDED_AT + " TEXT NOT NULL, " +
                COL_INGREDIENT_IS_DEFAULT + " INTEGER DEFAULT 0, " +
                "FOREIGN KEY(" + COL_INGREDIENT_RECIPE_ID + ") REFERENCES " +
                TABLE_RECIPES + "(" + COL_RECIPE_ID + ") ON DELETE CASCADE)");

        initializeDefaultUsers(db);
        seedDefaultRecipes(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INGREDIENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECIPES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    private void initializeDefaultUsers(SQLiteDatabase db) {
        String timestamp = getCurrentTimestamp();
        addUser(db, "admin@utaste.com", "admin123", "Admin", "User", "Administrator", timestamp);
        addUser(db, "chef@utaste.com", "chef123", "Chef", "User", "Chef", timestamp);
        addUser(db, "waiter@utaste.com", "waiter123", "Waiter", "User", "Waiter", timestamp);
    }

    private void addUser(SQLiteDatabase db, String email, String password, String firstName, String lastName, String role, String timestamp) {
        ContentValues values = new ContentValues();
        values.put(COL_USER_EMAIL, email);
        values.put(COL_USER_PASSWORD, password);
        values.put(COL_USER_FIRST_NAME, firstName);
        values.put(COL_USER_LAST_NAME, lastName);
        values.put(COL_USER_ROLE, role);
        values.put(COL_USER_CREATED_AT, timestamp);
        values.put(COL_USER_MODIFIED_AT, timestamp);
        db.insert(TABLE_USERS, null, values);
    }

    private String getCurrentTimestamp() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
    }

    // ==================== DEFAULT RECIPES ====================
    private void seedDefaultRecipes(SQLiteDatabase db) {
        String timestamp = getCurrentTimestamp();

        // --- Omelette Recipe ---
        ContentValues omelette = new ContentValues();
        omelette.put(COL_RECIPE_NAME, "Omelette");
        omelette.put(COL_RECIPE_IMAGE, "https://www.olivetomato.com/wp-content/uploads/2016/02/SAM4952-1.jpg");
        omelette.put(COL_RECIPE_DESCRIPTION, "Simple omelette with eggs, salt, and pepper.");
        omelette.put(COL_RECIPE_CREATED_AT, timestamp);
        omelette.put(COL_RECIPE_MODIFIED_AT, timestamp);
        omelette.put(COL_RECIPE_IS_DEFAULT, 1);
        long omeletteId = db.insert(TABLE_RECIPES, null, omelette);

        insertDefaultIngredient(db, "Eggs", "3 units", omeletteId);
        insertDefaultIngredient(db, "Salt", "1 pinch", omeletteId);
        insertDefaultIngredient(db, "Pepper", "1 pinch", omeletteId);

        // --- Pancakes Recipe ---
        ContentValues pancakes = new ContentValues();
        pancakes.put(COL_RECIPE_NAME, "Pancakes");
        pancakes.put(COL_RECIPE_IMAGE, "pancakes.jpg");
        pancakes.put(COL_RECIPE_DESCRIPTION, "Fluffy pancakes made with flour, milk, and eggs.");
        pancakes.put(COL_RECIPE_CREATED_AT, timestamp);
        pancakes.put(COL_RECIPE_MODIFIED_AT, timestamp);
        pancakes.put(COL_RECIPE_IS_DEFAULT, 1);
        long pancakesId = db.insert(TABLE_RECIPES, null, pancakes);

        insertDefaultIngredient(db, "Flour", "200 g", pancakesId);
        insertDefaultIngredient(db, "Milk", "250 ml", pancakesId);
        insertDefaultIngredient(db, "Eggs", "2 units", pancakesId);
        insertDefaultIngredient(db, "Sugar", "2 tbsp", pancakesId);
        insertDefaultIngredient(db, "Butter", "1 tbsp", pancakesId);
    }

    private void insertDefaultIngredient(SQLiteDatabase db, String name, String quantity, long recipeId) {
        ContentValues ing = new ContentValues();
        ing.put(COL_INGREDIENT_NAME, name);
        ing.put(COL_INGREDIENT_QUANTITY, quantity);
        ing.put(COL_INGREDIENT_RECIPE_ID, recipeId);
        ing.put(COL_INGREDIENT_QR_CODE, name.toLowerCase().replace(" ", "_") + "_qr");
        ing.put(COL_INGREDIENT_ADDED_AT, getCurrentTimestamp());
        ing.put(COL_INGREDIENT_IS_DEFAULT, 1);
        db.insert(TABLE_INGREDIENTS, null, ing);
    }


    // ==================== DELETE NON-DEFAULT RECIPES/INGREDIENTS ====================
    public void deleteNonDefaultContent() {
        SQLiteDatabase db = getWritableDatabase();
        // Delete ingredients not marked as default
        db.delete(TABLE_INGREDIENTS, COL_INGREDIENT_IS_DEFAULT + " = 0", null);
        // Delete recipes not marked as default
        db.delete(TABLE_RECIPES, COL_RECIPE_IS_DEFAULT + " = 0", null);
    }
    // ==================== RECIPE OPERATIONS ====================

    public long addRecipe(Recipe recipe) {
        if (getRecipeByName(recipe.getName()) != null) return -1;

        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        String timestamp = getCurrentTimestamp();
        values.put(COL_RECIPE_NAME, recipe.getName().trim());
        values.put(COL_RECIPE_IMAGE, recipe.getImagePath());
        values.put(COL_RECIPE_DESCRIPTION, recipe.getDescription());
        values.put(COL_RECIPE_CREATED_AT, timestamp);
        values.put(COL_RECIPE_MODIFIED_AT, timestamp);

        return db.insert(TABLE_RECIPES, null, values);
    }

    public Recipe getRecipeById(int id) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_RECIPES, null, COL_RECIPE_ID + "=?", new String[]{String.valueOf(id)}, null, null, null);
        Recipe recipe = null;
        if (cursor != null && cursor.moveToFirst()) {
            recipe = mapRecipe(cursor);
            cursor.close();
        }
        return recipe;
    }

    public Recipe getRecipeByName(String name) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_RECIPES, null, COL_RECIPE_NAME + "=?", new String[]{name.trim()}, null, null, null);
        Recipe recipe = null;
        if (cursor != null && cursor.moveToFirst()) {
            recipe = mapRecipe(cursor);
            cursor.close();
        }
        return recipe;
    }

    private Recipe mapRecipe(Cursor cursor) {
        int idIndex = cursor.getColumnIndexOrThrow(COL_RECIPE_ID);
        int nameIndex = cursor.getColumnIndexOrThrow(COL_RECIPE_NAME);
        int imageIndex = cursor.getColumnIndexOrThrow(COL_RECIPE_IMAGE);
        int descIndex = cursor.getColumnIndexOrThrow(COL_RECIPE_DESCRIPTION);
        int createdIndex = cursor.getColumnIndexOrThrow(COL_RECIPE_CREATED_AT);
        int modifiedIndex = cursor.getColumnIndexOrThrow(COL_RECIPE_MODIFIED_AT);
        
        return new Recipe(
                cursor.getInt(idIndex),
                cursor.isNull(nameIndex) ? "" : cursor.getString(nameIndex),
                cursor.isNull(imageIndex) ? "" : cursor.getString(imageIndex),
                cursor.isNull(descIndex) ? "" : cursor.getString(descIndex),
                cursor.isNull(createdIndex) ? "" : cursor.getString(createdIndex),
                cursor.isNull(modifiedIndex) ? "" : cursor.getString(modifiedIndex)
        );
    }

    public List<Recipe> getAllRecipes() {
        List<Recipe> recipes = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_RECIPES, null, null, null, null, null, COL_RECIPE_NAME + " ASC");
        if (cursor != null && cursor.moveToFirst()) {
            do {
                recipes.add(mapRecipe(cursor));
            } while (cursor.moveToNext());
            cursor.close();
        }
        return recipes;
    }

    public boolean updateRecipe(int id, String name, String imagePath, String description) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_RECIPE_NAME, name.trim());
        values.put(COL_RECIPE_IMAGE, imagePath);
        values.put(COL_RECIPE_DESCRIPTION, description);
        values.put(COL_RECIPE_MODIFIED_AT, getCurrentTimestamp());

        int rows = db.update(TABLE_RECIPES, values, COL_RECIPE_ID + "=?", new String[]{String.valueOf(id)});
        return rows > 0;
    }

    public boolean deleteRecipe(int id) {
        SQLiteDatabase db = getWritableDatabase();
        int rows = db.delete(TABLE_RECIPES, COL_RECIPE_ID + "=?", new String[]{String.valueOf(id)});
        return rows > 0;
    }

    // ==================== INGREDIENT OPERATIONS ====================

    public long addIngredient(RecipeIngredient ingredient) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        double quantity = ingredient.getQuantityPercentage();
        quantity = Math.max(0, Math.min(100, quantity));

        values.put(COL_INGREDIENT_RECIPE_ID, ingredient.getRecipeId());
        values.put(COL_INGREDIENT_QR_CODE, ingredient.getQrCode().trim());
        values.put(COL_INGREDIENT_NAME, ingredient.getName().trim());
        values.put(COL_INGREDIENT_QUANTITY, quantity);
        values.put(COL_INGREDIENT_ADDED_AT, getCurrentTimestamp());

        return db.insert(TABLE_INGREDIENTS, null, values);
    }

    public List<RecipeIngredient> getIngredientsForRecipe(int recipeId) {
        List<RecipeIngredient> ingredients = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_INGREDIENTS, null, COL_INGREDIENT_RECIPE_ID + "=?", new String[]{String.valueOf(recipeId)}, null, null, COL_INGREDIENT_NAME + " ASC");
        if (cursor != null && cursor.moveToFirst()) {
            do {
                ingredients.add(mapIngredient(cursor));
            } while (cursor.moveToNext());
            cursor.close();
        }
        return ingredients;
    }

    public RecipeIngredient getIngredientById(int id) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_INGREDIENTS, null, COL_INGREDIENT_ID + "=?", new String[]{String.valueOf(id)}, null, null, null);
        RecipeIngredient ingredient = null;
        if (cursor != null && cursor.moveToFirst()) {
            ingredient = mapIngredient(cursor);
            cursor.close();
        }
        return ingredient;
    }

    private RecipeIngredient mapIngredient(Cursor cursor) {
        return new RecipeIngredient(
                cursor.getInt(cursor.getColumnIndexOrThrow(COL_INGREDIENT_ID)),
                cursor.getInt(cursor.getColumnIndexOrThrow(COL_INGREDIENT_RECIPE_ID)),
                cursor.getString(cursor.getColumnIndexOrThrow(COL_INGREDIENT_QR_CODE)),
                cursor.getString(cursor.getColumnIndexOrThrow(COL_INGREDIENT_NAME)),
                cursor.getDouble(cursor.getColumnIndexOrThrow(COL_INGREDIENT_QUANTITY)),
                cursor.getString(cursor.getColumnIndexOrThrow(COL_INGREDIENT_ADDED_AT))
        );
    }

    public boolean updateIngredient(int id, String qr, String name, double quantity) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_INGREDIENT_QR_CODE, qr.trim());
        values.put(COL_INGREDIENT_NAME, name.trim());
        values.put(COL_INGREDIENT_QUANTITY, Math.max(0, Math.min(100, quantity)));
        int rows = db.update(TABLE_INGREDIENTS, values, COL_INGREDIENT_ID + "=?", new String[]{String.valueOf(id)});
        return rows > 0;
    }

    public boolean deleteIngredient(int ingredientId) {
        SQLiteDatabase db = getWritableDatabase();
        int rows = db.delete(TABLE_INGREDIENTS, COL_INGREDIENT_ID + "=?", new String[]{String.valueOf(ingredientId)});
        return rows > 0;
    }

    public int getIngredientCount(int recipeId) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_INGREDIENTS + " WHERE " + COL_INGREDIENT_RECIPE_ID + "=?", new String[]{String.valueOf(recipeId)});
        int count = 0;
        if (cursor != null && cursor.moveToFirst()) {
            count = cursor.getInt(0);
            cursor.close();
        }
        return count;
    }

    // ==================== DATABASE RESET & STATS ====================

    public void resetDatabase() {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_INGREDIENTS, null, null);
        db.delete(TABLE_RECIPES, null, null);
        db.delete(TABLE_USERS, null, null);

        db.execSQL("DELETE FROM sqlite_sequence WHERE name='" + TABLE_USERS + "'");
        db.execSQL("DELETE FROM sqlite_sequence WHERE name='" + TABLE_RECIPES + "'");
        db.execSQL("DELETE FROM sqlite_sequence WHERE name='" + TABLE_INGREDIENTS + "'");

        initializeDefaultUsers(db);
    }

    public String getDatabaseStats() {
        SQLiteDatabase db = getReadableDatabase();
        int userCount = getCount(TABLE_USERS);
        int recipeCount = getCount(TABLE_RECIPES);
        int ingredientCount = getCount(TABLE_INGREDIENTS);
        return "Users: " + userCount + ", Recipes: " + recipeCount + ", Ingredients: " + ingredientCount;
    }

    private int getCount(String table) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + table, null);
        int count = 0;
        if (cursor != null && cursor.moveToFirst()) {
            count = cursor.getInt(0);
            cursor.close();
        }
        return count;
    }

    public void updateIngredientQuantity(int id, double qty) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
    }


}
