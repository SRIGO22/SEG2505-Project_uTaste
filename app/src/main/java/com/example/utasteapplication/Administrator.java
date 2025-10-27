package com.example.utasteapplication;

/*
 * Auteur : Othmane El Moutaouakkil
 */

/**
 * Classe Administrator – représente un utilisateur ayant les privilèges d’administrateur.
 *
 * Cette classe hérite de la classe User et implémente l’interface Role.
 * Elle symbolise un utilisateur spécial capable de gérer le système :
 * par exemple, administrer les comptes utilisateurs ou réinitialiser la base de données.
 */
public class Administrator extends User implements Role {

    /**
     * Constructeur de la classe Administrator.
     * Il crée une instance d’administrateur en réutilisant les champs de la classe User (email et mot de passe).
     *
     * @param email    L’adresse courriel de l’administrateur (utilisée pour la connexion).
     * @param password Le mot de passe associé à cet administrateur.
     */
    public Administrator(String email, String password) {
        // On appelle le constructeur de la classe parente (User)
        // pour initialiser les informations d’identification de base.
        super(email, password);
    }

    /**
     * Retourne le rôle spécifique de cet utilisateur.
     * Cette méthode provient de la classe User, où elle est définie comme abstraite.
     *
     * @return Le texte "Administrator", identifiant ce type d’utilisateur.
     */
    @Override
    public String getRole() {
        return "Administrator";
    }

    /**
     * Retourne le nom du rôle de l’utilisateur.
     * Cette méthode provient de l’interface Role et renvoie la même valeur que getRole().
     *
     * @return Le nom du rôle, soit "Administrator".
     */
    @Override
    public String getRoleName() {
        // Ici, on réutilise simplement la méthode getRole() pour éviter la redondance.
        return getRole();
    }
}