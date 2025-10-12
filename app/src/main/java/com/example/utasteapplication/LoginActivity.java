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

public class LoginActivity extends AppCompatActivity {

    private EditText emailInput;
    private EditText passwordInput;
    private Button loginButton;
    private UserManager userManager;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Get singleton instances
        userManager = UserManager.getInstance();
        sessionManager = SessionManager.getInstance();

        // Initialize UI components
        emailInput = findViewById(R.id.email_input);
        passwordInput = findViewById(R.id.password_input);
        loginButton = findViewById(R.id.login_button);

        // Set login button click listener
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleLogin();
            }
        });
    }

    private void handleLogin() {
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

        // Validate input
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter both email and password", Toast.LENGTH_SHORT).show();
            return;
        }

        // Find user
        User user = userManager.findUser(email);

        if (user == null) {
            Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show();
            return;
        }

        // Authenticate
        if (!user.authenticate(password)) {
            Toast.makeText(this, "Incorrect password", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create session
        sessionManager.createSession(user);

        // Navigate to appropriate home screen based on role
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
                return;
        }

        // Pass user email to next activity
        intent.putExtra("USER_EMAIL", email);
        intent.putExtra("USER_ROLE", role);
        startActivity(intent);

        Toast.makeText(this, "Welcome " + role + "!", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Don't logout here - let user explicitly logout
    }
}