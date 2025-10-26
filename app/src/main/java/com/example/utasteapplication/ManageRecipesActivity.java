package com.example.utasteapplication;


/*
 * Author: Othmane El Moutaouakkil
 */


/**
 * ManageRecipesActivity - Chef can view, add, edit, delete recipes
 */

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import java.util.List;

public class ManageRecipesActivity extends AppCompatActivity {

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

        // Initialize UI components
        recipesListView = findViewById(R.id.recipes_list_view);
        addRecipeButton = findViewById(R.id.add_recipe_button);
        backButton = findViewById(R.id.back_button);

        // Load recipes
        loadRecipes();

        // Set button listeners
        addRecipeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddRecipe();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Set list item click listener
        recipesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Recipe selectedRecipe = recipes.get(position);
                showRecipeOptions(selectedRecipe);
            }
        });
    }

    private void loadRecipes() {
        recipes = dbHelper.getAllRecipes();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, recipes);
        recipesListView.setAdapter(adapter);
    }

    private void openAddRecipe() {
        Intent intent = new Intent(ManageRecipesActivity.this, AddEditRecipeActivity.class);
        startActivity(intent);
    }

    private void showRecipeOptions(Recipe recipe) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(recipe.getName());
        builder.setItems(new String[]{"View Details", "Edit Recipe", "Manage Ingredients", "Delete Recipe"},
                (dialog, which) -> {
                    switch (which) {
                        case 0:
                            viewRecipeDetails(recipe);
                            break;
                        case 1:
                            editRecipe(recipe);
                            break;
                        case 2:
                            manageIngredients(recipe);
                            break;
                        case 3:
                            confirmDeleteRecipe(recipe);
                            break;
                    }
                });
        builder.show();
    }

    private void viewRecipeDetails(Recipe recipe) {
        Intent intent = new Intent(ManageRecipesActivity.this, RecipeDetailsActivity.class);
        intent.putExtra("RECIPE_ID", recipe.getId());
        startActivity(intent);
    }

    private void editRecipe(Recipe recipe) {
        Intent intent = new Intent(ManageRecipesActivity.this, AddEditRecipeActivity.class);
        intent.putExtra("RECIPE_ID", recipe.getId());
        startActivity(intent);
    }

    private void manageIngredients(Recipe recipe) {
        Intent intent = new Intent(ManageRecipesActivity.this, ManageIngredientsActivity.class);
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