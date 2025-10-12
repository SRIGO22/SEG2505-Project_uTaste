package com.example.utasteapplication;

/*
 * Author: Sara Rigotti
 */

import android.os.Build;
import java.time.LocalDateTime;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Classe Session - Représente une session utilisateur active
 * Garde en mémoire l'utilisateur connecté et l'heure de connexion
 */
public class Session {
    // Déclaration des attributs de la session
    private User user; // L'utilisateur actuellement connecté dans cette session
    private final String loginTime; // L'heure et la date de connexion (immuable une fois définie)

    /**
     * Constructeur de la classe Session
     * Crée une nouvelle session pour un utilisateur et enregistre l'heure de connexion
     *
     * @param user L'utilisateur pour lequel créer une session
     */
    public Session(User user) {
        this.user = user; // Associe l'utilisateur à cette session
        this.loginTime = getCurrentTimestamp(); // Enregistre l'heure actuelle comme heure de connexion
    }

    /**
     * Méthode pour déconnecter l'utilisateur
     * Met l'utilisateur à null pour indiquer qu'il n'y a plus de session active
     */
    public void logout() {
        this.user = null; // Supprime la référence à l'utilisateur pour terminer la session
    }

    /**
     * Récupère l'utilisateur actuellement connecté dans cette session
     *
     * @return L'objet User connecté, ou null si aucune session n'est active
     */
    public User getUser() {
        return user;
    }

    /**
     * Récupère l'heure de connexion de cette session
     *
     * @return L'heure de connexion sous forme de chaîne de caractères
     */
    public String getLoginTime() {
        return loginTime;
    }

    /**
     * Méthode privée pour obtenir l'heure et la date actuelles sous forme de chaîne
     * Utilise différentes méthodes selon la version d'Android pour assurer la compatibilité
     *
     * @return L'horodatage actuel au format ISO 8601 (yyyy-MM-dd'T'HH:mm:ss)
     */
    private String getCurrentTimestamp() {
        // Vérifie si la version d'Android est 26 (Android 8.0 Oreo) ou supérieure
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Utilise l'API moderne LocalDateTime pour Android 8.0+
            return LocalDateTime.now().toString();
        } else {
            // Méthode de secours pour les versions Android antérieures à 8.0
            // Crée un formateur de date avec le format ISO 8601
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
            // Retourne la date actuelle formatée
            return sdf.format(new Date());
        }
    }
}