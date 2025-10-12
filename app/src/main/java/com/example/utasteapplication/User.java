package com.example.utasteapplication;
/**
 * Author: Othmane El Moutaouakkil
 *
 **/

import android.os.Build;

import java.time.LocalDateTime;

public abstract class User {
    protected String firstName;
    protected String lastName;
    protected String email;
    protected String password;
    protected final String createdAt;
    protected String modifiedAt;

    public User(String email, String password) {
        this.email = email;
        this.password = password;
        this.createdAt = getCurrentTimestamp();
        this.modifiedAt = this.createdAt;
    }

    public boolean authenticate(String inputPwd) {
        return this.password.equals(inputPwd);
    }

    public void changePassword(String newPwd) {
        this.password = newPwd;
        updateModifiedAt();
    }

    public void updateProfile(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        updateModifiedAt();
    }

    // Getter method for email
    public String getEmail() {
        return email;
    }

    protected void updateModifiedAt() {
        this.modifiedAt = getCurrentTimestamp();
    }

    private String getCurrentTimestamp() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return LocalDateTime.now().toString();
        }
        return "minimum API version required for this method is API 26, I think... --Othmane";
    }

    public abstract String getRole();
}