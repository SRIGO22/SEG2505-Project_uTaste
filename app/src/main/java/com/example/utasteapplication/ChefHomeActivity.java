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

public class ChefHomeActivity extends AppCompatActivity {

    private TextView welcomeText;
    private Button changePasswordButton;
    private Button logoutButton;
    private String userEmail;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chef_home);

        // Get SessionManager instance
        sessionManager = SessionManager.getInstance();

        // Get user email from intent
        userEmail = getIntent().getStringExtra("USER_EMAIL");

        // Initialize UI components
        welcomeText = findViewById(R.id.welcome_text);
        changePasswordButton = findViewById(R.id.change_password_button);
        logoutButton = findViewById(R.id.logout_button);

        // Set welcome message
        welcomeText.setText(getString(R.string.welcome_message, "Chef"));

        // Set button listeners
        changePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openChangePassword();
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });
    }

    private void openChangePassword() {
        Intent intent = new Intent(ChefHomeActivity.this, ChangePasswordActivity.class);
        intent.putExtra("USER_EMAIL", userEmail);
        intent.putExtra("USER_ROLE", "Chef");
        startActivity(intent);
    }

    private void logout() {
        // Logout from session
        sessionManager.logout();

        Intent intent = new Intent(ChefHomeActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}