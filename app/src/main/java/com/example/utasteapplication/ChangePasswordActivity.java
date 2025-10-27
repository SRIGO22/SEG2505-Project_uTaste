package com.example.utasteapplication;

/**
 * Auteur : Othmane El Moutaouakkil
 * Activité : Changement de mot de passe
 * Cette activité permet à un utilisateur de modifier son mot de passe
 * en vérifiant son mot de passe actuel et en enregistrant le nouveau.
 * Elle utilise le pattern Singleton pour la gestion des utilisateurs.
 */

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class ChangePasswordActivity extends AppCompatActivity {

    // === Composants de l’interface ===
    private EditText currentPasswordInput;   // Champ pour entrer le mot de passe actuel
    private EditText newPasswordInput;       // Champ pour entrer le nouveau mot de passe
    private EditText confirmPasswordInput;   // Champ pour confirmer le nouveau mot de passe

    private Button savePasswordButton;       // Bouton pour sauvegarder le nouveau mot de passe
    private Button cancelButton;             // Bouton pour annuler et revenir à l’écran précédent

    // === Informations de l’utilisateur ===
    private String userEmail;                // Adresse courriel de l’utilisateur
    private String userRole;                 // Rôle de l’utilisateur (Administrator, Chef, Waiter)
    private UserManager userManager;         // Gestionnaire des utilisateurs (implémenté en Singleton)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Lie l’activité à son interface XML
        setContentView(R.layout.activity_change_password);

        // Récupère les informations envoyées depuis l’activité précédente
        userEmail = getIntent().getStringExtra("USER_EMAIL");
        userRole = getIntent().getStringExtra("USER_ROLE");

        // Obtient l’unique instance du gestionnaire d’utilisateurs
        userManager = UserManager.getInstance();

        // Lie les éléments XML aux variables Java
        currentPasswordInput = findViewById(R.id.current_password_input);
        newPasswordInput = findViewById(R.id.new_password_input);
        confirmPasswordInput = findViewById(R.id.confirm_password_input);
        savePasswordButton = findViewById(R.id.save_password_button);
        cancelButton = findViewById(R.id.cancel_button);

        // Lorsqu’on clique sur “Sauvegarder”, on tente de changer le mot de passe
        savePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleChangePassword();
            }
        });

        // Lorsqu’on clique sur “Annuler”, on ferme simplement l’écran actuel
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    // Cette méthode gère tout le processus de changement de mot de passe :
    // 1. Vérifie que tous les champs ont été remplis
    // 2. Compare les deux nouveaux mots de passe pour s’assurer qu’ils correspondent
    // 3. Vérifie que le nouveau mot de passe est assez long
    // 4. Authentifie l’utilisateur avec son ancien mot de passe
    // 5. Si tout est bon, enregistre le nouveau mot de passe
    private void handleChangePassword() {

        // Récupère les valeurs entrées et enlève les espaces
        String currentPassword = currentPasswordInput.getText().toString().trim();
        String newPassword = newPasswordInput.getText().toString().trim();
        String confirmPassword = confirmPasswordInput.getText().toString().trim();

        // Étape 1 : Tous les champs doivent être remplis
        if (currentPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "Veuillez remplir tous les champs.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Étape 2 : Vérifie que le nouveau mot de passe est bien confirmé
        if (!newPassword.equals(confirmPassword)) {
            Toast.makeText(this, "Les nouveaux mots de passe ne correspondent pas.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Étape 3 : Vérifie la longueur minimale du mot de passe
        if (newPassword.length() < 6) {
            Toast.makeText(this, "Le mot de passe doit contenir au moins 6 caractères.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Étape 4 : Recherche l’utilisateur à partir de son courriel
        User user = userManager.findUser(userEmail);

        // Vérifie que l’utilisateur existe dans la base de données
        if (user == null) {
            Toast.makeText(this, "Utilisateur introuvable.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Étape 5 : Authentifie le mot de passe actuel avant de modifier
        if (!user.authenticate(currentPassword)) {
            Toast.makeText(this, "Le mot de passe actuel est incorrect.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Tout est valide → on change le mot de passe
        user.changePassword(newPassword);
        Toast.makeText(this, "Mot de passe modifié avec succès!", Toast.LENGTH_LONG).show();

        // Par sécurité, on efface les champs de saisie
        currentPasswordInput.setText("");
        newPasswordInput.setText("");
        confirmPasswordInput.setText("");

        // Retourne à l’écran précédent
        finish();
    }
}