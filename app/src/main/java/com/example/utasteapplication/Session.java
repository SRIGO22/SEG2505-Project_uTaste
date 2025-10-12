package com.example.utasteapplication;/*
 * Author: Sara Rigotti
 */

import android.os.Build;
import java.time.LocalDateTime;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Session {
    private User user;
    private final String loginTime;

    public Session(User user) {
        this.user = user;
        this.loginTime = getCurrentTimestamp();
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

    private String getCurrentTimestamp() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return LocalDateTime.now().toString();
        } else {
            // Fallback for API levels below 26
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
            return sdf.format(new Date());
        }
    }
}