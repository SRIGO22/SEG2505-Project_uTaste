package com.example.utasteapplication;/*
* Author: Sara Rigotti
*/

public class Waiter extends User implements Role {
    public Waiter(String email, String password) {
        super(email, password);
    }

    @Override
    public String getRole() {
        return "Waiter";
    }

    @Override
    public String getRoleName() {
        return getRole();
    }
}
