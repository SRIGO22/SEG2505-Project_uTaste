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


public class ChefHomeActivity extends AppCompatActivity {

    // Éléments de l’interface utilisateur
    private TextView welcomeText;
    private Button manageRecipesButton;
    private Button manageIngredientsButton;
    private Button changePasswordButton;
    private Button logoutButton;

    // Gestionnaire de session pour savoir quel utilisateur est connecté
    private SessionManager sessionManager;
    private String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chef_home);

        // Initialise le gestionnaire de session
        sessionManager = SessionManager.getInstance();

        // Si aucun utilisateur n’est connecté, on retourne à l’écran de connexion
        if (!sessionManager.isLoggedIn()) {
            redirectToLogin();
            return;
        }

        // Récupère l’utilisateur actuellement connecté (s’il existe)
        User currentUser = sessionManager.getCurrentUser();
        userEmail = currentUser != null ? currentUser.getEmail() : "";

        // Lie les composants de l’interface aux éléments du layout XML
        welcomeText = findViewById(R.id.welcome_text);
        manageRecipesButton = findViewById(R.id.manage_recipes_button);
        manageIngredientsButton = findViewById(R.id.manage_ingredients_button);
        changePasswordButton = findViewById(R.id.change_password_button);
        logoutButton = findViewById(R.id.logout_button);

        // Message de bienvenue personnalisé
        welcomeText.setText(getString(R.string.welcome_message, "Chef"));

        // Actions des boutons
        manageRecipesButton.setOnClickListener(v -> openManageRecipes());
        manageIngredientsButton.setOnClickListener(v -> openManageIngredients());
        changePasswordButton.setOnClickListener(v -> openChangePassword());
        logoutButton.setOnClickListener(v -> logout());
    }

    // Ouvre l’écran de gestion des recettes
    private void openManageRecipes() {
        Intent intent = new Intent(ChefHomeActivity.this, ManageRecipesActivity.class);
        startActivity(intent);
    }

    // Ouvre la gestion des ingrédients
    // Le chef doit d’abord choisir la recette à modifier
    private void openManageIngredients() {
        DatabaseHelper db = DatabaseHelper.getInstance(this);
        List<Recipe> recipes = db.getAllRecipes();

        // Si aucune recette n’existe encore, on avertit le chef
        if (recipes.isEmpty()) {
            new AlertDialog.Builder(this)
                    .setTitle("No Recipes")
                    .setMessage("Please create a recipe first before managing ingredients.")
                    .setPositiveButton("OK", null)
                    .show();
            return;
        }

        // On prépare une liste de noms de recettes à afficher dans le dialogue
        String[] recipeNames = new String[recipes.size()];
        for (int i = 0; i < recipes.size(); i++) {
            recipeNames[i] = recipes.get(i).getName();
        }
        // Dialogue permettant de choisir une recette
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

    // Ouvre la page de changement de mot de passe
    private void openChangePassword() {
        Intent intent = new Intent(ChefHomeActivity.this, ChangePasswordActivity.class);
        intent.putExtra("USER_EMAIL", userEmail);
        intent.putExtra("USER_ROLE", "Chef");
        startActivity(intent);
    }

    // Déconnecte l’utilisateur et supprime sa session
    private void logout() {
        sessionManager.logout();
        redirectToLogin();
    }

    // Redirige vers la page de connexion et vide la pile d’activités
    private void redirectToLogin() {
        Intent intent = new Intent(ChefHomeActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
