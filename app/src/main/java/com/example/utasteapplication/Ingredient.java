/* 
* Author: Sara Rigotti
*/ 

public class Ingredient {
    public int id;
    public int recipeId;
    public String title;
    public String qrCode;
    public double quantityPercent;

    public Ingredient(int recipeId, String title, String qrCode, double quantityPercent) {
        this.recipeId = recipeId;
        this.title = title;
        this.qrCode = qrCode;
        this.quantityPercent = quantityPercent;
    }
}
