package com.example.utasteapplication;

/*
 * Author: Othmane El Moutaouakkil
 * Updated: Prompt to add ingredients after adding a new recipe
 */

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import java.util.List;

public class ManageRecipesActivity extends AppCompatActivity {

    private static final int REQUEST_ADD_RECIPE = 1001;

    private ListView recipesListView;
    private Button addRecipeButton;
    private Button backButton;
    private DatabaseHelper dbHelper;
    private List<Recipe> recipes;
    private ArrayAdapter<Recipe> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_recipes);

        dbHelper = DatabaseHelper.getInstance(this);

        recipesListView = findViewById(R.id.recipes_list_view);
        addRecipeButton = findViewById(R.id.add_recipe_button);
        backButton = findViewById(R.id.back_button);

        loadRecipes();

        addRecipeButton.setOnClickListener(v -> openAddRecipe());
        backButton.setOnClickListener(v -> finish());

        recipesListView.setOnItemClickListener((parent, view, position, id) -> {
            Recipe selectedRecipe = recipes.get(position);
            showRecipeOptions(selectedRecipe);
        });
    }

    private void loadRecipes() {
        recipes = dbHelper.getAllRecipes();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, recipes);
        recipesListView.setAdapter(adapter);
    }

    private void openAddRecipe() {
        Intent intent = new Intent(this, AddEditRecipeActivity.class);
        startActivityForResult(intent, REQUEST_ADD_RECIPE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_ADD_RECIPE && resultCode == RESULT_OK && data != null) {
            int newRecipeId = data.getIntExtra("NEW_RECIPE_ID", -1);
            if (newRecipeId != -1) {
                Toast.makeText(this, "Recipe added! Now add ingredients.", Toast.LENGTH_SHORT).show();
                Recipe newRecipe = dbHelper.getRecipeById(newRecipeId);
                if (newRecipe != null) {
                    Intent intent = new Intent(this, ManageIngredientsActivity.class);
                    intent.putExtra("RECIPE_ID", newRecipe.getId());
                    intent.putExtra("RECIPE_NAME", newRecipe.getName());
                    startActivity(intent);
                }
            }
        }

        // Reload the list to include new or updated recipes
        loadRecipes();
    }

    private void showRecipeOptions(Recipe recipe) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(recipe.getName());
        builder.setItems(new String[]{"View Details", "Edit Recipe", "Manage Ingredients", "Delete Recipe"}, (dialog, which) -> {
            switch (which) {
                case 0: viewRecipeDetails(recipe); break;
                case 1: editRecipe(recipe); break;
                case 2: manageIngredients(recipe); break;
                case 3: confirmDeleteRecipe(recipe); break;
            }
        });
        builder.show();
    }

    private void viewRecipeDetails(Recipe recipe) {
        Intent intent = new Intent(this, RecipeDetailsActivity.class);
        intent.putExtra("RECIPE_ID", recipe.getId());
        startActivity(intent);
    }

    private void editRecipe(Recipe recipe) {
        Intent intent = new Intent(this, AddEditRecipeActivity.class);
        intent.putExtra("RECIPE_ID", recipe.getId());
        startActivityForResult(intent, REQUEST_ADD_RECIPE); // Optional: handle edits similarly
    }

    private void manageIngredients(Recipe recipe) {
        Intent intent = new Intent(this, ManageIngredientsActivity.class);
        intent.putExtra("RECIPE_ID", recipe.getId());
        intent.putExtra("RECIPE_NAME", recipe.getName());
        startActivity(intent);
    }

    private void confirmDeleteRecipe(Recipe recipe) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Recipe");
        builder.setMessage("Are you sure you want to delete '" + recipe.getName() + "'?");
        builder.setPositiveButton("Delete", (dialog, which) -> {
            if (dbHelper.deleteRecipe(recipe.getId())) {
                Toast.makeText(this, "Recipe deleted successfully", Toast.LENGTH_SHORT).show();
                loadRecipes();
            } else {
                Toast.makeText(this, "Failed to delete recipe", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadRecipes();
    }
}
