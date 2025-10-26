package com.example.utasteapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.utasteapplication.RecipeIngredient;

import java.util.List;

public class ArrayAdapterIngredients extends ArrayAdapter<RecipeIngredient> {

    public ArrayAdapterIngredients(Context context, List<RecipeIngredient> ingredients) {
        super(context, 0, ingredients);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        RecipeIngredient ingredient = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_list_item_2, parent, false);
        }

        TextView text1 = convertView.findViewById(android.R.id.text1);
        TextView text2 = convertView.findViewById(android.R.id.text2);

        text1.setText(ingredient.getName());
        text2.setText("QR: " + ingredient.getQrCode() + " | Qty: " + ingredient.getQuantityPercentage() + "%");

        return convertView;
    }
}
