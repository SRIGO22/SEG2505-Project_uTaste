package com.example.utasteapplication;
/**
 * Author: Othmane El Moutaouakkil
 *
 **/

import android.os.Build;

import java.time.LocalDateTime;

// Classe abstraite qui représente un utilisateur général du système
// Les classes Administrator, Chef et Waiter héritent de celle-ci
public abstract class User {
    // Informations de base de l'utilisateur
    protected String firstName;   // Prénom (optionnel)
    protected String lastName;    // Nom de famille (optionnel)
    protected String email;       // Adresse courriel (unique, sert d'identifiant)
    protected String password;    // Mot de passe

    // Suivi de création et de modification du compte
    protected final String createdAt; // Date de création (ne change jamais)
    protected String modifiedAt;      // Dernière modification du profil

    // Constructeur : crée un nouvel utilisateur avec un courriel et un mot de passe
    public User(String email, String password) {
        this.email = email;
        this.password = password;
        // Définit la date de création au moment de l’inscription
        this.createdAt = getCurrentTimestamp();
        // Au début, la date de modification est la même que celle de création
        this.modifiedAt = this.createdAt;
    }

    // Vérifie si le mot de passe entré correspond au bon mot de passe
    public boolean authenticate(String inputPwd) {
        // Compare le mot de passe saisi avec le mot de passe stocké
        return this.password.equals(inputPwd);
    }

    // Change le mot de passe de l’utilisateur et met à jour la date de modification
    public void changePassword(String newPwd) {
        this.password = newPwd;
        updateModifiedAt();
    }

    // Met à jour les infos du profil (nom, prénom, email)
    // et actualise la date de dernière modification
    public void updateProfile(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        updateModifiedAt();
    }

    // Retourne l’adresse courriel de l’utilisateur
    public String getEmail() {
        return email;
    }

    // Met à jour la date de dernière modification du compte
    protected void updateModifiedAt() {
        this.modifiedAt = getCurrentTimestamp();
    }

    // Récupère la date et l’heure actuelles sous forme de texte
    // Utilise LocalDateTime si la version Android le permet (API 26+)
    private String getCurrentTimestamp() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return LocalDateTime.now().toString();
        }
        // Si la version Android est trop vieille, retourne un message d’avertissement
        return "minimum API version required for this method is API 26, I think... --Othmane";
    }

    // Méthode abstraite : chaque rôle (Admin, Chef, Waiter) doit définir son propre type de rôle
    public abstract String getRole();
}