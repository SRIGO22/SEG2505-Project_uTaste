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

public class ManageWaitersActivity extends AppCompatActivity {

    private EditText waiterEmailInput;
    private EditText waiterPasswordInput;
    private Button addWaiterButton;
    private Button backButton;
    private String userEmail;
    private UserManager userManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_waiters);

        // Get user email from intent
        userEmail = getIntent().getStringExtra("USER_EMAIL");

        // Get UserManager singleton instance
        userManager = UserManager.getInstance();

        // Initialize UI components
        waiterEmailInput = findViewById(R.id.waiter_email_input);
        waiterPasswordInput = findViewById(R.id.waiter_password_input);
        addWaiterButton = findViewById(R.id.add_waiter_button);
        backButton = findViewById(R.id.back_button);

        // Set button listeners
        addWaiterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleAddWaiter();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void handleAddWaiter() {
        String email = waiterEmailInput.getText().toString().trim();
        String password = waiterPasswordInput.getText().toString().trim();

        // Validate inputs
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validate email format
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Please enter a valid email address", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check if user already exists
        if (userManager.findUser(email) != null) {
            Toast.makeText(this, "User with this email already exists", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check password length
        if (password.length() < 6) {
            Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create new waiter
        Waiter newWaiter = new Waiter(email, password);
        newWaiter.updateProfile("New", "Waiter", email);
        userManager.addUser(newWaiter);

        Toast.makeText(this, "Waiter added successfully!", Toast.LENGTH_SHORT).show();

        // Clear input fields
        waiterEmailInput.setText("");
        waiterPasswordInput.setText("");
    }
}