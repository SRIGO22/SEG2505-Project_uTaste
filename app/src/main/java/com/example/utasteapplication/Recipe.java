/*
* Author: Sara Rigotti
*/

public class Recipe {
    public int id;
    public String name;
    public String imagePath;
    public String description;

    public Recipe(String name, String imagePath, String description) {
        this.name = name;
        this.imagePath = imagePath;
        this.description = description;
    }
}
