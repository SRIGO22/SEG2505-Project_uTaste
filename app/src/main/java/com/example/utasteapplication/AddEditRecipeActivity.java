package com.example.utasteapplication;

/*
 * Author: Othmane El Moutaouakkil
 * Updated: Return new recipe ID to ManageRecipesActivity
 */

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddEditRecipeActivity extends AppCompatActivity {

    private EditText nameEditText;
    private EditText descriptionEditText;
    private EditText imagePathEditText;
    private Button saveButton;
    private Button cancelButton;

    private DatabaseHelper dbHelper;
    private Recipe currentRecipe;
    private int recipeId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_recipe);

        dbHelper = DatabaseHelper.getInstance(this);

        nameEditText = findViewById(R.id.editText_recipe_name);
        descriptionEditText = findViewById(R.id.editText_recipe_description);
        imagePathEditText = findViewById(R.id.editText_recipe_image);
        saveButton = findViewById(R.id.button_save_recipe);
        cancelButton = findViewById(R.id.button_cancel);

        // Check if editing existing recipe
        if (getIntent() != null && getIntent().hasExtra("RECIPE_ID")) {
            recipeId = getIntent().getIntExtra("RECIPE_ID", -1);
            loadRecipe(recipeId);
        }

        saveButton.setOnClickListener(v -> saveRecipe());
        cancelButton.setOnClickListener(v -> finish());
    }

    private void loadRecipe(int id) {
        currentRecipe = dbHelper.getRecipeById(id);
        if (currentRecipe != null) {
            nameEditText.setText(currentRecipe.getName());
            descriptionEditText.setText(currentRecipe.getDescription());
            imagePathEditText.setText(currentRecipe.getImagePath());
        }
    }

    private void saveRecipe() {
        String name = nameEditText.getText().toString().trim();
        String description = descriptionEditText.getText().toString().trim();
        String imagePath = imagePathEditText.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "Please enter a recipe name", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(description)) {
            Toast.makeText(this, "Please enter a description", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(imagePath)) {
            Toast.makeText(this, "Please enter image path", Toast.LENGTH_SHORT).show();
            return;
        }

        if (recipeId > 0 && currentRecipe != null) {
            // Update existing recipe
            boolean success = dbHelper.updateRecipe(
                    currentRecipe.getId(),
                    name,
                    imagePath,
                    description
            );
            if (success) {
                Toast.makeText(this, "Recipe updated successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Failed to update recipe", Toast.LENGTH_SHORT).show();
            }
            finish(); // no ingredients prompt for updates
        } else {
            // Create new recipe
            Recipe newRecipe = new Recipe(name, imagePath, description);
            long newId = dbHelper.addRecipe(newRecipe);
            if (newId != -1) {
                Toast.makeText(this, "Recipe added successfully", Toast.LENGTH_SHORT).show();

                // Return new recipe ID to ManageRecipesActivity
                Intent resultIntent = new Intent();
                resultIntent.putExtra("NEW_RECIPE_ID", (int) newId); // cast long to int safely
                setResult(RESULT_OK, resultIntent);
            } else {
                Toast.makeText(this, "Failed to add recipe", Toast.LENGTH_SHORT).show();
            }
            finish();
        }
    }

}

