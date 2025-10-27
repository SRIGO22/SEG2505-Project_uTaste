package com.example.utasteapplication;

/*
 * Auteur : Othmane El Moutaouakkil
 * Description : Cette activité permet à l’utilisateur d’ajouter un nouvel ingrédient
 *               ou de modifier un ingrédient existant. Elle inclut la possibilité
 *               de scanner un code QR pour identifier rapidement un ingrédient.
 * Dernière mise à jour : Unification du mode Ajout/Édition + ajout du scan QR
 */

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

public class AddEditIngredientActivity extends AppCompatActivity {

    // Éléments de l’interface utilisateur
    private EditText nomInput, qrCodeInput, quantiteInput;
    private Button boutonSauvegarder, boutonAnnuler, boutonScannerQr;

    private DatabaseHelper dbHelper;
    private int recetteId;
    private int ingredientId = -1; // Par défaut : on ajoute un ingrédient
    private RecipeIngredient ingredientAModifier;

    // Lanceur du scanner de code QR et gestion du résultat
    private final ActivityResultLauncher<ScanOptions> barcodeLauncher =
            registerForActivityResult(new ScanContract(), result -> {
                if (result.getContents() != null) {
                    qrCodeInput.setText(result.getContents());
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_ingredient);

        // --- Initialisation des éléments de l’interface ---
        nomInput = findViewById(R.id.editText_ingredient_name);
        qrCodeInput = findViewById(R.id.editText_ingredient_qrcode);
        quantiteInput = findViewById(R.id.editText_ingredient_quantity);
        boutonSauvegarder = findViewById(R.id.button_save_ingredient);
        boutonAnnuler = findViewById(R.id.button_cancel_ingredient);
        boutonScannerQr = findViewById(R.id.button_scan_qr);

        dbHelper = DatabaseHelper.getInstance(this);

        // Récupération des identifiants transmis depuis l’activité précédente
        recetteId = getIntent().getIntExtra("RECIPE_ID", -1);
        ingredientId = getIntent().getIntExtra("INGREDIENT_ID", -1);

        // Si on modifie un ingrédient existant, on pré-remplit les champs
        if (ingredientId != -1) {
            ingredientAModifier = dbHelper.getIngredientById(ingredientId);
            if (ingredientAModifier != null) {
                nomInput.setText(ingredientAModifier.getName());
                qrCodeInput.setText(ingredientAModifier.getQrCode());
                quantiteInput.setText(String.valueOf(ingredientAModifier.getQuantityPercentage()));
            }
        }

        // Écouteurs des boutons
        boutonSauvegarder.setOnClickListener(v -> sauvegarderIngredient());
        boutonAnnuler.setOnClickListener(v -> finish());

        // Activation du scanner QR
        boutonScannerQr.setOnClickListener(v -> {
            ScanOptions options = new ScanOptions();
            options.setPrompt("Scannez le code QR de l’ingrédient");
            options.setBeepEnabled(true);
            options.setOrientationLocked(true);
            barcodeLauncher.launch(options);
        });
    }

    /**
     * Gère à la fois l’ajout et la modification d’un ingrédient.
     * Vérifie la validité des champs avant de sauvegarder dans la base de données.
     */
    private void sauvegarderIngredient() {
        String nom = nomInput.getText().toString().trim();
        String qrCode = qrCodeInput.getText().toString().trim();
        String quantiteStr = quantiteInput.getText().toString().trim();

        // Vérification des champs vides
        if (TextUtils.isEmpty(nom) || TextUtils.isEmpty(qrCode) || TextUtils.isEmpty(quantiteStr)) {
            Toast.makeText(this, "Veuillez remplir tous les champs.", Toast.LENGTH_SHORT).show();
            return;
        }

        double quantite;
        try {
            quantite = Double.parseDouble(quantiteStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Veuillez entrer une quantité valide.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (quantite <= 0 || quantite > 100) {
            Toast.makeText(this, "La quantité doit être comprise entre 0 et 100.", Toast.LENGTH_SHORT).show();
            return;
        }

        // --- Mode Édition ---
        if (ingredientId != -1 && ingredientAModifier != null) {
            ingredientAModifier.setName(nom);
            ingredientAModifier.setQrCode(qrCode);
            ingredientAModifier.setQuantityPercentage(quantite);

            boolean misAJour = dbHelper.updateIngredient(
                    ingredientAModifier.getId(),
                    ingredientAModifier.getQrCode(),
                    ingredientAModifier.getName(),
                    ingredientAModifier.getQuantityPercentage()
            );

            if (misAJour) {
                Toast.makeText(this, "Ingrédient mis à jour avec succès.", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Échec de la mise à jour de l’ingrédient.", Toast.LENGTH_SHORT).show();
            }

            // --- Mode Ajout ---
        } else {
            RecipeIngredient nouvelIngredient = new RecipeIngredient(recetteId, qrCode, nom, quantite);
            long resultat = dbHelper.addIngredient(nouvelIngredient);

            if (resultat != -1) {
                Toast.makeText(this, "Ingrédient ajouté avec succès.", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Échec de l’ajout de l’ingrédient.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}