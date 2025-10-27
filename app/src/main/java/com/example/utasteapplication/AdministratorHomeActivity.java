package com.example.utasteapplication;

/**
 * Author: Othmane El Moutaouakkil
 * Administrator Home Activity
 * Updated to include recipe management and database reset
 */

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AdministratorHomeActivity extends AppCompatActivity {

    private TextView welcomeText;
    private Button manageWaitersButton;
    private Button changePasswordButton;
    private Button addRecipeButton;
    private Button manageRecipesButton;
    private Button resetNonDefaultButton;
    private Button logoutButton;

    private String userEmail;
    private SessionManager sessionManager;
    private DatabaseHelper db; // database instance

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administrator_home);

        sessionManager = SessionManager.getInstance();
        db = DatabaseHelper.getInstance(this);

        userEmail = getIntent().getStringExtra("USER_EMAIL");

        welcomeText = findViewById(R.id.welcome_text);
        manageWaitersButton = findViewById(R.id.manage_waiters_button);
        changePasswordButton = findViewById(R.id.change_password_button);
        addRecipeButton = findViewById(R.id.button_add_recipe_admin);
        manageRecipesButton = findViewById(R.id.button_manage_recipes_admin);
        resetNonDefaultButton = findViewById(R.id.button_remove_non_default);
        logoutButton = findViewById(R.id.logout_button);

        welcomeText.setText("Welcome, Administrator!");

        // Open Waiter management
        manageWaitersButton.setOnClickListener(v -> openManageWaiters());

        // Change password
        changePasswordButton.setOnClickListener(v -> openChangePassword());

        // Admin adds recipes (same screen Chef uses)
        addRecipeButton.setOnClickListener(v -> openAddRecipe());

        // Admin manages recipes (delete non-default)
        manageRecipesButton.setOnClickListener(v -> openManageRecipes());

        // Reset DB (delete non-default recipes + ingredients)
        resetNonDefaultButton.setOnClickListener(v -> resetDatabaseContent());

        // Logout
        logoutButton.setOnClickListener(v -> logout());
    }

    private void openManageWaiters() {
        Intent intent = new Intent(this, ManageWaitersActivity.class);
        intent.putExtra("USER_EMAIL", userEmail);
        startActivity(intent);
    }

    private void openChangePassword() {
        Intent intent = new Intent(this, ChangePasswordActivity.class);
        intent.putExtra("USER_EMAIL", userEmail);
        intent.putExtra("USER_ROLE", "Administrator");
        startActivity(intent);
    }

    private void openAddRecipe() {
        Intent intent = new Intent(this, AddEditRecipeActivity.class);
        startActivity(intent);
    }

    private void openManageRecipes() {
        Intent intent = new Intent(this, ManageRecipesActivity.class);
        startActivity(intent);
    }

    private void resetDatabaseContent() {
        db.deleteNonDefaultContent();
        Toast.makeText(this, "Non-default recipes & ingredients removed!", Toast.LENGTH_SHORT).show();
    }

    private void logout() {
        sessionManager.logout();

        Intent intent = new Intent(AdministratorHomeActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

        finish();
    }
}
