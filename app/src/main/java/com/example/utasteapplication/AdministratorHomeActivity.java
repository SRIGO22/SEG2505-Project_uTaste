package com.example.utasteapplication;

/**
 * Auteur : Othmane El Moutaouakkil
 *
 * Classe AdministratorHomeActivity – écran principal pour l’administrateur.
 *
 * Cette activité sert de tableau de bord à l’administrateur après sa connexion.
 * Elle permet de gérer les serveurs, de modifier le mot de passe et de se déconnecter du système.
 *
 * Mise à jour : intègre la gestion de session via la classe SessionManager.
 */

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class AdministratorHomeActivity extends AppCompatActivity {

    // Éléments visuels de l’interface
    private TextView welcomeText;           // Message de bienvenue pour l’administrateur
    private Button manageWaitersButton;     // Bouton pour accéder à la gestion des serveurs
    private Button changePasswordButton;    // Bouton pour changer le mot de passe
    private Button logoutButton;            // Bouton pour se déconnecter

    // Gestion de la session et de l’utilisateur
    private String userEmail;               // Email de l’administrateur actuellement connecté
    private SessionManager sessionManager;  // Instance du gestionnaire de session (pattern Singleton)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Lie cette activité à son interface XML correspondante
        setContentView(R.layout.activity_administrator_home);

        // Récupère l’unique instance du gestionnaire de session
        sessionManager = SessionManager.getInstance();

        // Récupère l’adresse email de l’utilisateur envoyée depuis la page précédente
        userEmail = getIntent().getStringExtra("USER_EMAIL");

        // Associe les éléments du layout XML avec les variables Java
        welcomeText = findViewById(R.id.welcome_text);
        manageWaitersButton = findViewById(R.id.manage_waiters_button);
        changePasswordButton = findViewById(R.id.change_password_button);
        logoutButton = findViewById(R.id.logout_button);

        // Affiche un message d’accueil à l’administrateur connecté
        welcomeText.setText("Welcome, Administrator!");

        // ----- Configuration des boutons -----

        // Bouton : Gérer les serveurs
        manageWaitersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openManageWaiters();
            }
        });

        // Bouton : Changer le mot de passe
        changePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openChangePassword();
            }
        });

        // Bouton : Se déconnecter
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });
    }

    // Ouvre la page de gestion des serveurs
    // Transmet l’adresse courriel de l’administrateur à la prochaine activité
    private void openManageWaiters() {
        Intent intent = new Intent(AdministratorHomeActivity.this, ManageWaitersActivity.class);
        intent.putExtra("USER_EMAIL", userEmail);
        startActivity(intent);
    }

    // Ouvre la page de changement de mot de passe
    // Passe l’email et le rôle "Administrator" à la prochaine activité
    private void openChangePassword() {
        Intent intent = new Intent(AdministratorHomeActivity.this, ChangePasswordActivity.class);
        intent.putExtra("USER_EMAIL", userEmail);
        intent.putExtra("USER_ROLE", "Administrator");
        startActivity(intent);
    }

    // Déconnecte l’utilisateur actuel et retourne à la page de connexion
    private void logout() {
        // Termine la session courante
        sessionManager.logout();

        // Redirige vers la page de connexion tout en effaçant l’historique
        Intent intent = new Intent(AdministratorHomeActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

        // Ferme l’activité actuelle
        finish();
    }
}