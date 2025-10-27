package com.example.utasteapplication;

/*
 * Author: Othmane El Moutaouakkil
 * Updated: Fixed Intent key mismatch, added header and back button
 */

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ManageIngredientsActivity extends AppCompatActivity {

    private DatabaseHelper db;
    private ListView listIngredients;
    private Button btnAddIngredient;
    private Button btnBack;
    private TextView tvRecipeName;

    private ArrayList<RecipeIngredient> ingredientList;
    private ArrayAdapter<String> adapter;

    // Recipe info
    private int recipeId;
    private String recipeName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_ingredients);

        db = DatabaseHelper.getInstance(this);

        listIngredients = findViewById(R.id.listIngredients);
        btnAddIngredient = findViewById(R.id.btnAddIngredient);
        btnBack = findViewById(R.id.btnBack);
        tvRecipeName = findViewById(R.id.manage_ingredients_title);

        // Receive recipe info
        recipeId = getIntent().getIntExtra("RECIPE_ID", -1);
        recipeName = getIntent().getStringExtra("RECIPE_NAME");

        if (recipeId == -1) {
            Toast.makeText(this, "Error: Recipe not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        tvRecipeName.setText("Ingredients for: " + recipeName);

        loadIngredients();

        btnAddIngredient.setOnClickListener(view -> {
            Intent i = new Intent(ManageIngredientsActivity.this, AddEditIngredientActivity.class);
            i.putExtra("RECIPE_ID", recipeId);
            i.putExtra("RECIPE_NAME", recipeName);
            startActivity(i);
        });

        btnBack.setOnClickListener(v -> finish());

        listIngredients.setOnItemClickListener((adapterView, view, position, id) -> {
            RecipeIngredient selected = ingredientList.get(position);
            showIngredientOptions(selected);
        });
    }

    private void loadIngredients() {
        ingredientList = (ArrayList<RecipeIngredient>) db.getIngredientsForRecipe(recipeId);

        ArrayList<String> texts = new ArrayList<>();
        for (RecipeIngredient ing : ingredientList) {
            texts.add(ing.getName() + " - " + ing.getQuantityPercentage() + "%");
        }

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, texts);
        listIngredients.setAdapter(adapter);
    }

    private void showIngredientOptions(RecipeIngredient ingredient) {
        final String[] options = {"Modifier quantité", "Supprimer"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(ingredient.getName())
                .setItems(options, (dialogInterface, which) -> {
                    if (which == 0) editQuantity(ingredient);
                    else if (which == 1) deleteIngredient(ingredient);
                }).show();
    }

    private void editQuantity(RecipeIngredient ingredient) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Changer la quantité (%)");

        View customView = getLayoutInflater().inflate(R.layout.dialog_edit_quantity, null);
        android.widget.EditText editQty = customView.findViewById(R.id.editQuantity);
        editQty.setText(String.valueOf(ingredient.getQuantityPercentage()));
        builder.setView(customView);

        builder.setPositiveButton("OK", (dialog, which) -> {
            try {
                double qty = Double.parseDouble(editQty.getText().toString());
                if (qty < 0 || qty > 100) {
                    Toast.makeText(this, "Pourcentage invalide", Toast.LENGTH_SHORT).show();
                    return;
                }
                db.updateIngredientQuantity(ingredient.getId(), qty);
                Toast.makeText(this, "Quantité mise à jour", Toast.LENGTH_SHORT).show();
                loadIngredients();
            } catch (Exception e) {
                Toast.makeText(this, "Erreur", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Annuler", null);
        builder.show();
    }

    private void deleteIngredient(RecipeIngredient ingredient) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Supprimer l'ingrédient ?")
                .setMessage("Voulez-vous vraiment supprimer cet ingrédient ?")
                .setPositiveButton("Oui", (dialog, which) -> {
                    db.deleteIngredient(ingredient.getId());
                    Toast.makeText(this, "Supprimé", Toast.LENGTH_SHORT).show();
                    loadIngredients();
                })
                .setNegativeButton("Non", null)
                .show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadIngredients();
    }
}
