package com.example.ktsitsa;

public class Recipes {
    private String recipeName;
    private String recipeDescription;
    private String recipeIngredients;

    private String recipeImage;
    private String key;
    private String uid;
    private boolean approved;
    private boolean recommended;

    public String getKey() {
        return key;
    }

    public Recipes(){}
    //This class represents Recepie by his fields.
    public Recipes(String recipeName, String recipeDescription, String recipeIngredients, String recipeImage, String Key, String uid) {
        this.recipeName = recipeName;
        this.recipeDescription = recipeDescription;
        this.recipeImage = recipeImage;
        this.recipeIngredients = recipeIngredients;
        this.key = Key;
        this.approved = false;
        this.uid = uid;
        this.recommended = false;
    }

    public boolean isApproved(){
        return approved;
    }

    public boolean isRecommended(){
        return recommended;
    }

    public String getUid() {
        return uid;
    }


    public String getRecipeIngredients() {
        return recipeIngredients;
    }

    public void setRecipeIngredients(String recipeIngredients) {
        this.recipeIngredients = recipeIngredients;
    }

    public String getRecipeName() {
        return recipeName;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    public String getRecipeDescription() {
        return recipeDescription;
    }

    public void setRecipeDescription(String recipeDescription) {
        this.recipeDescription = recipeDescription;
    }

    public String getRecipeImage() {
        return recipeImage;
    }

    public void setRecipeImage(String recipeImage) {
        this.recipeImage = recipeImage;
    }


}
