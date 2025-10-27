package com.example.utasteapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.utasteapplication.RecipeIngredient;

import java.util.List;

// Adaptateur qui permet d’afficher une liste d’ingrédients (RecipeIngredient)
// dans une ListView de façon simple et lisible
public class ArrayAdapterIngredients extends ArrayAdapter<RecipeIngredient> {

    // Constructeur : reçoit le contexte et la liste d’ingrédients à afficher
    public ArrayAdapterIngredients(Context context, List<RecipeIngredient> ingredients) {
        // Appelle le constructeur de la classe parent (ArrayAdapter)
        super(context, 0, ingredients);
    }

    // Cette méthode est appelée à chaque fois qu’une ligne doit être affichée dans la liste.
    // - position : position de l’ingrédient dans la liste
    // - convertView : vue réutilisable (si elle existe déjà)
    // - parent : la vue parente (le conteneur de la liste)
    // Retourne la vue finale avec les infos de l’ingrédient
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Récupère l’ingrédient à afficher à cette position
        RecipeIngredient ingredient = getItem(position);

        // Si la vue n’existe pas encore, on en crée une nouvelle à partir du layout Android par défaut
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(android.R.layout.simple_list_item_2, parent, false);
        }

        // Référence aux deux zones de texte du layout simple_list_item_2
        TextView text1 = convertView.findViewById(android.R.id.text1);
        TextView text2 = convertView.findViewById(android.R.id.text2);

        // Affecte le nom de l’ingrédient dans le premier champ
        text1.setText(ingredient.getName());

        // Affiche le code QR et le pourcentage de quantité dans le second champ
        text2.setText("QR: " + ingredient.getQrCode() + " | Qty: " + ingredient.getQuantityPercentage() + "%");

        // Retourne la vue complète prête à être affichée dans la liste
        return convertView;
    }
}