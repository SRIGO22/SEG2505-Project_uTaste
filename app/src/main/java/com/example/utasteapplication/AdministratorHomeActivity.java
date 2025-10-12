package com.example.utasteapplication;

/**
 * Author: Othmane El Moutaouakkil
 * Administrator Home Activity
 */

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class AdministratorHomeActivity extends AppCompatActivity {

    private TextView welcomeText;
    private Button manageWaitersButton;
    private Button changePasswordButton;
    private Button logoutButton;
    private String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administrator_home);

        // Get user email from intent
        userEmail = getIntent().getStringExtra("USER_EMAIL");

        // Initialize UI components
        welcomeText = findViewById(R.id.welcome_text);
        manageWaitersButton = findViewById(R.id.manage_waiters_button);
        changePasswordButton = findViewById(R.id.change_password_button);
        logoutButton = findViewById(R.id.logout_button);

        // Set welcome message
        welcomeText.setText(getString(R.string.welcome_message, "Administrator"));

        // Set button listeners
        manageWaitersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openManageWaiters();
            }
        });

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

    private void openManageWaiters() {
        Intent intent = new Intent(AdministratorHomeActivity.this, ManageWaitersActivity.class);
        intent.putExtra("USER_EMAIL", userEmail);
        startActivity(intent);
    }

    private void openChangePassword() {
        Intent intent = new Intent(AdministratorHomeActivity.this, ChangePasswordActivity.class);
        intent.putExtra("USER_EMAIL", userEmail);
        intent.putExtra("USER_ROLE", "Administrator");
        startActivity(intent);
    }

    private void logout() {
        Intent intent = new Intent(AdministratorHomeActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}