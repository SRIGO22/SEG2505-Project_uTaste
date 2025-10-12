package com.example.utasteapplication;

/*
 * Author: Sara Rigotti
 */

import java.util.ArrayList;
import java.util.List;

public class UserManager {
    private List<User> users = new ArrayList<>();

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
}