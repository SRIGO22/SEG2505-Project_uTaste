package com.example.utasteapplication;
/*
 * Author: Othmane El Moutaouakkil
 */

/**
 * Recipe Model Class
 * Represents a recipe with unique name, image, and description
 */
public class Recipe {
    private int id;
    private String name;
    private String imagePath; // Path to local image resource
    private String description;
    // Timestamps are final to ensure they are set only on creation/loading
    private final String createdAt;
    private String modifiedAt; // Not final, as it changes on modification

    /**
     * Constructor for new recipe (before database insertion)
     * Timestamps will be null and set by the database
     * @param name Unique recipe name
     * @param imagePath Path to image resource (e.g., "recipe_image_1")
     * @param description Recipe description
     */
    public Recipe(String name, String imagePath, String description) {
        // Apply defensive trimming immediately
        this.name = name != null ? name.trim() : null;
        this.imagePath = imagePath != null ? imagePath.trim() : null;
        this.description = description != null ? description.trim() : null;
        this.createdAt = null; // DB will set this
        this.modifiedAt = null; // DB will set this
    }

    /**
     * Constructor for existing recipe (from database)
     * @param id Database ID
     * @param name Recipe name
     * @param imagePath Path to image resource
     * @param description Recipe description
     * @param createdAt Creation timestamp
     * @param modifiedAt Last modification timestamp
     */
    public Recipe(int id, String name, String imagePath, String description,
                  String createdAt, String modifiedAt) {
        this.id = id;
        this.name = name != null ? name.trim() : null;
        this.imagePath = imagePath != null ? imagePath.trim() : null;
        this.description = description != null ? description.trim() : null;
        // Timestamps loaded from DB are set as final
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }

    // Getters
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

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        // Defensive trimming to prevent "Pizza" vs "  Pizza " duplicates
        this.name = name != null ? name.trim() : null;
    }

    public void setImagePath(String imagePath) {
        // Defensive trimming
        this.imagePath = imagePath != null ? imagePath.trim() : null;
    }

    public void setDescription(String description) {
        // Defensive trimming
        this.description = description != null ? description.trim() : null;
    }

    /**
     * Set modifiedAt, used by the DatabaseHelper when updating a record.
     * @param modifiedAt Last modification timestamp
     */
    public void setModifiedAt(String modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

    // Removed setCreatedAt because it is managed as a 'final' field or by the database insert logic.
    // Removed setId because it should only be set by the DB upon insertion (or in the DB constructor).

    /**
     * Validates the recipe data
     * @return true if recipe is valid, false otherwise
     */
    public boolean isValid() {
        if (name == null || name.isEmpty()) { // .trim() is not needed here if setName() is used
            return false;
        }
        if (description == null || description.isEmpty()) {
            return false;
        }
        if (imagePath == null || imagePath.isEmpty()) {
            return false;
        }
        // Optional UX check: limit description length
        if (description.length() > 500) { // Example limit
            return false;
        }
        return true;
    }

    /**
     * Returns the recipe name for display in lists
     * @return Recipe name
     */
    @Override
    public String toString() {
        return name;
    }

    /**
     * Returns detailed information about the recipe
     * @return Detailed string representation
     */
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

    /**
     * Creates a copy of this recipe
     * @return A new Recipe with the same values
     */
    public Recipe copy() {
        return new Recipe(id, name, imagePath, description, createdAt, modifiedAt);
    }

    /**
     * Compare two recipes for equality based on name (unique constraint)
     * @param obj Object to compare
     * @return true if recipes have the same name
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Recipe recipe = (Recipe) obj;

        // Use equals on the name field
        return name != null ? name.equals(recipe.name) : recipe.name == null;
    }

    /**
     * Generate hash code based on recipe name
     * @return hash code
     */
    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }
}