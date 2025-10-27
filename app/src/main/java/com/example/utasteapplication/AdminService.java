package com.example.utasteapplication;

/*
 * Auteur : Sara Rigotti
 *
 * Classe AdminService – Fournit les fonctionnalités de gestion destinées à l’administrateur.
 *
 * Cette classe agit comme une couche de service entre l’interface et la base de données.
 * Elle permet à l’administrateur de :
 *   - Réinitialiser la base de données (suppression de certaines tables ou utilisateurs spécifiques),
 *   - Réinitialiser les mots de passe,
 *   - Mettre à jour le profil d’un utilisateur.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class AdminService {
    // ----- Accès à la base de données -----
    private DatabaseHelper dbHelper; // Aide à gérer la création et la manipulation de la base de données SQLite

    /**
     * Constructeur du service d’administration.
     * Initialise le gestionnaire de base de données pour permettre les opérations d’écriture et de mise à jour.
     *
     * @param context Le contexte de l’application, nécessaire pour accéder à la base de données.
     */
    public AdminService(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    /**
     * Réinitialise partiellement la base de données.
     * Supprime les utilisateurs ayant le rôle "Waiter", ainsi que toutes les recettes et ingrédients existants.
     *
     * Cette méthode peut être utilisée, par exemple, avant un redéploiement ou pour nettoyer les données.
     */
    public void resetDatabase() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Supprime tous les utilisateurs de type "Waiter"
        db.delete("users", "role = ?", new String[]{"Waiter"});

        // Supprime toutes les recettes enregistrées
        db.delete("recipes", null, null);

        // Supprime tous les ingrédients enregistrés
        db.delete("ingredients", null, null);

        // Si la table "sales" était implémentée, elle serait vidée ici également
    }

    /**
     * Réinitialise le mot de passe d’un utilisateur donné.
     * Met à jour la valeur du champ "password" dans la table des utilisateurs.
     *
     * @param email      L’adresse courriel de l’utilisateur dont le mot de passe doit être réinitialisé.
     * @param defaultPwd Le nouveau mot de passe par défaut à appliquer.
     */
    public void resetPassword(String email, String defaultPwd) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Prépare les nouvelles valeurs à insérer dans la base
        ContentValues values = new ContentValues();
        values.put("password", defaultPwd);

        // Met à jour le mot de passe pour l’utilisateur correspondant à l’email donné
        db.update("users", values, "email = ?", new String[]{email});
    }

    /**
     * Met à jour le profil d’un utilisateur (nom et prénom).
     * Cette méthode est utile lorsque l’administrateur souhaite corriger ou modifier les informations d’un compte.
     *
     * @param email     L’adresse courriel de l’utilisateur à mettre à jour.
     * @param firstName Le nouveau prénom de l’utilisateur.
     * @param lastName  Le nouveau nom de famille de l’utilisateur.
     */
    public void updateUserProfile(String email, String firstName, String lastName) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Prépare les nouvelles valeurs à enregistrer
        ContentValues values = new ContentValues();
        values.put("firstName", firstName);
        values.put("lastName", lastName);

        // Applique les modifications à l’utilisateur correspondant
        db.update("users", values, "email = ?", new String[]{email});
    }
}