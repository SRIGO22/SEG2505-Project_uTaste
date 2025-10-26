package com.example.utasteapplication;

/**
 * Author: Othmane El Moutaouakkil
 * Change Password Activity
 * Updated to use Singleton pattern
 */

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class ChangePasswordActivity extends AppCompatActivity {

    // Déclaration des champs de saisie pour les mots de passe
    private EditText currentPasswordInput; // Champ pour entrer le mot de passe actuel
    private EditText newPasswordInput; // Champ pour entrer le nouveau mot de passe
    private EditText confirmPasswordInput; // Champ pour confirmer le nouveau mot de passe

    // Déclaration des boutons
    private Button savePasswordButton; // Bouton pour sauvegarder le nouveau mot de passe
    private Button cancelButton; // Bouton pour annuler et retourner à l'écran précédent

    // Variables pour stocker les informations de l'utilisateur
    private String userEmail; // Email de l'utilisateur qui change son mot de passe
    private String userRole; // Rôle de l'utilisateur (Administrator, Chef, Waiter)
    private UserManager userManager; // Gestionnaire des utilisateurs (pattern Singleton)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Associe l'activité avec le fichier de mise en page XML
        setContentView(R.layout.activity_change_password);

        // Récupère les détails de l'utilisateur passés depuis l'activité précédente
        userEmail = getIntent().getStringExtra("USER_EMAIL");
        userRole = getIntent().getStringExtra("USER_ROLE");

        // Récupère l'instance unique du gestionnaire d'utilisateurs (pattern Singleton)
        userManager = UserManager.getInstance();

        // Initialise les composants de l'interface en les liant avec les éléments du layout XML
        currentPasswordInput = findViewById(R.id.current_password_input);
        newPasswordInput = findViewById(R.id.new_password_input);
        confirmPasswordInput = findViewById(R.id.confirm_password_input);
        savePasswordButton = findViewById(R.id.save_password_button);
        cancelButton = findViewById(R.id.cancel_button);

        // Configure le comportement du bouton "Sauvegarder"
        savePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Traite la demande de changement de mot de passe
                handleChangePassword();
            }
        });

        // Configure le comportement du bouton "Annuler"
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Ferme l'activité et retourne à l'écran précédent
                finish();
            }
        });
    }

    /**
     * Méthode pour gérer le processus de changement de mot de passe
     * Valide les entrées et met à jour le mot de passe si tout est correct
     */
    private void handleChangePassword() {
        // Récupère les valeurs saisies par l'utilisateur et supprime les espaces
        String currentPassword = currentPasswordInput.getText().toString().trim();
        String newPassword = newPasswordInput.getText().toString().trim();
        String confirmPassword = confirmPasswordInput.getText().toString().trim();

        // Validation 1 : Vérifie que tous les champs sont remplis
        if (currentPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
            // Affiche un message d'erreur si un champ est vide
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return; // Arrête l'exécution de la méthode
        }

        // Validation 2 : Vérifie que le nouveau mot de passe et la confirmation correspondent
        if (!newPassword.equals(confirmPassword)) {
            // Affiche un message d'erreur si les mots de passe ne correspondent pas
            Toast.makeText(this, "New passwords do not match", Toast.LENGTH_SHORT).show();
            return; // Arrête l'exécution de la méthode
        }

        // Validation 3 : Vérifie que le nouveau mot de passe a au moins 6 caractères
        if (newPassword.length() < 6) {
            // Affiche un message d'erreur si le mot de passe est trop court
            Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
            return; // Arrête l'exécution de la méthode
        }

        // Recherche l'utilisateur dans la base de données en utilisant son email
        User user = userManager.findUser(userEmail);

        // Vérifie si l'utilisateur existe dans le système
        if (user == null) {
            // Affiche un message d'erreur si l'utilisateur n'est pas trouvé
            Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show();
            return; // Arrête l'exécution de la méthode
        }

        // Authentifie l'utilisateur avec le mot de passe actuel saisi
        if (!user.authenticate(currentPassword)) {
            // Affiche un message d'erreur si le mot de passe actuel est incorrect
            Toast.makeText(this, "Current password is incorrect", Toast.LENGTH_SHORT).show();
            return; // Arrête l'exécution de la méthode
        }

        // Si toutes les validations passent, change le mot de passe de l'utilisateur
        user.changePassword(newPassword);

        // Affiche un message de confirmation du changement réussi
        Toast.makeText(this, "Password changed successfully!", Toast.LENGTH_LONG).show();

        // Efface tous les champs de saisie pour la sécurité
        currentPasswordInput.setText("");
        newPasswordInput.setText("");
        confirmPasswordInput.setText("");

        // Ferme l'activité et retourne à l'écran précédent
        finish();
    }
}