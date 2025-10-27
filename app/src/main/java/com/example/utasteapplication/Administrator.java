package com.example.utasteapplication;

/*
 * Auteur : Othmane El Moutaouakkil
 * Classe représentant un utilisateur ayant les privilèges d’administrateur.
 * L’administrateur peut gérer le système, modifier les comptes ou réinitialiser la base de données.
 */
public class Administrator extends User implements Role {

    // Constructeur : crée un nouvel administrateur en utilisant le courriel et le mot de passe

    public Administrator(String email, String password) {
        // Appelle le constructeur de la classe parente (User)

        super(email, password);
    }

    // Retourne le rôle de cet utilisateur (ici : "Administrator")
    @Override
    public String getRole() {
        return "Administrator";
    }

    // Retourne le nom du rôle, identique à getRole()
    @Override
    public String getRoleName() {
        return getRole();
    }
}