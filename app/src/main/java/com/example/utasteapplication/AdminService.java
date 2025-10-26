package com.example.utasteapplication;/*
* Author: Sara Rigotti
*/

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class AdminService {
    private DatabaseHelper dbHelper;

    public AdminService(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void resetDatabase() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete("users", "role = ?", new String[]{"Waiter"});
        db.delete("recipes", null, null);
        db.delete("ingredients", null, null);
        // Sales table would be cleared here too if implemented
    }

    public void resetPassword(String email, String defaultPwd) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("password", defaultPwd);
        db.update("users", values, "email = ?", new String[]{email});
    }

    public void updateUserProfile(String email, String firstName, String lastName) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("firstName", firstName);
        values.put("lastName", lastName);
        db.update("users", values, "email = ?", new String[]{email});
    }
}
