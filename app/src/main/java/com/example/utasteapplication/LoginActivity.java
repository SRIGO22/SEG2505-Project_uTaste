package com.example.utasteapplication;

/**
 * Author: Othmane El Moutaouakkil
 * Login Activity - Entry point for the uTaste application
 * Updated to use Singleton pattern
 */

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

// Éléments de l’interface utilisateur
public class LoginActivity extends AppCompatActivity {

    // Déclaration des composants de l'interface utilisateur
    private EditText emailInput; // Champ de saisie pour l'adresse email
    private EditText passwordInput; // Champ de saisie pour le mot de passe
    private Button loginButton; // Bouton pour soumettre la connexion

    // Gestionnaires pour les utilisateurs et les sessions
    private UserManager userManager; // Gestionnaire des utilisateurs (pattern Singleton)
    private SessionManager sessionManager; // Gestionnaire de session (pattern Singleton)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Associe l'activité avec le fichier de mise en page XML de connexion
        setContentView(R.layout.activity_login);

        // Récupère les instances uniques des gestionnaires (pattern Singleton)
        userManager = UserManager.getInstance();
        sessionManager = SessionManager.getInstance();

        // Initialise les composants de l'interface en les liant avec les éléments du layout XML
        emailInput = findViewById(R.id.email_input);
        passwordInput = findViewById(R.id.password_input);
        loginButton = findViewById(R.id.login_button);

        // Configure le comportement du bouton de connexion
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Traite la tentative de connexion lorsque le bouton est cliqué
                handleLogin();
            }
        });
    }

    // Gère le processus de connexion :
    // 1. Vérifie les champs
    // 2. Authentifie l’utilisateur
    // 3. Redirige vers l’écran correspondant à son rôle
    private void handleLogin() {
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

        // Vérifie que les champs ne sont pas vides
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter both email and password", Toast.LENGTH_SHORT).show();
            return; // Arrête l'exécution de la méthode
        }

        // Recherche l'utilisateur dans la base de données en utilisant son email
        User user = userManager.findUser(email);

        // Vérifie si l’utilisateur existe
        if (user == null) {
            Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show();
            return;
        }

        // Vérifie le mot de passe
        if (!user.authenticate(password)) {
            Toast.makeText(this, "Incorrect password", Toast.LENGTH_SHORT).show();
            return;
        }

        // Crée une session pour l’utilisateur connecté
        sessionManager.createSession(user);

        // Redirige vers l’activité correspondant au rôle
        Intent intent;
        String role = user.getRole();

        switch (role) {
            case "Administrator":
                intent = new Intent(LoginActivity.this, AdministratorHomeActivity.class);
                break;
            case "Chef":
                intent = new Intent(LoginActivity.this, ChefHomeActivity.class);
                break;
            case "Waiter":
                intent = new Intent(LoginActivity.this, WaiterHomeActivity.class);
                break;
            default:
                Toast.makeText(this, "Unknown role", Toast.LENGTH_SHORT).show();
                return; // Arrête l'exécution de la méthode
        }

        // Passe les infos de l’utilisateur à la prochaine activité
        intent.putExtra("USER_EMAIL", email);
        intent.putExtra("USER_ROLE", role);
        startActivity(intent);

        // Affiche un message de bienvenue avec le rôle de l'utilisateur
        Toast.makeText(this, "Welcome " + role + "!", Toast.LENGTH_SHORT).show();
    }

    // On ne ferme pas la session automatiquement ici.
    // L’utilisateur doit se déconnecter lui-même via le bouton “Logout”.
    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}