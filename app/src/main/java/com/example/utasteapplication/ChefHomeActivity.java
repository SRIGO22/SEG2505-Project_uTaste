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
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.example.utasteapplication.DatabaseHelper;
import com.example.utasteapplication.Recipe;
import java.util.List;

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
     * Shows a dialog to select a recipe first, since ingredients are managed per recipe
     */
    private void openManageIngredients() {
        DatabaseHelper db = DatabaseHelper.getInstance(this);
        List<Recipe> recipes = db.getAllRecipes();
        
        if (recipes.isEmpty()) {
            // No recipes available
            new AlertDialog.Builder(this)
                    .setTitle("No Recipes")
                    .setMessage("Please create a recipe first before managing ingredients.")
                    .setPositiveButton("OK", null)
                    .show();
            return;
        }
        
        // Create a dialog to select a recipe
        String[] recipeNames = new String[recipes.size()];
        for (int i = 0; i < recipes.size(); i++) {
            recipeNames[i] = recipes.get(i).getName();
        }
        
        new AlertDialog.Builder(this)
                .setTitle("Select Recipe")
                .setItems(recipeNames, (dialog, which) -> {
                    Recipe selectedRecipe = recipes.get(which);
                    Intent intent = new Intent(ChefHomeActivity.this, ManageIngredientsActivity.class);
                    intent.putExtra("RECIPE_ID", selectedRecipe.getId());
                    intent.putExtra("RECIPE_NAME", selectedRecipe.getName());
                    startActivity(intent);
                })
                .setNegativeButton("Cancel", null)
                .show();
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
