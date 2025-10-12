package com.example.utasteapplication;/*
* Author: Sara Rigotti
*/

import java.time.LocalDateTime;

public class Session {
    private User user;
    private final String loginTime;

    public Session(User user) {
        this.user = user;
        this.loginTime = LocalDateTime.now().toString();
    }

    public void logout() {
        this.user = null;
    }

    public User getUser() {
        return user;
    }

    public String getLoginTime() {
        return loginTime;
    }
}
