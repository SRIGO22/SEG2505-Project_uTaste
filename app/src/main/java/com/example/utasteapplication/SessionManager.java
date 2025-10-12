package com.example.utasteapplication;

/**
 * Author: Sara Rigotti & Othmane El Moutaouakkil
 * SessionManager - Singleton to manage current user session
 */

public class SessionManager {
    private static SessionManager instance;
    private Session currentSession;

    // Private constructor to prevent instantiation
    private SessionManager() {
    }

    // Singleton getInstance method
    public static synchronized SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    public void createSession(User user) {
        this.currentSession = new Session(user);
    }

    public Session getCurrentSession() {
        return currentSession;
    }

    public User getCurrentUser() {
        if (currentSession != null) {
            return currentSession.getUser();
        }
        return null;
    }

    public boolean isLoggedIn() {
        return currentSession != null && currentSession.getUser() != null;
    }

    public void logout() {
        if (currentSession != null) {
            currentSession.logout();
            currentSession = null;
        }
    }
}