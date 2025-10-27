# SEG2505-Project_uTaste

## Groupe
Sara Rigotti # 300226854  
Radwan Abdurashed # 300418882  
Othmane Moutaouakkil # 300422326  
Marven Monalliance # 300401373

## Description
Le projet «uTaste» consiste à concevoir et développer une application Android en Java, destinée à un restaurant gastronomique. Elle permet à différents utilisateurs (administrateur, chef cuisinier, serveur) de gérer des recettes, consulter des informations nutritionnelles, enregistrer des ventes et noter les plats.  
L’application doit :
- intégrer des fonctionnalités spécifiques à chaque rôle utilisateur ;
- respecter une architecture logicielle modélisée en UML ;
- être livrée en plusieurs étapes avec démonstration finale.

Ce projet vise à développer des compétences en génie logiciel : modélisation, conception, programmation, tests, documentation et travail en équipe.

### Rôles pris en charge
- **Administrateur** : Gère les utilisateurs, réinitialise les mots de passe et la base de données.
- **Chef** : Crée et gère les recettes et les ingrédients.
- **Serveur** : Consulte les recettes et enregistre les ventes (à implémenter dans le livrable 3).

---

## Instructions d’installation et de reconstruction

### Prérequis
- Android Studio (version stable la plus récente)
- SDK minimum : API 26 (Android 8.0 Oreo)
- SQLite (intégré à Android)
- Émulateur ou appareil Android physique

### Comment exécuter l’application
1. Cloner le dépôt et se positionner sur le tag `deliverable-2`.
2. Ouvrir le projet dans Android Studio.
3. Compiler et exécuter l’application sur un émulateur ou un appareil.
4. Se connecter avec l’un des comptes par défaut :
    - Administrateur : `admin@utaste.com` / `admin123`
    - Chef : `chef@utaste.com` / `chef123`
    - Serveur : `waiter@utaste.com` / `waiter123`

---

## Scénario de validation

### Administrateur
- Se connecter et accéder au tableau de bord administrateur.
- Ajouter un nouveau serveur avec un email et mot de passe valides.
- Réinitialiser le mot de passe de n’importe quel utilisateur.
- Modifier le profil d’un utilisateur (prénom, nom).
- Réinitialiser la base de données à son état initial.

### Chef
- Se connecter et accéder au tableau de bord chef.
- Créer une nouvelle recette avec :
    - Nom unique
    - Image depuis la banque locale
    - Description
- Ajouter des ingrédients via :
    - Scan d’un code QR
    - Saisie d’un titre et d’un pourcentage de quantité
- Modifier ou supprimer des ingrédients.
- Modifier ou supprimer des recettes.

---

## Limitations connues

- Le scan de code QR est simulé ; l’intégration complète de la caméra est prévue.
- L’intégration de l’API nutritionnelle (OpenFoodFacts) est prévue pour le livrable 3.
- **Problème avec les images de recettes** :
    - Les images ajoutées aux recettes ne s’affichent pas.
    - J’ai essayé :
        - D’ajouter les images localement.
        - D’utiliser des URLs externes.
        - D’intégrer des bibliothèques de logging pour diagnostiquer le problème.
    - Ce n’est pas un problème de layout.
    - Pour le moment, j’ai décidé de laisser cette limitation telle quelle.

---
