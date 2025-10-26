package com.example.utasteapplication;

/*
 * Author: Othmane El Moutaouakkil
 */

import android.os.Bundle;
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
        if (imagePath != null && !imagePath.isEmpty()) {
            recipeImageView.setImageURI(android.net.Uri.parse(imagePath));
        } else {
            recipeImageView.setImageResource(R.drawable.ic_recipe_placeholder); // default placeholder
        }

        // Load ingredients
        List<RecipeIngredient> ingredients = dbHelper.getIngredientsForRecipe(recipeId);
        ArrayAdapterIngredients adapter = new ArrayAdapterIngredients(this, ingredients);
        ingredientsListView.setAdapter(adapter);
    }
}
