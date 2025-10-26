package com.example.utasteapplication;

/**
 * Author: Othmane El Moutaouakkil
 * Manage Waiters Activity
 * Updated to use Singleton pattern
 */

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Activité de gestion des serveurs
 * Permet à l'administrateur d'ajouter de nouveaux serveurs au système
 */
public class ManageWaitersActivity extends AppCompatActivity {

    // Déclaration des composants de l'interface utilisateur
    private EditText waiterEmailInput; // Champ de saisie pour l'email du nouveau serveur
    private EditText waiterPasswordInput; // Champ de saisie pour le mot de passe du nouveau serveur
    private Button addWaiterButton; // Bouton pour ajouter un nouveau serveur
    private Button backButton; // Bouton pour retourner à l'écran précédent

    // Variables pour gérer l'utilisateur et les données
    private String userEmail; // Email de l'administrateur qui gère les serveurs
    private UserManager userManager; // Gestionnaire des utilisateurs (pattern Singleton)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Associe l'activité avec le fichier de mise en page XML pour la gestion des serveurs
        setContentView(R.layout.activity_manage_waiters);

        // Récupère l'email de l'administrateur passé depuis l'activité précédente
        userEmail = getIntent().getStringExtra("USER_EMAIL");

        // Récupère l'instance unique du gestionnaire d'utilisateurs (pattern Singleton)
        userManager = UserManager.getInstance();

        // Initialise les composants de l'interface en les liant avec les éléments du layout XML
        waiterEmailInput = findViewById(R.id.waiter_email_input);
        waiterPasswordInput = findViewById(R.id.waiter_password_input);
        addWaiterButton = findViewById(R.id.add_waiter_button);
        backButton = findViewById(R.id.back_button);

        // Configure le comportement du bouton "Ajouter un serveur"
        addWaiterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Traite l'ajout d'un nouveau serveur
                handleAddWaiter();
            }
        });

        // Configure le comportement du bouton "Retour"
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Ferme l'activité et retourne à l'écran d'accueil de l'administrateur
                finish();
            }
        });
    }

    /**
     * Méthode pour gérer l'ajout d'un nouveau serveur
     * Valide les informations saisies et crée un nouvel utilisateur avec le rôle "Waiter"
     */
    private void handleAddWaiter() {
        // Récupère les valeurs saisies par l'administrateur et supprime les espaces
        String email = waiterEmailInput.getText().toString().trim();
        String password = waiterPasswordInput.getText().toString().trim();

        // Validation 1 : Vérifie que tous les champs sont remplis
        if (email.isEmpty() || password.isEmpty()) {
            // Affiche un message d'erreur si un champ est vide
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return; // Arrête l'exécution de la méthode
        }

        // Validation 2 : Vérifie que l'email a un format valide
        // Utilise un validateur d'email intégré à Android
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            // Affiche un message d'erreur si le format de l'email est invalide
            Toast.makeText(this, "Please enter a valid email address", Toast.LENGTH_SHORT).show();
            return; // Arrête l'exécution de la méthode
        }

        // Validation 3 : Vérifie qu'un utilisateur avec cet email n'existe pas déjà
        if (userManager.findUser(email) != null) {
            // Affiche un message d'erreur si l'email est déjà utilisé
            Toast.makeText(this, "User with this email already exists", Toast.LENGTH_SHORT).show();
            return; // Arrête l'exécution de la méthode
        }

        // Validation 4 : Vérifie que le mot de passe a au moins 6 caractères
        if (password.length() < 6) {
            // Affiche un message d'erreur si le mot de passe est trop court
            Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
            return; // Arrête l'exécution de la méthode
        }

        // Si toutes les validations passent, crée un nouveau serveur
        Waiter newWaiter = new Waiter(email, password);
        // Met à jour le profil du nouveau serveur avec un prénom et nom par défaut
        newWaiter.updateProfile("New", "Waiter", email);
        // Ajoute le nouveau serveur au gestionnaire d'utilisateurs
        userManager.addUser(newWaiter);

        // Affiche un message de confirmation de l'ajout réussi
        Toast.makeText(this, "Waiter added successfully!", Toast.LENGTH_SHORT).show();

        // Efface les champs de saisie pour permettre l'ajout d'un autre serveur
        waiterEmailInput.setText("");
        waiterPasswordInput.setText("");
    }
}