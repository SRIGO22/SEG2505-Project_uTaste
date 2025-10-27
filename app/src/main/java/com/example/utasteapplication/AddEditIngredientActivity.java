package com.example.utasteapplication;

/*
 * Author: Othmane El Moutaouakkil
 * Updated: Unified Add/Edit Ingredient handling + QR code scanning
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

    private EditText editTextName, editTextQrCode, editTextQuantity;
    private Button buttonSave, buttonCancel, buttonScanQr;

    private DatabaseHelper dbHelper;
    private int recipeId;
    private int ingredientId = -1; // Default: adding new ingredient
    private RecipeIngredient ingredientToEdit;

    // QR code scanner launcher
    private final ActivityResultLauncher<ScanOptions> barcodeLauncher =
            registerForActivityResult(new ScanContract(), result -> {
                if(result.getContents() != null) {
                    editTextQrCode.setText(result.getContents());
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_ingredient);

        // Initialize UI
        editTextName = findViewById(R.id.editText_ingredient_name);
        editTextQrCode = findViewById(R.id.editText_ingredient_qrcode);
        editTextQuantity = findViewById(R.id.editText_ingredient_quantity);
        buttonSave = findViewById(R.id.button_save_ingredient);
        buttonCancel = findViewById(R.id.button_cancel_ingredient);
        buttonScanQr = findViewById(R.id.button_scan_qr); // new button for scanning

        dbHelper = DatabaseHelper.getInstance(this);

        // Get recipe ID and ingredient ID from intent
        recipeId = getIntent().getIntExtra("RECIPE_ID", -1);
        ingredientId = getIntent().getIntExtra("INGREDIENT_ID", -1);

        // If editing, load ingredient data
        if (ingredientId != -1) {
            ingredientToEdit = dbHelper.getIngredientById(ingredientId);
            if (ingredientToEdit != null) {
                editTextName.setText(ingredientToEdit.getName());
                editTextQrCode.setText(ingredientToEdit.getQrCode());
                editTextQuantity.setText(String.valueOf(ingredientToEdit.getQuantityPercentage()));
            }
        }

        // Save button listener
        buttonSave.setOnClickListener(v -> saveIngredient());

        // Cancel button listener
        buttonCancel.setOnClickListener(v -> finish());

        // QR code scanner button
        buttonScanQr.setOnClickListener(v -> {
            ScanOptions options = new ScanOptions();
            options.setPrompt("Scan the ingredient QR code");
            options.setBeepEnabled(true);
            options.setOrientationLocked(true);
            barcodeLauncher.launch(options);
        });
    }

    private void saveIngredient() {
        String name = editTextName.getText().toString().trim();
        String qrCode = editTextQrCode.getText().toString().trim();
        String quantityStr = editTextQuantity.getText().toString().trim();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(qrCode) || TextUtils.isEmpty(quantityStr)) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        double quantity;
        try {
            quantity = Double.parseDouble(quantityStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid quantity", Toast.LENGTH_SHORT).show();
            return;
        }

        if (quantity <= 0 || quantity > 100) {
            Toast.makeText(this, "Quantity must be between 0 and 100", Toast.LENGTH_SHORT).show();
            return;
        }

        if (ingredientId != -1 && ingredientToEdit != null) {
            // Update existing ingredient
            ingredientToEdit.setName(name);
            ingredientToEdit.setQrCode(qrCode);
            ingredientToEdit.setQuantityPercentage(quantity);

            boolean updated = dbHelper.updateIngredient(
                    ingredientToEdit.getId(),
                    ingredientToEdit.getQrCode(),
                    ingredientToEdit.getName(),
                    ingredientToEdit.getQuantityPercentage()
            );

            if (updated) {
                Toast.makeText(this, "Ingredient updated", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Failed to update ingredient", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Add new ingredient
            RecipeIngredient newIngredient = new RecipeIngredient(recipeId, qrCode, name, quantity);
            long result = dbHelper.addIngredient(newIngredient);

            if (result != -1) {
                Toast.makeText(this, "Ingredient added", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Failed to add ingredient", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
