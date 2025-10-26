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


/**
 * DatabaseHelper - SQLite database management
 * Manages users, recipes, and ingredients tables
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "utaste.db";
    private static final int DATABASE_VERSION = 1;

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

    // Recipe Ingredients table
    private static final String TABLE_INGREDIENTS = "recipe_ingredients";
    private static final String COL_INGREDIENT_ID = "id";
    private static final String COL_INGREDIENT_RECIPE_ID = "recipe_id";
    private static final String COL_INGREDIENT_QR_CODE = "qr_code";
    private static final String COL_INGREDIENT_TITLE = "title";
    private static final String COL_INGREDIENT_QUANTITY = "quantity_percentage";
    private static final String COL_INGREDIENT_ADDED_AT = "added_at";

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
    public void onCreate(SQLiteDatabase db) {
        // Create users table
        String createUsersTable = "CREATE TABLE " + TABLE_USERS + " (" +
                COL_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_USER_EMAIL + " TEXT UNIQUE NOT NULL, " +
                COL_USER_PASSWORD + " TEXT NOT NULL, " +
                COL_USER_FIRST_NAME + " TEXT, " +
                COL_USER_LAST_NAME + " TEXT, " +
                COL_USER_ROLE + " TEXT NOT NULL, " +
                COL_USER_CREATED_AT + " TEXT NOT NULL, " +
                COL_USER_MODIFIED_AT + " TEXT NOT NULL)";
        db.execSQL(createUsersTable);

        // Create recipes table
        String createRecipesTable = "CREATE TABLE " + TABLE_RECIPES + " (" +
                COL_RECIPE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_RECIPE_NAME + " TEXT UNIQUE NOT NULL, " +
                COL_RECIPE_IMAGE + " TEXT, " +
                COL_RECIPE_DESCRIPTION + " TEXT, " +
                COL_RECIPE_CREATED_AT + " TEXT NOT NULL, " +
                COL_RECIPE_MODIFIED_AT + " TEXT NOT NULL)";
        db.execSQL(createRecipesTable);

        // Create recipe ingredients table with foreign key
        String createIngredientsTable = "CREATE TABLE " + TABLE_INGREDIENTS + " (" +
                COL_INGREDIENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_INGREDIENT_RECIPE_ID + " INTEGER NOT NULL, " +
                COL_INGREDIENT_QR_CODE + " TEXT NOT NULL, " +
                COL_INGREDIENT_TITLE + " TEXT NOT NULL, " +
                COL_INGREDIENT_QUANTITY + " REAL NOT NULL, " +
                COL_INGREDIENT_ADDED_AT + " TEXT NOT NULL, " +
                "FOREIGN KEY(" + COL_INGREDIENT_RECIPE_ID + ") REFERENCES " +
                TABLE_RECIPES + "(" + COL_RECIPE_ID + ") ON DELETE CASCADE)";
        db.execSQL(createIngredientsTable);

        // Enable foreign key constraints
        db.execSQL("PRAGMA foreign_keys=ON;");

        // Initialize default users
        initializeDefaultUsers(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INGREDIENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECIPES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        db.execSQL("PRAGMA foreign_keys=ON;");
    }

    private void initializeDefaultUsers(SQLiteDatabase db) {
        String timestamp = getCurrentTimestamp();

        // Add default administrator
        ContentValues admin = new ContentValues();
        admin.put(COL_USER_EMAIL, "admin@utaste.com");
        admin.put(COL_USER_PASSWORD, "admin123");
        admin.put(COL_USER_FIRST_NAME, "Admin");
        admin.put(COL_USER_LAST_NAME, "User");
        admin.put(COL_USER_ROLE, "Administrator");
        admin.put(COL_USER_CREATED_AT, timestamp);
        admin.put(COL_USER_MODIFIED_AT, timestamp);
        db.insert(TABLE_USERS, null, admin);

        // Add default chef
        ContentValues chef = new ContentValues();
        chef.put(COL_USER_EMAIL, "chef@utaste.com");
        chef.put(COL_USER_PASSWORD, "chef123");
        chef.put(COL_USER_FIRST_NAME, "Chef");
        chef.put(COL_USER_LAST_NAME, "User");
        chef.put(COL_USER_ROLE, "Chef");
        chef.put(COL_USER_CREATED_AT, timestamp);
        chef.put(COL_USER_MODIFIED_AT, timestamp);
        db.insert(TABLE_USERS, null, chef);

        // Add default waiter
        ContentValues waiter = new ContentValues();
        waiter.put(COL_USER_EMAIL, "waiter@utaste.com");
        waiter.put(COL_USER_PASSWORD, "waiter123");
        waiter.put(COL_USER_FIRST_NAME, "Waiter");
        waiter.put(COL_USER_LAST_NAME, "User");
        waiter.put(COL_USER_ROLE, "Waiter");
        waiter.put(COL_USER_CREATED_AT, timestamp);
        waiter.put(COL_USER_MODIFIED_AT, timestamp);
        db.insert(TABLE_USERS, null, waiter);
    }

    // ==================== USER CRUD OPERATIONS ====================

    /**
     * Add a new user to the database
     */
    public long addUser(String email, String password, String firstName,
                        String lastName, String role) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        String timestamp = getCurrentTimestamp();

        values.put(COL_USER_EMAIL, email);
        values.put(COL_USER_PASSWORD, password);
        values.put(COL_USER_FIRST_NAME, firstName);
        values.put(COL_USER_LAST_NAME, lastName);
        values.put(COL_USER_ROLE, role);
        values.put(COL_USER_CREATED_AT, timestamp);
        values.put(COL_USER_MODIFIED_AT, timestamp);

        long result = db.insert(TABLE_USERS, null, values);
        return result;
    }

    /**
     * Get user by email
     */
    public User getUserByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, null,
                COL_USER_EMAIL + "=?", new String[]{email},
                null, null, null);

        User user = null;
        if (cursor != null && cursor.moveToFirst()) {
            String role = cursor.getString(cursor.getColumnIndexOrThrow(COL_USER_ROLE));
            String password = cursor.getString(cursor.getColumnIndexOrThrow(COL_USER_PASSWORD));
            String firstName = cursor.getString(cursor.getColumnIndexOrThrow(COL_USER_FIRST_NAME));
            String lastName = cursor.getString(cursor.getColumnIndexOrThrow(COL_USER_LAST_NAME));

            // Create user based on role
            switch (role) {
                case "Administrator":
                    user = new Administrator(email, password);
                    break;
                case "Chef":
                    user = new Chef(email, password);
                    break;
                case "Waiter":
                    user = new Waiter(email, password);
                    break;
            }

            // Update profile with names
            if (user != null) {
                user.updateProfile(firstName, lastName, email);
            }
            cursor.close();
        }
        return user;
    }

    /**
     * Check if user exists
     */
    public boolean userExists(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, new String[]{COL_USER_EMAIL},
                COL_USER_EMAIL + "=?", new String[]{email},
                null, null, null);
        boolean exists = cursor != null && cursor.getCount() > 0;
        if (cursor != null) {
            cursor.close();
        }
        return exists;
    }

    /**
     * Update user password
     */
    public boolean updateUserPassword(String email, String newPassword) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_USER_PASSWORD, newPassword);
        values.put(COL_USER_MODIFIED_AT, getCurrentTimestamp());

        int rows = db.update(TABLE_USERS, values, COL_USER_EMAIL + "=?",
                new String[]{email});
        return rows > 0;
    }

    /**
     * Reset user password to default
     */
    public boolean resetUserPassword(String email, String defaultPassword) {
        return updateUserPassword(email, defaultPassword);
    }

    /**
     * Update user profile (names)
     */
    public boolean updateUserProfile(String email, String firstName, String lastName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_USER_FIRST_NAME, firstName);
        values.put(COL_USER_LAST_NAME, lastName);
        values.put(COL_USER_MODIFIED_AT, getCurrentTimestamp());

        int rows = db.update(TABLE_USERS, values, COL_USER_EMAIL + "=?",
                new String[]{email});
        return rows > 0;
    }

    /**
     * Get all users from database
     */
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, null, null, null, null, null,
                COL_USER_EMAIL + " ASC");

        if (cursor != null && cursor.moveToFirst()) {
            do {
                String email = cursor.getString(cursor.getColumnIndexOrThrow(COL_USER_EMAIL));
                User user = getUserByEmail(email);
                if (user != null) {
                    users.add(user);
                }
            } while (cursor.moveToNext());
            cursor.close();
        }
        return users;
    }

    /**
     * Get all waiters
     */
    public List<Waiter> getAllWaiters() {
        List<Waiter> waiters = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, null,
                COL_USER_ROLE + "=?", new String[]{"Waiter"},
                null, null, COL_USER_EMAIL + " ASC");

        if (cursor != null && cursor.moveToFirst()) {
            do {
                String email = cursor.getString(cursor.getColumnIndexOrThrow(COL_USER_EMAIL));
                String password = cursor.getString(cursor.getColumnIndexOrThrow(COL_USER_PASSWORD));
                String firstName = cursor.getString(cursor.getColumnIndexOrThrow(COL_USER_FIRST_NAME));
                String lastName = cursor.getString(cursor.getColumnIndexOrThrow(COL_USER_LAST_NAME));

                Waiter waiter = new Waiter(email, password);
                waiter.updateProfile(firstName, lastName, email);
                waiters.add(waiter);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return waiters;
    }

    /**
     * Delete user by email
     */
    public boolean deleteUser(String email) {
        // Don't allow deletion of default users
        if (email.equals("admin@utaste.com") || email.equals("chef@utaste.com") || email.equals("waiter@utaste.com")) {
            return false;
        }

        SQLiteDatabase db = this.getWritableDatabase();
        int rows = db.delete(TABLE_USERS, COL_USER_EMAIL + "=?", new String[]{email});
        return rows > 0;
    }

    // ==================== RECIPE CRUD OPERATIONS ====================

    /**
     * Add a new recipe
     */
    public long addRecipe(Recipe recipe) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        String timestamp = getCurrentTimestamp();

        values.put(COL_RECIPE_NAME, recipe.getName());
        values.put(COL_RECIPE_IMAGE, recipe.getImagePath());
        values.put(COL_RECIPE_DESCRIPTION, recipe.getDescription());
        values.put(COL_RECIPE_CREATED_AT, timestamp);
        values.put(COL_RECIPE_MODIFIED_AT, timestamp);

        long result = db.insert(TABLE_RECIPES, null, values);
        return result;
    }

    /**
     * Get recipe by ID
     */
    public Recipe getRecipeById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_RECIPES, null,
                COL_RECIPE_ID + "=?", new String[]{String.valueOf(id)},
                null, null, null);

        Recipe recipe = null;
        if (cursor != null && cursor.moveToFirst()) {
            recipe = new Recipe(
                    cursor.getInt(cursor.getColumnIndexOrThrow(COL_RECIPE_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COL_RECIPE_NAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COL_RECIPE_IMAGE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COL_RECIPE_DESCRIPTION)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COL_RECIPE_CREATED_AT)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COL_RECIPE_MODIFIED_AT))
            );
            cursor.close();
        }
        return recipe;
    }

    /**
     * Get recipe by name
     */
    public Recipe getRecipeByName(String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_RECIPES, null,
                COL_RECIPE_NAME + "=?", new String[]{name},
                null, null, null);

        Recipe recipe = null;
        if (cursor != null && cursor.moveToFirst()) {
            recipe = new Recipe(
                    cursor.getInt(cursor.getColumnIndexOrThrow(COL_RECIPE_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COL_RECIPE_NAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COL_RECIPE_IMAGE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COL_RECIPE_DESCRIPTION)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COL_RECIPE_CREATED_AT)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COL_RECIPE_MODIFIED_AT))
            );
            cursor.close();
        }
        return recipe;
    }

    /**
     * Get all recipes
     */
    public List<Recipe> getAllRecipes() {
        List<Recipe> recipes = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_RECIPES, null, null, null, null, null,
                COL_RECIPE_NAME + " ASC");

        if (cursor != null && cursor.moveToFirst()) {
            do {
                Recipe recipe = new Recipe(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COL_RECIPE_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_RECIPE_NAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_RECIPE_IMAGE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_RECIPE_DESCRIPTION)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_RECIPE_CREATED_AT)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_RECIPE_MODIFIED_AT))
                );
                recipes.add(recipe);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return recipes;
    }

    /**
     * Update recipe
     */
    public boolean updateRecipe(int id, String name, String imagePath, String description) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_RECIPE_NAME, name);
        values.put(COL_RECIPE_IMAGE, imagePath);
        values.put(COL_RECIPE_DESCRIPTION, description);
        values.put(COL_RECIPE_MODIFIED_AT, getCurrentTimestamp());

        int rows = db.update(TABLE_RECIPES, values, COL_RECIPE_ID + "=?",
                new String[]{String.valueOf(id)});
        return rows > 0;
    }

    /**
     * Delete recipe (and its ingredients via CASCADE)
     */
    public boolean deleteRecipe(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        // Foreign key CASCADE will automatically delete ingredients
        int rows = db.delete(TABLE_RECIPES, COL_RECIPE_ID + "=?",
                new String[]{String.valueOf(id)});
        return rows > 0;
    }

    // ==================== INGREDIENT CRUD OPERATIONS ====================

    /**
     * Add ingredient to recipe
     */
    public long addIngredient(RecipeIngredient ingredient) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COL_INGREDIENT_RECIPE_ID, ingredient.getRecipeId());
        values.put(COL_INGREDIENT_QR_CODE, ingredient.getQrCode());
        values.put(COL_INGREDIENT_TITLE, ingredient.getName());
        values.put(COL_INGREDIENT_QUANTITY, ingredient.getQuantityPercentage());
        values.put(COL_INGREDIENT_ADDED_AT, getCurrentTimestamp());

        long result = db.insert(TABLE_INGREDIENTS, null, values);
        return result;
    }

    /**
     * Get all ingredients for a recipe
     */
    public List<RecipeIngredient> getIngredientsForRecipe(int recipeId) {
        List<RecipeIngredient> ingredients = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_INGREDIENTS, null,
                COL_INGREDIENT_RECIPE_ID + "=?", new String[]{String.valueOf(recipeId)},
                null, null, COL_INGREDIENT_TITLE + " ASC");

        if (cursor != null && cursor.moveToFirst()) {
            do {
                RecipeIngredient ingredient = new RecipeIngredient(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COL_INGREDIENT_ID)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COL_INGREDIENT_RECIPE_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_INGREDIENT_QR_CODE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_INGREDIENT_TITLE)),
                        cursor.getDouble(cursor.getColumnIndexOrThrow(COL_INGREDIENT_QUANTITY)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_INGREDIENT_ADDED_AT))
                );
                ingredients.add(ingredient);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return ingredients;
    }

    /**
     * Get ingredient by ID
     */
    public RecipeIngredient getIngredientById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_INGREDIENTS, null,
                COL_INGREDIENT_ID + "=?", new String[]{String.valueOf(id)},
                null, null, null);

        RecipeIngredient ingredient = null;
        if (cursor != null && cursor.moveToFirst()) {
            ingredient = new RecipeIngredient(
                    cursor.getInt(cursor.getColumnIndexOrThrow(COL_INGREDIENT_ID)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(COL_INGREDIENT_RECIPE_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COL_INGREDIENT_QR_CODE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COL_INGREDIENT_TITLE)),
                    cursor.getDouble(cursor.getColumnIndexOrThrow(COL_INGREDIENT_QUANTITY)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COL_INGREDIENT_ADDED_AT))
            );
            cursor.close();
        }
        return ingredient;
    }

    /**
     * Update ingredient quantity
     */
    public boolean updateIngredientQuantity(int ingredientId, double newQuantity) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_INGREDIENT_QUANTITY, newQuantity);

        int rows = db.update(TABLE_INGREDIENTS, values, COL_INGREDIENT_ID + "=?",
                new String[]{String.valueOf(ingredientId)});
        return rows > 0;
    }

    /**
     * Delete ingredient
     */
    public boolean deleteIngredient(int ingredientId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rows = db.delete(TABLE_INGREDIENTS, COL_INGREDIENT_ID + "=?",
                new String[]{String.valueOf(ingredientId)});
        return rows > 0;
    }

    // ==================== DATABASE MANAGEMENT ====================

    /**
     * Reset entire database to initial state
     * Deletes all data and recreates default users
     */
    public void resetDatabase() {
        SQLiteDatabase db = this.getWritableDatabase();

        // Delete all data
        db.delete(TABLE_INGREDIENTS, null, null);
        db.delete(TABLE_RECIPES, null, null);
        db.delete(TABLE_USERS, null, null);

        // Reinitialize default users
        initializeDefaultUsers(db);
    }

    /**
     * Get current timestamp
     */
    private String getCurrentTimestamp() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
    }

    /**
     * Get database statistics
     */
    public String getDatabaseStats() {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor userCursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_USERS, null);
        Cursor recipeCursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_RECIPES, null);
        Cursor ingredientCursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_INGREDIENTS, null);

        int userCount = 0, recipeCount = 0, ingredientCount = 0;

        if (userCursor.moveToFirst()) userCount = userCursor.getInt(0);
        if (recipeCursor.moveToFirst()) recipeCount = recipeCursor.getInt(0);
        if (ingredientCursor.moveToFirst()) ingredientCount = ingredientCursor.getInt(0);

        userCursor.close();
        recipeCursor.close();
        ingredientCursor.close();

        return "Users: " + userCount + ", Recipes: " + recipeCount + ", Ingredients: " + ingredientCount;
    }
}
