package com.example.utasteapplication;

/**
 * Author: Othmane El Moutaouakkil
 * Manage Waiters Activity
 */

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ManageWaitersActivity extends AppCompatActivity {

    private EditText waiterEmailInput;
    private EditText waiterPasswordInput;
    private Button addWaiterButton;
    private Button backButton;
    private RecyclerView waitersRecyclerView;
    private String userEmail;
    private UserManager userManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_waiters);

        // Get user email from intent
        userEmail = getIntent().getStringExtra("USER_EMAIL");

        // Initialize UserManager
        userManager = new UserManager();
        initializeDefaultUsers();

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

    private void initializeDefaultUsers() {
        // Re-create default users (in production, use a database)
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

    private void handleAddWaiter() {
        String email = waiterEmailInput.getText().toString().trim();
        String password = waiterPasswordInput.getText().toString().trim();

        // Validate inputs
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
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