package com.example.utasteapplication;

/*
 * Author: Sara Rigotti
 */

/**
 * Classe Waiter - Représente un utilisateur avec le rôle de serveur
 * Cette classe hérite de User et implémente l'interface Role
 * Le serveur peut voir les recettes, enregistrer des ventes et consulter les bilans
 */
public class Waiter extends User implements Role {

    /**
     * Constructeur de la classe Waiter
     * Crée un nouvel utilisateur avec le rôle de serveur
     *
     * @param email L'adresse email du serveur (utilisée pour l'authentification)
     * @param password Le mot de passe du serveur
     */
    public Waiter(String email, String password) {
        // Appelle le constructeur de la classe parent (User) pour initialiser l'email et le mot de passe
        super(email, password);
    }

    /**
     * Retourne le rôle de cet utilisateur
     * Méthode héritée de la classe User (méthode abstraite)
     *
     * @return Le rôle "Waiter" sous forme de chaîne de caractères
     */
    @Override
    public String getRole() {
        return "Waiter";
    }

    /**
     * Retourne le nom du rôle de cet utilisateur
     * Méthode de l'interface Role
     *
     * @return Le nom du rôle "Waiter" sous forme de chaîne de caractères
     */
    @Override
    public String getRoleName() {
        // Utilise la méthode getRole() pour retourner le même nom de rôle
        return getRole();
    }
}