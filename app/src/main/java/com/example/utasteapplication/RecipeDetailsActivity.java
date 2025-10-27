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

        // Relie les éléments du layout XML avec les variables du code
        TextView recipeNameTextView = findViewById(R.id.recipe_name_textview);
        TextView recipeDescriptionTextView = findViewById(R.id.recipe_description_textview);
        ImageView recipeImageView = findViewById(R.id.recipe_imageview);
        ListView ingredientsListView = findViewById(R.id.ingredients_listview);

        // Récupère l'accès à la base de données (via le helper en Singleton)
        DatabaseHelper dbHelper = DatabaseHelper.getInstance(this);

        // Récupère l'ID de la recette envoyé depuis l'activité précédente
        int recipeId = getIntent().getIntExtra("RECIPE_ID", -1);
        if (recipeId == -1) {
            // Si aucun ID n’a été reçu, affiche un message d’erreur et ferme l’activité
            Toast.makeText(this, "Invalid recipe ID", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Récupère les informations de la recette à partir de la base de données
        Recipe recipe = dbHelper.getRecipeById(recipeId);
        if (recipe == null) {
            // Si la recette n’existe pas, avertit l’utilisateur et ferme la page
            Toast.makeText(this, "Recipe not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Affiche le nom et la description de la recette
        recipeNameTextView.setText(recipe.getName());
        recipeDescriptionTextView.setText(recipe.getDescription());

        // Affiche l’image de la recette si disponible, sinon une image par défaut
        String imagePath = recipe.getImagePath();
        if (imagePath != null && !imagePath.isEmpty()) {
            recipeImageView.setImageURI(android.net.Uri.parse(imagePath));
        } else {
            recipeImageView.setImageResource(R.drawable.ic_recipe_placeholder); // default placeholder
        }

        // Charge et affiche la liste des ingrédients liés à cette recette
        List<RecipeIngredient> ingredients = dbHelper.getIngredientsForRecipe(recipeId);
        ArrayAdapterIngredients adapter = new ArrayAdapterIngredients(this, ingredients);
        ingredientsListView.setAdapter(adapter);
    }
}
