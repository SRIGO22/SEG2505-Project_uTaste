package com.example.utasteapplication;

/*
 * Author: Sara Rigotti
 */

/**
 * Classe Chef - Représente un utilisateur avec le rôle de chef cuisinier
 * Cette classe hérite de User et implémente l'interface Role
 * Le chef peut créer, modifier et supprimer des recettes et gérer les ingrédients
 */
public class Chef extends User implements Role {

    /**
     * Constructeur de la classe Chef
     * Crée un nouvel utilisateur avec le rôle de chef cuisinier
     *
     * @param email L'adresse email du chef (utilisée pour l'authentification)
     * @param password Le mot de passe du chef
     */
    public Chef(String email, String password) {
        // Appelle le constructeur de la classe parent (User) pour initialiser l'email et le mot de passe
        super(email, password);
    }

    /**
     * Retourne le rôle de cet utilisateur
     * Méthode héritée de la classe User (méthode abstraite)
     *
     * @return Le rôle "Chef" sous forme de chaîne de caractères
     */
    @Override
    public String getRole() {
        return "Chef";
    }

    /**
     * Retourne le nom du rôle de cet utilisateur
     * Méthode de l'interface Role
     *
     * @return Le nom du rôle "Chef" sous forme de chaîne de caractères
     */
    @Override
    public String getRoleName() {
        // Utilise la méthode getRole() pour retourner le même nom de rôle
        return getRole();
    }
}