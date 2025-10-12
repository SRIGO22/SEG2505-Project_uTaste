package com.example.utasteapplication;

/**
 * Author: Othmane El Moutaouakkil
 * Chef Home Activity
 * Updated to use SessionManager
 */

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Activité d'accueil pour le rôle Chef
 * Affiche les fonctionnalités disponibles pour un chef cuisinier
 */
public class ChefHomeActivity extends AppCompatActivity {

    // Déclaration des composants de l'interface utilisateur
    private TextView welcomeText; // Texte de bienvenue affiché au chef
    private Button changePasswordButton; // Bouton pour changer le mot de passe
    private Button logoutButton; // Bouton pour se déconnecter

    // Variables pour gérer la session et l'utilisateur
    private String userEmail; // Email du chef actuellement connecté
    private SessionManager sessionManager; // Gestionnaire de session pour suivre l'utilisateur connecté

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Associe l'activité avec le fichier de mise en page XML pour le chef
        setContentView(R.layout.activity_chef_home);

        // Récupère l'instance unique du gestionnaire de session (pattern Singleton)
        sessionManager = SessionManager.getInstance();

        // Récupère l'email du chef passé depuis l'activité de connexion
        userEmail = getIntent().getStringExtra("USER_EMAIL");

        // Initialise les composants de l'interface en les liant avec les éléments du layout XML
        welcomeText = findViewById(R.id.welcome_text);
        changePasswordButton = findViewById(R.id.change_password_button);
        logoutButton = findViewById(R.id.logout_button);

        // Affiche un message de bienvenue personnalisé pour le chef
        // Utilise une ressource string pour permettre la localisation
        welcomeText.setText(getString(R.string.welcome_message, "Chef"));

        // Configure le comportement du bouton "Changer le mot de passe"
        changePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Ouvre l'écran de changement de mot de passe
                openChangePassword();
            }
        });

        // Configure le comportement du bouton "Déconnexion"
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Déconnecte le chef et retourne à l'écran de connexion
                logout();
            }
        });
    }

    /**
     * Méthode pour ouvrir l'activité de changement de mot de passe
     * Transfère l'email et le rôle du chef à la prochaine activité
     */
    private void openChangePassword() {
        // Crée une intention pour naviguer vers l'activité de changement de mot de passe
        Intent intent = new Intent(ChefHomeActivity.this, ChangePasswordActivity.class);
        // Passe l'email du chef à la prochaine activité
        intent.putExtra("USER_EMAIL", userEmail);
        // Passe le rôle "Chef" à la prochaine activité
        intent.putExtra("USER_ROLE", "Chef");
        // Lance la nouvelle activité
        startActivity(intent);
    }

    /**
     * Méthode pour déconnecter le chef
     * Efface la session et retourne à l'écran de connexion
     */
    private void logout() {
        // Déconnecte l'utilisateur en utilisant le gestionnaire de session
        sessionManager.logout();

        // Crée une intention pour retourner à l'écran de connexion
        Intent intent = new Intent(ChefHomeActivity.this, LoginActivity.class);
        // Efface toutes les activités précédentes de la pile et démarre une nouvelle tâche
        // Cela empêche le chef de revenir en arrière après la déconnexion
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        // Lance l'activité de connexion
        startActivity(intent);
        // Ferme l'activité actuelle
        finish();
    }
}
