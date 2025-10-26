package com.example.utasteapplication;

/*
 * Author: Othmane El Moutaouakkil
 */

/**
 * Classe Administrator - Représente un utilisateur avec le rôle d'administrateur
 * Cette classe hérite de User et implémente l'interface Role
 * L'administrateur peut gérer les utilisateurs, réinitialiser les mots de passe et la base de données
 */
public class Administrator extends User implements Role {

    /**
     * Constructeur de la classe Administrator
     * Crée un nouvel utilisateur avec le rôle d'administrateur
     *
     * @param email L'adresse email de l'administrateur (utilisée pour l'authentification)
     * @param password Le mot de passe de l'administrateur
     */
    public Administrator(String email, String password) {
        // Appelle le constructeur de la classe parent (User) pour initialiser l'email et le mot de passe
        super(email, password);
    }

    /**
     * Retourne le rôle de cet utilisateur
     * Méthode héritée de la classe User (méthode abstraite)
     *
     * @return Le rôle "Administrator" sous forme de chaîne de caractères
     */
    @Override
    public String getRole() {
        return "Administrator";
    }

    /**
     * Retourne le nom du rôle de cet utilisateur
     * Méthode de l'interface Role
     *
     * @return Le nom du rôle "Administrator" sous forme de chaîne de caractères
     */
    @Override
    public String getRoleName() {
        // Utilise la méthode getRole() pour retourner le même nom de rôle
        return getRole();
    }
}