package com.example.utasteapplication;

/*
 * Author: Sara Rigotti
 * Updated to Singleton pattern for persistent user management
 */

import java.util.ArrayList;
import java.util.List;

public class UserManager {
    private static UserManager instance;
    private List<User> users = new ArrayList<>();

    // Private constructor to prevent instantiation
    private UserManager() {
        initializeDefaultUsers();
    }

    // Singleton getInstance method
    public static synchronized UserManager getInstance() {
        if (instance == null) {
            instance = new UserManager();
        }
        return instance;
    }

    private void initializeDefaultUsers() {
        // Create default administrator
        Administrator admin = new Administrator("admin@utaste.com", "admin123");
        admin.updateProfile("Admin", "User", "admin@utaste.com");
        users.add(admin);

        // Create default chef
        Chef chef = new Chef("chef@utaste.com", "chef123");
        chef.updateProfile("Chef", "User", "chef@utaste.com");
        users.add(chef);

        // Create default waiter
        Waiter waiter = new Waiter("waiter@utaste.com", "waiter123");
        waiter.updateProfile("Waiter", "User", "waiter@utaste.com");
        users.add(waiter);
    }

    public void addUser(User u) {
        users.add(u);
    }

    public void removeUser(String email) {
        users.removeIf(u -> u.getEmail().equals(email));
    }

    public User findUser(String email) {
        for (User u : users) {
            if (u.getEmail().equals(email)) {
                return u;
            }
        }
        return null;
    }

    public List<User> getAllUsers() {
        return users;
    }

    public List<Waiter> getAllWaiters() {
        List<Waiter> waiters = new ArrayList<>();
        for (User u : users) {
            if (u instanceof Waiter) {
                waiters.add((Waiter) u);
            }
        }
        return waiters;
    }
}