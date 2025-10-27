package com.example.utasteapplication;
/*
 * Auteur : Othmane El Moutaouakkil
 * Classe représentant une recette dans l’application.
 * Contient les informations essentielles : nom, image, description et dates.
 */
public class Recipe {
    private int id;
    private String name;
    private String imagePath; // Chemin vers l'image locale
    private String description;
    private final String createdAt;// Défini seulement à la création ou au chargement
    private String modifiedAt; // Peut changer lors d'une modification

    // Constructeur utilisé quand on crée une nouvelle recette (avant l’ajout à la base de données)
    public Recipe(String name, String imagePath, String description) {
        // Apply defensive trimming immediately
        this.name = name != null ? name.trim() : null;
        this.imagePath = imagePath != null ? imagePath.trim() : null;
        this.description = description != null ? description.trim() : null;
        this.createdAt = null; // DB will set this
        this.modifiedAt = null; // DB will set this
    }

    // Constructeur utilisé quand on charge une recette existante depuis la base
    public Recipe(int id, String name, String imagePath, String description,
                  String createdAt, String modifiedAt) {
        this.id = id;
        this.name = name != null ? name.trim() : null;
        this.imagePath = imagePath != null ? imagePath.trim() : null;
        this.description = description != null ? description.trim() : null;
        this.createdAt = createdAt; // La base de données remplira cette valeur
        this.modifiedAt = modifiedAt; // Idem
    }

    // Constructeur utilisé pour une recette déjà existante dans la base de données
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImagePath() {
        return imagePath;
    }

    public String getDescription() {
        return description;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getModifiedAt() {
        return modifiedAt;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        // On nettoie le texte pour éviter les doublons avec des espaces
        if (name != null) {
            this.name = name.trim();
        } else {
            this.name = null;
        }
    }

    public void setImagePath(String imagePath) {
        // On enlève les espaces inutiles s’il y en a
        if (imagePath != null) {
            this.imagePath = imagePath.trim();
        } else {
            this.imagePath = null;
        }
    }


    public void setDescription(String description) {
        // Même chose pour la description
        if (description != null) {
            this.description = description.trim();
        } else {
            this.description = null;
        }
    }

    // Met à jour la date de dernière modification
    public void setModifiedAt(String modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

    // Vérifie que la recette contient les infos essentielles
    public boolean isValid() {
        if (name == null || name.isEmpty()) {
            return false;
        }
        if (description == null || description.isEmpty()) {
            return false;
        }
        if (imagePath == null || imagePath.isEmpty()) {
            return false;
        }
        if (description.length() > 500) { // Longueur max d’exemple
            return false;
        }
        return true;
    }

    // Retourne juste le nom de la recette (utile pour les listes)
    @Override
    public String toString() {
        return name;
    }

    // Donne une version plus détaillée de la recette (utile pour le débogage)
    public String toDetailedString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Recipe: ").append(name).append("\n");
        sb.append("Description: ").append(description).append("\n");
        sb.append("Image: ").append(imagePath);
        if (createdAt != null) {
            sb.append("\nCreated: ").append(createdAt);
        }
        if (modifiedAt != null) {
            sb.append("\nModified: ").append(modifiedAt);
        }
        return sb.toString();
    }

    // Crée une copie complète de la recette actuelle
    public Recipe copy() {
        return new Recipe(id, name, imagePath, description, createdAt, modifiedAt);
    }

    // Vérifie si deux recettes sont identiques (même nom)
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Recipe recipe = (Recipe) obj;

        // Use equals on the name field
        return name != null ? name.equals(recipe.name) : recipe.name == null;
    }

    // Génère un code unique basé sur le nom de la recette
    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }
}