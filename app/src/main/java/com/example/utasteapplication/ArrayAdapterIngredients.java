package com.example.utasteapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.utasteapplication.RecipeIngredient;

import java.util.List;

/**
 * Classe ArrayAdapterIngredients
 * Cette classe permet d’adapter une liste d’ingrédients (RecipeIngredient)
 * afin qu’ils puissent être affichés dans une vue de type ListView.
 */
public class ArrayAdapterIngredients extends ArrayAdapter<RecipeIngredient> {

    /**
     * Constructeur de l’adaptateur
     * @param context  le contexte de l’application (activité ou fragment)
     * @param ingredients  la liste des ingrédients à afficher
     */
    public ArrayAdapterIngredients(Context context, List<RecipeIngredient> ingredients) {
        super(context, 0, ingredients);  // Appel au constructeur de la classe parent ArrayAdapter
    }

    /**
     * Méthode appelée pour créer ou réutiliser une vue (ligne) dans la liste
     * @param position   position de l’élément actuel dans la liste
     * @param convertView   vue existante réutilisable (si non nulle)
     * @param parent   le conteneur parent de la vue
     * @return la vue finale contenant les données de l’ingrédient
     */
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