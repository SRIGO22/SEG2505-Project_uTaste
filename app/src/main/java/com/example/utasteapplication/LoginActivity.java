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

/**
 * Activité de connexion - Point d'entrée de l'application uTaste
 * Permet aux utilisateurs de s'authentifier avec leur email et mot de passe
 */
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

    /**
     * Méthode pour gérer le processus de connexion
     * Valide les informations d'identification et redirige vers l'écran approprié selon le rôle
     */
    private void handleLogin() {
        // Récupère les valeurs saisies par l'utilisateur et supprime les espaces
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

        // Validation 1 : Vérifie que les champs email et mot de passe ne sont pas vides
        if (email.isEmpty() || password.isEmpty()) {
            // Affiche un message d'erreur si un champ est vide
            Toast.makeText(this, "Please enter both email and password", Toast.LENGTH_SHORT).show();
            return; // Arrête l'exécution de la méthode
        }

        // Recherche l'utilisateur dans la base de données en utilisant son email
        User user = userManager.findUser(email);

        // Validation 2 : Vérifie si l'utilisateur existe dans le système
        if (user == null) {
            // Affiche un message d'erreur si l'utilisateur n'est pas trouvé
            Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show();
            return; // Arrête l'exécution de la méthode
        }

        // Validation 3 : Authentifie l'utilisateur avec le mot de passe saisi
        if (!user.authenticate(password)) {
            // Affiche un message d'erreur si le mot de passe est incorrect
            Toast.makeText(this, "Incorrect password", Toast.LENGTH_SHORT).show();
            return; // Arrête l'exécution de la méthode
        }

        // Si l'authentification réussit, crée une nouvelle session pour cet utilisateur
        sessionManager.createSession(user);

        // Prépare la navigation vers l'écran d'accueil approprié
        Intent intent;
        // Récupère le rôle de l'utilisateur pour déterminer l'écran de destination
        String role = user.getRole();

        // Détermine l'activité de destination selon le rôle de l'utilisateur
        switch (role) {
            case "Administrator":
                // Redirige vers l'écran d'accueil de l'administrateur
                intent = new Intent(LoginActivity.this, AdministratorHomeActivity.class);
                break;
            case "Chef":
                // Redirige vers l'écran d'accueil du chef cuisinier
                intent = new Intent(LoginActivity.this, ChefHomeActivity.class);
                break;
            case "Waiter":
                // Redirige vers l'écran d'accueil du serveur
                intent = new Intent(LoginActivity.this, WaiterHomeActivity.class);
                break;
            default:
                // Si le rôle est inconnu ou invalide, affiche un message d'erreur
                Toast.makeText(this, "Unknown role", Toast.LENGTH_SHORT).show();
                return; // Arrête l'exécution de la méthode
        }

        // Passe les informations de l'utilisateur à la prochaine activité
        intent.putExtra("USER_EMAIL", email); // Transfère l'email de l'utilisateur
        intent.putExtra("USER_ROLE", role); // Transfère le rôle de l'utilisateur
        // Lance l'activité d'accueil appropriée
        startActivity(intent);

        // Affiche un message de bienvenue avec le rôle de l'utilisateur
        Toast.makeText(this, "Welcome " + role + "!", Toast.LENGTH_SHORT).show();
    }

    /**
     * Méthode appelée lorsque l'activité est détruite
     * Note : On ne déconnecte PAS automatiquement l'utilisateur ici
     * La déconnexion doit être explicite (via le bouton logout)
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Ne pas appeler sessionManager.logout() ici pour maintenir la session active
        // L'utilisateur doit se déconnecter manuellement via le bouton de déconnexion
    }
}