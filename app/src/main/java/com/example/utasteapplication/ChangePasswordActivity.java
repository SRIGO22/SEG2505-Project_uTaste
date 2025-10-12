package com.example.utasteapplication;

/**
 * Author: Othmane El Moutaouakkil
 * Change Password Activity
 */

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.utasteapplication.UserManager;

public class ChangePasswordActivity extends AppCompatActivity {

    private EditText currentPasswordInput;
    private EditText newPasswordInput;
    private EditText confirmPasswordInput;
    private Button savePasswordButton;
    private Button cancelButton;
    private String userEmail;
    private String userRole;
    private UserManager userManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        // Get user details from intent
        userEmail = getIntent().getStringExtra("USER_EMAIL");
        userRole = getIntent().getStringExtra("USER_ROLE");

        // Initialize UserManager (in a real app, this would be a singleton)
        userManager = new UserManager();
        initializeDefaultUsers();

        // Initialize UI components
        currentPasswordInput = findViewById(R.id.current_password_input);
        newPasswordInput = findViewById(R.id.new_password_input);
        confirmPasswordInput = findViewById(R.id.confirm_password_input);
        savePasswordButton = findViewById(R.id.save_password_button);
        cancelButton = findViewById(R.id.cancel_button);

        // Set button listeners
        savePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleChangePassword();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initializeDefaultUsers() {
        // Re-create default users (in production, this would be from a database)
        Administrator admin = new Administrator("admin@utaste.com", "admin123");
        admin.updateProfile("Admin", "User", "admin@utaste.com");
        userManager.addUser(admin);

        Chef chef = new Chef("chef@utaste.com", "chef123");
        chef.updateProfile("Chef", "User", "chef@utaste.com");
        userManager.addUser(chef);

        Waiter waiter = new Waiter("waiter@utaste.com", "waiter123");
        waiter.updateProfile("Waiter", "User", "waiter@utaste.com");
        userManager.addUser(waiter);
    }

    private void handleChangePassword() {
        String currentPassword = currentPasswordInput.getText().toString().trim();
        String newPassword = newPasswordInput.getText().toString().trim();
        String confirmPassword = confirmPasswordInput.getText().toString().trim();

        // Validate inputs
        if (currentPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check if new passwords match
        if (!newPassword.equals(confirmPassword)) {
            Toast.makeText(this, "New passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check password length
        if (newPassword.length() < 6) {
            Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
            return;
        }

        // Find user and authenticate
        User user = userManager.findUser(userEmail);

        if (user == null) {
            Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!user.authenticate(currentPassword)) {
            Toast.makeText(this, "Current password is incorrect", Toast.LENGTH_SHORT).show();
            return;
        }

        // Change password
        user.changePassword(newPassword);

        Toast.makeText(this, "Password changed successfully!", Toast.LENGTH_LONG).show();

        // Clear input fields
        currentPasswordInput.setText("");
        newPasswordInput.setText("");
        confirmPasswordInput.setText("");

        // Return to previous screen
        finish();
    }
}