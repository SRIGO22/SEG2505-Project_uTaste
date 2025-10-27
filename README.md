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

Ce projet vise à développer des compétences en génie logiciel : modélisation, conception, programmation, tests, documentation et travail en équipe.\

### Supported Roles
- **Administrator**: Manages users, resets passwords, and resets the database.
- **Chef**: Creates and manages recipes and ingredients.
- **Waiter**: Views recipes and records sales (to be implemented in Deliverable 3).

## Setup & Reconstruction Instructions

### Requirements
- Android Studio (latest stable version)
- Minimum SDK: API 26 (Android 8.0 Oreo)
- SQLite (bundled with Android)
- Emulator or physical Android device

### How to Run
1. Clone the repository and checkout the tag `deliverable-2`.
2. Open the project in Android Studio.
3. Build and run the app on an emulator or device.
4. Log in using one of the default accounts:
   - Administrator: `admin@utaste.com` / `admin123`
   - Chef: `chef@utaste.com` / `chef123`
   - Waiter: `waiter@utaste.com` / `waiter123`

---

## Validation Scenario

### Administrator
- Log in and access the admin dashboard.
- Add a new Waiter with a valid email and password.
- Reset any user's password to default.
- Edit a user's profile (first name, last name).
- Reset the database to its initial state.

### Chef
- Log in and access the chef dashboard.
- Create a new recipe with:
  - Unique name
  - Image from local bank
  - Description
- Add ingredients by:
  - Scanning a QR code
  - Entering a title and quantity percentage
- Modify or delete ingredients.
- Modify or delete recipes.

---

## Known Limitations

- QR code scanning is mocked; full camera integration is pending.
- Nutrition API integration (OpenFoodFacts) is planned for Deliverable 3.
- 

---


