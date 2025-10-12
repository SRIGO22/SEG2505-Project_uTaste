package com.example.utasteapplication;

/*
 * Author: Sara Rigotti
 * Updated to Singleton pattern for persistent user management
 */

import java.util.ArrayList;
import java.util.List;

/**
 * Classe UserManager - Gestionnaire centralisé de tous les utilisateurs de l'application
 * Utilise le pattern Singleton pour garantir une seule instance et une gestion cohérente
 * Gère la création, suppression, recherche et stockage de tous les utilisateurs
 */
public class UserManager {
    // L'unique instance du UserManager (pattern Singleton)
    private static UserManager instance;

    // Liste contenant tous les utilisateurs du système (Administrator, Chef, Waiter)
    private List<User> users = new ArrayList<>();

    /**
     * Constructeur privé pour empêcher l'instanciation directe
     * Force l'utilisation de getInstance() pour obtenir l'instance unique
     * Initialise automatiquement les utilisateurs par défaut
     */
    private UserManager() {
        // Crée les utilisateurs par défaut lors de la première instanciation
        initializeDefaultUsers();
    }

    /**
     * Méthode pour obtenir l'instance unique du UserManager (pattern Singleton)
     * Synchronized pour assurer la sécurité en cas d'accès concurrent (thread-safe)
     *
     * @return L'instance unique du UserManager
     */
    public static synchronized UserManager getInstance() {
        // Vérifie si l'instance n'existe pas encore (lazy initialization)
        if (instance == null) {
            // Crée l'instance unique lors du premier appel
            instance = new UserManager();
        }
        // Retourne toujours la même instance
        return instance;
    }

    /**
     * Méthode privée pour initialiser les utilisateurs par défaut du système
     * Crée automatiquement un administrateur, un chef et un serveur prédéfinis
     * Appelée uniquement lors de la première création du UserManager
     */
    private void initializeDefaultUsers() {
        // Crée l'administrateur par défaut avec email et mot de passe prédéfinis
        Administrator admin = new Administrator("admin@utaste.com", "admin123");
        // Configure le profil de l'administrateur (prénom, nom, email)
        admin.updateProfile("Admin", "User", "admin@utaste.com");
        // Ajoute l'administrateur à la liste des utilisateurs
        users.add(admin);

        // Crée le chef par défaut avec email et mot de passe prédéfinis
        Chef chef = new Chef("chef@utaste.com", "chef123");
        // Configure le profil du chef (prénom, nom, email)
        chef.updateProfile("Chef", "User", "chef@utaste.com");
        // Ajoute le chef à la liste des utilisateurs
        users.add(chef);

        // Crée le serveur par défaut avec email et mot de passe prédéfinis
        Waiter waiter = new Waiter("waiter@utaste.com", "waiter123");
        // Configure le profil du serveur (prénom, nom, email)
        waiter.updateProfile("Waiter", "User", "waiter@utaste.com");
        // Ajoute le serveur à la liste des utilisateurs
        users.add(waiter);
    }

    /**
     * Méthode pour ajouter un nouvel utilisateur au système
     * Utilisée principalement pour ajouter de nouveaux serveurs
     *
     * @param u L'objet User à ajouter (peut être Administrator, Chef ou Waiter)
     */
    public void addUser(User u) {
        // Ajoute l'utilisateur à la liste
        users.add(u);
    }

    /**
     * Méthode pour supprimer un utilisateur du système en utilisant son email
     * Utilise removeIf pour supprimer l'utilisateur correspondant
     *
     * @param email L'adresse email de l'utilisateur à supprimer
     */
    public void removeUser(String email) {
        // Parcourt la liste et supprime l'utilisateur dont l'email correspond
        // removeIf utilise une expression lambda pour définir la condition de suppression
        users.removeIf(u -> u.getEmail().equals(email));
    }

    /**
     * Méthode pour rechercher un utilisateur spécifique par son email
     * Utilisée lors de l'authentification pour vérifier si un utilisateur existe
     *
     * @param email L'adresse email de l'utilisateur recherché
     * @return L'objet User correspondant, ou null si aucun utilisateur n'est trouvé
     */
    public User findUser(String email) {
        // Parcourt tous les utilisateurs de la liste
        for (User u : users) {
            // Vérifie si l'email de l'utilisateur actuel correspond à l'email recherché
            if (u.getEmail().equals(email)) {
                // Retourne l'utilisateur trouvé
                return u;
            }
        }
        // Retourne null si aucun utilisateur avec cet email n'existe
        return null;
    }

    /**
     * Méthode pour récupérer la liste complète de tous les utilisateurs
     * Utile pour l'administration ou les statistiques
     *
     * @return La liste de tous les utilisateurs du système
     */
    public List<User> getAllUsers() {
        return users;
    }

    /**
     * Méthode pour récupérer uniquement la liste des serveurs
     * Filtre les utilisateurs pour ne retourner que ceux avec le rôle "Waiter"
     * Utile pour l'administrateur qui gère spécifiquement les serveurs
     *
     * @return Une nouvelle liste contenant uniquement les objets Waiter
     */
    public List<Waiter> getAllWaiters() {
        // Crée une nouvelle liste vide pour stocker les serveurs
        List<Waiter> waiters = new ArrayList<>();
        // Parcourt tous les utilisateurs
        for (User u : users) {
            // Vérifie si l'utilisateur est une instance de la classe Waiter
            if (u instanceof Waiter) {
                // Cast l'utilisateur en Waiter et l'ajoute à la liste
                waiters.add((Waiter) u);
            }
        }
        // Retourne la liste filtrée contenant uniquement les serveurs
        return waiters;
    }
}