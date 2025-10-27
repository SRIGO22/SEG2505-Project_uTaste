package com.example.utasteapplication;
/*
 * Author: Othmane El Moutaouakkil
 */

// Cette classe représente un ingrédient d'une recette
// Chaque ingrédient possède un nom, un code QR et une proportion dans la recette
public class RecipeIngredient {
    private int id;
    private int recipeId;
    private String qrCode;
    private String name; // Nom de l’ingrédient saisi par l’utilisateur
    private double quantityPercentage; // Pourcentage de cet ingrédient (entre 0 et 100)
    private final String addedAt; // Date à laquelle l’ingrédient a été ajouté (fixe une fois défini)

    // Méthode utilitaire pour nettoyer une chaîne de texte (supprime les espaces inutiles)
    private String cleanString(String str) {
        return str != null ? str.trim() : null;
    }

    // Constructeur utilisé pour un nouvel ingrédient avant l’insertion en base de données
    // La date sera ajoutée automatiquement par la base de données
    public RecipeIngredient(int recipeId, String qrCode, String name, double quantityPercentage) {
        this.recipeId = recipeId;
        // Improvement: Apply defensive trimming
        this.qrCode = cleanString(qrCode);
        this.name = cleanString(name);
        this.quantityPercentage = quantityPercentage; // The setter logic handles boundaries
        this.addedAt = null; // DB will set this
    }

    // Constructeur utilisé pour un ingrédient déjà existant (chargé depuis la base de données)
    public RecipeIngredient(int id, int recipeId, String qrCode, String name,
                            double quantityPercentage, String addedAt) {
        this.id = id;
        this.recipeId = recipeId;
        this.qrCode = cleanString(qrCode);
        this.name = cleanString(name);
        this.quantityPercentage = quantityPercentage;
        this.addedAt = addedAt; // Valeur déjà définie en base de données
    }

    // Getters
    public int getId() {
        return id;
    }

    public int getRecipeId() {
        return recipeId;
    }

    public String getQrCode() {
        return qrCode;
    }

    public String getName() {
        return name;
    }

    public double getQuantityPercentage() {
        return quantityPercentage;
    }

    public String getAddedAt() {
        return addedAt;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setRecipeId(int recipeId) {
        this.recipeId = recipeId;
    }

    public void setQrCode(String qrCode) {
        // Improvement: Apply defensive trimming
        this.qrCode = cleanString(qrCode);
    }

    /**
     * Fix: Corrected typo from setname to setName
     * Improvement: Apply defensive trimming
     */
    public void setName(String name) {
        this.name = cleanString(name);
    }

    // Définit la proportion de l’ingrédient en vérifiant que la valeur est dans les limites
    public void setQuantityPercentage(double quantityPercentage) {
        if (quantityPercentage < 0) {
            this.quantityPercentage = 0;
        } else if (quantityPercentage > 100) {
            this.quantityPercentage = 100;
        } else {
            this.quantityPercentage = quantityPercentage;
        }
    }

    // Vérifie si les données de l’ingrédient sont valides
    public boolean isValid() {
        if (name == null || name.isEmpty()) {
            return false;
        }
        if (qrCode == null || qrCode.isEmpty()) {
            return false;
        }
        if (quantityPercentage < 0 || quantityPercentage > 100) {
            return false;
        }
        if (recipeId <= 0) {
            return false;
        }
        return true;
    }

    // Retourne une courte description de l’ingrédient (utile pour les listes)
    @Override
    public String toString() {
        return name + " - " + String.format("%.1f", quantityPercentage) + "%";
    }

    // Retourne une version détaillée de l’ingrédient (utile pour le débogage)
    public String toDetailedString() {
        return "Ingredient{" +
                "id=" + id +
                ", recipeId=" + recipeId +
                ", name='" + name + '\'' +
                ", qrCode='" + qrCode + '\'' +
                ", quantityPercentage=" + quantityPercentage +
                "%, addedAt='" + addedAt + '\'' +
                '}';
    }

    // Crée une copie complète de cet ingrédient
    public RecipeIngredient copy() {
        return new RecipeIngredient(id, recipeId, qrCode, name, quantityPercentage, addedAt);
    }

    // Compare deux ingrédients selon leur code QR et leur recette associée
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        RecipeIngredient that = (RecipeIngredient) obj;

        if (recipeId != that.recipeId) return false;

        String thisQr = cleanString(qrCode);
        String thatQr = cleanString(that.qrCode);

        if (thisQr != null) {
            return thisQr.equals(thatQr);
        } else {
            return thatQr == null;
        }
    }

    // Génère un code de hachage basé sur le code QR et l’ID de la recette
    @Override
    public int hashCode() {
        int result = recipeId;
        String cleanedQrCode = cleanString(qrCode);
        if (cleanedQrCode != null) {
            result = 31 * result + cleanedQrCode.hashCode();
        }
        return result;
    }
}