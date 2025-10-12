package com.example.utasteapplication;

/*
 * Author: Othmane El Moutaouakkil
 */

public class Administrator extends User implements Role {
    public Administrator(String email, String password) {
        super(email, password);
    }

    @Override
    public String getRole() {
        return "Administrator";
    }

    @Override
    public String getRoleName() {
        return getRole();
    }
}