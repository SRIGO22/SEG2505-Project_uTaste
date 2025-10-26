package com.example.utasteapplication;
/**
 * Author: Othmane El Moutaouakkil
 *
 **/

import android.os.Build;

import java.time.LocalDateTime;

/**
 * Classe abstraite User - Représente un utilisateur générique du système
 * Classe parente pour Administrator, Chef et Waiter
 * Contient les attributs et méthodes communs à tous les types d'utilisateurs
 */
public abstract class User {
    // Attributs protégés accessibles par les classes enfants
    protected String firstName; // Prénom de l'utilisateur (optionnel)
    protected String lastName; // Nom de famille de l'utilisateur (optionnel)
    protected String email; // Adresse email de l'utilisateur (obligatoire, unique, sert d'identifiant)
    protected String password; // Mot de passe de l'utilisateur (obligatoire)
    protected final String createdAt; // Date et heure de création du compte (immuable)
    protected String modifiedAt; // Date et heure de la dernière modification du profil

    /**
     * Constructeur de la classe User
     * Initialise un nouvel utilisateur avec un email et un mot de passe
     *
     * @param email L'adresse email de l'utilisateur (utilisée pour l'authentification)
     * @param password Le mot de passe de l'utilisateur
     */
    public User(String email, String password) {
        this.email = email;
        this.password = password;
        // Enregistre l'horodatage de création (final = ne peut plus être modifié)
        this.createdAt = getCurrentTimestamp();
        // Initialise la date de modification avec la même valeur que la création
        this.modifiedAt = this.createdAt;
    }

    /**
     * Méthode pour authentifier un utilisateur
     * Vérifie si le mot de passe saisi correspond au mot de passe stocké
     *
     * @param inputPwd Le mot de passe saisi par l'utilisateur
     * @return true si le mot de passe est correct, false sinon
     */
    public boolean authenticate(String inputPwd) {
        // Compare le mot de passe saisi avec le mot de passe stocké
        return this.password.equals(inputPwd);
    }

    /**
     * Méthode pour changer le mot de passe de l'utilisateur
     * Met à jour automatiquement la date de modification
     *
     * @param newPwd Le nouveau mot de passe
     */
    public void changePassword(String newPwd) {
        // Remplace l'ancien mot de passe par le nouveau
        this.password = newPwd;
        // Met à jour l'horodatage de modification
        updateModifiedAt();
    }

    /**
     * Méthode pour mettre à jour le profil de l'utilisateur
     * Permet de modifier le prénom, le nom et l'email
     * Met à jour automatiquement la date de modification
     *
     * @param firstName Le nouveau prénom
     * @param lastName Le nouveau nom de famille
     * @param email La nouvelle adresse email
     */
    public void updateProfile(String firstName, String lastName, String email) {
        // Met à jour les informations du profil
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        // Met à jour l'horodatage de modification
        updateModifiedAt();
    }

    /**
     * Getter pour récupérer l'email de l'utilisateur
     *
     * @return L'adresse email de l'utilisateur
     */
    public String getEmail() {
        return email;
    }

    /**
     * Méthode protégée pour mettre à jour la date de dernière modification
     * Appelée automatiquement lors de toute modification du profil ou du mot de passe
     */
    protected void updateModifiedAt() {
        // Enregistre l'horodatage actuel comme date de modification
        this.modifiedAt = getCurrentTimestamp();
    }

    /**
     * Méthode privée pour obtenir l'horodatage actuel
     * Utilise LocalDateTime si disponible (Android API 26+)
     *
     * @return L'horodatage actuel sous forme de chaîne de caractères
     */
    private String getCurrentTimestamp() {
        // Vérifie si la version d'Android supporte LocalDateTime (API 26 = Android 8.0+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Utilise l'API moderne LocalDateTime
            return LocalDateTime.now().toString();
        }
        // Message d'erreur si la version Android est trop ancienne
        // Note: Cette condition devrait idéalement utiliser SimpleDateFormat comme dans Session.java
        return "minimum API version required for this method is API 26, I think... --Othmane";
    }

    /**
     * Méthode abstraite pour obtenir le rôle de l'utilisateur
     * Doit être implémentée par chaque classe enfant (Administrator, Chef, Waiter)
     *
     * @return Le rôle de l'utilisateur sous forme de chaîne de caractères
     */
    public abstract String getRole();
}