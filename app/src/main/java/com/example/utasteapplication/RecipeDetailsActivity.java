package com.example.utasteapplication;

/*
 * Author: Othmane El Moutaouakkil
 */

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.List;

public class RecipeDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);

        // Initialize UI
        TextView recipeNameTextView = findViewById(R.id.recipe_name_textview);
        TextView recipeDescriptionTextView = findViewById(R.id.recipe_description_textview);
        ImageView recipeImageView = findViewById(R.id.recipe_imageview);
        ListView ingredientsListView = findViewById(R.id.ingredients_listview);

        DatabaseHelper dbHelper = DatabaseHelper.getInstance(this);

        // Get recipe ID from intent
        int recipeId = getIntent().getIntExtra("RECIPE_ID", -1);
        if (recipeId == -1) {
            Toast.makeText(this, "Invalid recipe ID", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Load recipe
        Recipe recipe = dbHelper.getRecipeById(recipeId);
        if (recipe == null) {
            Toast.makeText(this, "Recipe not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Display recipe details
        recipeNameTextView.setText(recipe.getName());
        recipeDescriptionTextView.setText(recipe.getDescription());

        // Load image if available
        String imagePath = recipe.getImagePath();
        Log.d("RecipeDetails", "Recipe loaded - Name: " + recipe.getName());
        Log.d("RecipeDetails", "Image path from database: [" + imagePath + "]");
        Log.d("RecipeDetails", "Image path null? " + (imagePath == null));
        
        if (imagePath != null && !imagePath.isEmpty()) {
            boolean imageLoaded = false;
            
            // First try: Load as drawable resource (for local images)
            int imageResourceId = getResources().getIdentifier(
                imagePath, 
                "drawable", 
                getPackageName()
            );
            
            if (imageResourceId != 0) {
                // Found as drawable resource
                Log.d("RecipeDetails", "Found drawable resource: " + imageResourceId);
                recipeImageView.setImageResource(imageResourceId);
                imageLoaded = true;
            } else if (imagePath.startsWith("http://") || imagePath.startsWith("https://")) {
                Log.d("RecipeDetails", "Image is a URL, but URL loading not implemented");
                Toast.makeText(this, "URL images not supported yet. Use drawable resources.", Toast.LENGTH_SHORT).show();
                recipeImageView.setImageResource(R.drawable.ic_recipe_placeholder);
            } else if (imagePath.startsWith("content://") || imagePath.startsWith("file://")) {
                // Local URI
                try {
                    Log.d("RecipeDetails", "Loading from URI: " + imagePath);
                    recipeImageView.setImageURI(android.net.Uri.parse(imagePath));
                    imageLoaded = true;
                } catch (Exception e) {
                    Log.e("RecipeDetails", "Error loading URI: " + e.getMessage());
                    recipeImageView.setImageResource(R.drawable.ic_recipe_placeholder);
                }
            } else {
                // Try as file path
                try {
                    android.net.Uri uri = android.net.Uri.parse("file://" + imagePath);
                    recipeImageView.setImageURI(uri);
                    imageLoaded = true;
                } catch (Exception e) {
                    Log.e("RecipeDetails", "Error loading file path: " + e.getMessage());
                }
            }
            
            if (!imageLoaded) {
                // If nothing worked, use placeholder
                Log.d("RecipeDetails", "Using placeholder image");
                recipeImageView.setImageResource(R.drawable.ic_recipe_placeholder);
            }
        } else {
            Log.d("RecipeDetails", "No image path, using placeholder");
            recipeImageView.setImageResource(R.drawable.ic_recipe_placeholder);
        }

        // Load ingredients
        List<RecipeIngredient> ingredients = dbHelper.getIngredientsForRecipe(recipeId);
        ArrayAdapterIngredients adapter = new ArrayAdapterIngredients(this, ingredients);
        ingredientsListView.setAdapter(adapter);
    }
}
