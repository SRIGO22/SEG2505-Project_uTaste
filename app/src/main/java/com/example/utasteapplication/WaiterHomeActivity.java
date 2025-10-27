package com.example.utasteapplication;

/**
 * Author: Othmane El Moutaouakkil
 * Waiter Home Activity
 * Updated to use SessionManager
 */

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class WaiterHomeActivity extends AppCompatActivity {

    // Éléments de l’interface utilisateur
    private TextView welcomeText; // Message de bienvenue
    private Button changePasswordButton; // Bouton pour changer le mot de passe
    private Button logoutButton; // Bouton pour se déconnecter

    // Variables pour gérer la session et l'utilisateur
    private String userEmail; // Email du serveur actuellement connecté
    private SessionManager sessionManager; // Gestionnaire de session pour suivre l'utilisateur connecté

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Associe l'activité avec le fichier de mise en page XML pour le serveur
        setContentView(R.layout.activity_waiter_home);

        // Récupère l'instance unique du gestionnaire de session (pattern Singleton)
        sessionManager = SessionManager.getInstance();

        // Récupère l'email du serveur passé depuis l'activité de connexion
        userEmail = getIntent().getStringExtra("USER_EMAIL");

        // Initialise les composants de l'interface en les liant avec les éléments du layout XML
        welcomeText = findViewById(R.id.welcome_text);
        changePasswordButton = findViewById(R.id.change_password_button);
        logoutButton = findViewById(R.id.logout_button);

        // Affiche un message de bienvenue personnalisé pour le serveur
        // Utilise une ressource string pour permettre la localisation
        welcomeText.setText(getString(R.string.welcome_message, "Waiter"));

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
                // Déconnecte le serveur et retourne à l'écran de connexion
                logout();
            }
        });
    }

    // Ouvre l’écran de changement de mot de passe
    private void openChangePassword() {
        // Crée une intention pour naviguer vers l'activité de changement de mot de passe
        Intent intent = new Intent(WaiterHomeActivity.this, ChangePasswordActivity.class);
        // Passe l'email du serveur à la prochaine activité
        intent.putExtra("USER_EMAIL", userEmail);
        // Passe le rôle "Waiter" à la prochaine activité
        intent.putExtra("USER_ROLE", "Waiter");
        // Lance la nouvelle activité
        startActivity(intent);
    }

    // Déconnecte l’utilisateur et retourne à l’écran de connexion
    private void logout() {
        // Ferme la session active
        sessionManager.logout();

        // Redirige vers la page de connexion et nettoie l’historique
        Intent intent = new Intent(WaiterHomeActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        // Ferme l'activité actuelle
        finish();
    }
}