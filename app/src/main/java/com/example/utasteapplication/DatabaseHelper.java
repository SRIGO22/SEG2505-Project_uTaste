/*
* Author: Sara Rigotti
* Purpose: to manage database creation and versioning
*/

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "uTaste.db";
    public static final int DB_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createUsers = "CREATE TABLE users (" +
                "email TEXT PRIMARY KEY, " +
                "firstName TEXT, " +
                "lastName TEXT, " +
                "password TEXT, " +
                "role TEXT, " +
                "createdAt TEXT, " +
                "modifiedAt TEXT)";
        db.execSQL(createUsers);

        String createRecipes = "CREATE TABLE recipes (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT UNIQUE, " +
                "imagePath TEXT, " +
                "description TEXT)";
        db.execSQL(createRecipes);

        String createIngredients = "CREATE TABLE ingredients (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "recipeId INTEGER, " +
                "title TEXT, " +
                "qrCode TEXT, " +
                "quantityPercent REAL, " +
                "FOREIGN KEY(recipeId) REFERENCES recipes(id))";
        db.execSQL(createIngredients);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Handle schema changes
    }
}
