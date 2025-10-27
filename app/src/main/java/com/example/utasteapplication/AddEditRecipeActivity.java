package com.example.utasteapplication;

/*
 * Auteur : Othmane El Moutaouakkil
 * Description : Cette activité permet à l’utilisateur d’ajouter une nouvelle recette
 *               ou de modifier une recette existante. Une fois enregistrée,
 *               l’activité retourne l’ID de la recette à ManageRecipesActivity.
 * Dernière mise à jour : Retour de l’ID de la nouvelle recette à ManageRecipesActivity
 */

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddEditRecipeActivity extends AppCompatActivity {

    // Éléments de l’interface utilisateur
    private EditText nomEditText;
    private EditText descriptionEditText;
    private EditText cheminImageEditText;
    private Button boutonSauvegarder;
    private Button boutonAnnuler;

    private DatabaseHelper dbHelper;
    private Recipe recetteActuelle;
    private int recetteId = -1; // Par défaut : ajout d’une nouvelle recette

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_recipe);

        dbHelper = DatabaseHelper.getInstance(this);

        // --- Initialisation des composants de l’interface ---
        nomEditText = findViewById(R.id.editText_recipe_name);
        descriptionEditText = findViewById(R.id.editText_recipe_description);
        cheminImageEditText = findViewById(R.id.editText_recipe_image);
        boutonSauvegarder = findViewById(R.id.button_save_recipe);
        boutonAnnuler = findViewById(R.id.button_cancel);

        // Vérifie si l’utilisateur modifie une recette existante
        if (getIntent() != null && getIntent().hasExtra("RECIPE_ID")) {
            recetteId = getIntent().getIntExtra("RECIPE_ID", -1);
            chargerRecette(recetteId);
        }

        // Écouteurs des boutons
        boutonSauvegarder.setOnClickListener(v -> sauvegarderRecette());
        boutonAnnuler.setOnClickListener(v -> finish());
    }

    // Charge les informations d’une recette existante dans les champs du formulaire
    private void chargerRecette(int id) {
        recetteActuelle = dbHelper.getRecipeById(id);
        if (recetteActuelle != null) {
            nomEditText.setText(recetteActuelle.getName());
            descriptionEditText.setText(recetteActuelle.getDescription());
            cheminImageEditText.setText(recetteActuelle.getImagePath());
        }
    }

    // Vérifie les champs et effectue l’ajout ou la mise à jour de la recette
    // Retourne l’ID de la recette à l’activité précédente
    private void sauvegarderRecette() {
        String nom = nomEditText.getText().toString().trim();
        String description = descriptionEditText.getText().toString().trim();
        String cheminImage = cheminImageEditText.getText().toString().trim();

        // --- Vérification des champs obligatoires ---
        if (TextUtils.isEmpty(nom)) {
            Toast.makeText(this, "Veuillez entrer un nom de recette.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(description)) {
            Toast.makeText(this, "Veuillez entrer une description.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(cheminImage)) {
            Toast.makeText(this, "Veuillez entrer le chemin de l’image.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Si on modifie une recette existante
        if (recetteId > 0 && recetteActuelle != null) {
            boolean miseAJour = dbHelper.updateRecipe(
                    recetteActuelle.getId(),
                    nom,
                    cheminImage,
                    description
            );

            if (miseAJour) {
                Toast.makeText(this, "Recette mise à jour avec succès.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Échec de la mise à jour de la recette.", Toast.LENGTH_SHORT).show();
            }

            // Retour à la page précédente après mise à jour
            finish();

            // Sinon, on ajoute une nouvelle recette
        } else {
            Recipe nouvelleRecette = new Recipe(nom, cheminImage, description);
            long nouvelId = dbHelper.addRecipe(nouvelleRecette);

            if (nouvelId != -1) {
                Toast.makeText(this, "Recette ajoutée avec succès.", Toast.LENGTH_SHORT).show();

                // Renvoie l’ID de la nouvelle recette à ManageRecipesActivity
                Intent resultIntent = new Intent();
                resultIntent.putExtra("NEW_RECIPE_ID", (int) nouvelId); // Conversion sûre long → int
                setResult(RESULT_OK, resultIntent);
            } else {
                Toast.makeText(this, "Échec de l’ajout de la recette.", Toast.LENGTH_SHORT).show();
            }

            finish();
        }
    }
}