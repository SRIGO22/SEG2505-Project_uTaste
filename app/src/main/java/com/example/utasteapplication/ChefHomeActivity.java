package com.example.utasteapplication;

/**
 * Author: Othmane El Moutaouakkil
 * Chef Home Activity
 * Updated to use SessionManager and integrate recipe management
 */

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Home activity for Chef
 * Shows options: Manage Recipes, Change Password, Logout
 */
public class ChefHomeActivity extends AppCompatActivity {

    // UI components
    private TextView welcomeText;
    private Button manageRecipesButton;
    private Button manageIngredientsButton;
    private Button changePasswordButton;
    private Button logoutButton;

    // Session manager to track logged-in user
    private SessionManager sessionManager;
    private String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chef_home);

        // Initialize session manager
        sessionManager = SessionManager.getInstance();

        // Check if user is logged in
        if (!sessionManager.isLoggedIn()) {
            redirectToLogin();
            return;
        }

        // Get currently logged-in user email safely
        User currentUser = sessionManager.getCurrentUser();
        userEmail = currentUser != null ? currentUser.getEmail() : "";

        // Bind UI components
        welcomeText = findViewById(R.id.welcome_text);
        manageRecipesButton = findViewById(R.id.manage_recipes_button);
        manageIngredientsButton = findViewById(R.id.manage_ingredients_button);
        changePasswordButton = findViewById(R.id.change_password_button);
        logoutButton = findViewById(R.id.logout_button);

        // Set welcome message
        welcomeText.setText(getString(R.string.welcome_message, "Chef"));

        // Button listeners
        manageRecipesButton.setOnClickListener(v -> openManageRecipes());
        manageIngredientsButton.setOnClickListener(v -> openManageIngredients());
        changePasswordButton.setOnClickListener(v -> openChangePassword());
        logoutButton.setOnClickListener(v -> logout());
    }

    /**
     * Open ManageRecipesActivity
     */
    private void openManageRecipes() {
        Intent intent = new Intent(ChefHomeActivity.this, ManageRecipesActivity.class);
        startActivity(intent);
    }

    /**
     * Open ManageIngredientsActivity
     * Since ingredients are managed per recipe, navigate to ManageRecipes first
     */
    private void openManageIngredients() {
        // Navigate to Manage Recipes where user can select a recipe to manage ingredients
        Intent intent = new Intent(ChefHomeActivity.this, ManageRecipesActivity.class);
        startActivity(intent);
    }

    /**
     * Open ChangePasswordActivity
     */
    private void openChangePassword() {
        Intent intent = new Intent(ChefHomeActivity.this, ChangePasswordActivity.class);
        intent.putExtra("USER_EMAIL", userEmail);
        intent.putExtra("USER_ROLE", "Chef");
        startActivity(intent);
    }

    /**
     * Logout user and clear session
     */
    private void logout() {
        sessionManager.logout();
        redirectToLogin();
    }

    /**
     * Redirect to LoginActivity and clear activity stack
     */
    private void redirectToLogin() {
        Intent intent = new Intent(ChefHomeActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
