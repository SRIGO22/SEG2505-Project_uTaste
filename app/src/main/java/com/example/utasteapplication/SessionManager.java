package com.example.utasteapplication;

/**
 * Author: Sara Rigotti & Othmane El Moutaouakkil
 * SessionManager - Singleton to manage current user session
 */

/**
 * Classe SessionManager - Gestionnaire de session unique pour toute l'application
 * Utilise le pattern Singleton pour garantir qu'une seule instance existe
 * Gère la session de l'utilisateur actuellement connecté
 */
public class SessionManager {
    // L'unique instance du SessionManager (pattern Singleton)
    private static SessionManager instance;

    // La session active de l'utilisateur actuellement connecté
    private Session currentSession;

    /**
     * Constructeur privé pour empêcher l'instanciation directe
     * Force l'utilisation de getInstance() pour obtenir l'instance unique
     */
    private SessionManager() {
    }

    /**
     * Méthode pour obtenir l'instance unique du SessionManager (pattern Singleton)
     * Synchronized pour assurer la sécurité en cas d'accès concurrent (thread-safe)
     *
     * @return L'instance unique du SessionManager
     */
    public static synchronized SessionManager getInstance() {
        // Vérifie si l'instance n'existe pas encore (lazy initialization)
        if (instance == null) {
            // Crée l'instance unique lors du premier appel
            instance = new SessionManager();
        }
        // Retourne toujours la même instance
        return instance;
    }

    /**
     * Crée une nouvelle session pour un utilisateur
     * Appelée lors de la connexion réussie d'un utilisateur
     *
     * @param user L'utilisateur pour lequel créer une session
     */
    public void createSession(User user) {
        // Crée un nouvel objet Session avec l'utilisateur connecté
        this.currentSession = new Session(user);
    }

    /**
     * Récupère la session actuelle
     *
     * @return L'objet Session actuel, ou null si aucune session n'est active
     */
    public Session getCurrentSession() {
        return currentSession;
    }

    /**
     * Récupère l'utilisateur actuellement connecté
     * Méthode de commodité pour accéder directement à l'utilisateur sans passer par la session
     *
     * @return L'objet User connecté, ou null si aucun utilisateur n'est connecté
     */
    public User getCurrentUser() {
        // Vérifie d'abord si une session existe
        if (currentSession != null) {
            // Retourne l'utilisateur de la session active
            return currentSession.getUser();
        }
        // Retourne null si aucune session n'existe
        return null;
    }

    /**
     * Vérifie si un utilisateur est actuellement connecté
     * Utile pour vérifier l'état de connexion avant d'autoriser certaines actions
     *
     * @return true si un utilisateur est connecté, false sinon
     */
    public boolean isLoggedIn() {
        // Vérifie que la session existe ET que l'utilisateur de la session n'est pas null
        return currentSession != null && currentSession.getUser() != null;
    }

    /**
     * Déconnecte l'utilisateur actuel et termine la session
     * Appelée lorsque l'utilisateur clique sur le bouton de déconnexion
     */
    public void logout() {
        // Vérifie d'abord si une session existe
        if (currentSession != null) {
            // Déconnecte l'utilisateur dans la session
            currentSession.logout();
            // Supprime complètement la session
            currentSession = null;
        }
    }
}