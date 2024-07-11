package com.example.krldailyandroidapp.model;

public class RandomModel {

    private String UID; // Add UID field
    private String ArticleTitle;
    private String ArticleCategory;
    private String ArticleContent;
    private String ArticleImage;
    private String CreatedAt;

    public RandomModel() {
    }

    public RandomModel(String UID, String articleTitle, String articleCategory, String articleContent, String articleImage, String createdAt) {
        this.UID = UID;
        this.ArticleTitle = articleTitle;
        this.ArticleCategory = articleCategory;
        this.ArticleContent = articleContent;
        this.ArticleImage = articleImage;
        this.CreatedAt = createdAt;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public String getRecipeTitle() {
        return ArticleTitle;
    }

    public void setRecipeTitle(String recipeTitle) {
        this.ArticleTitle = recipeTitle;
    }

    public String getArticleCategory() {
        return ArticleCategory;
    }

    public void setArticleCategory(String articleCategory) {
        this.ArticleCategory = articleCategory;
    }

    public String getArticleContent() {
        return ArticleContent;
    }

    public void setArticleContent(String articleContent) {
        this.ArticleContent = articleContent;
    }

    public String getArticleImage() {
        return ArticleImage;
    }

    public void setArticleImage(String articleImage) {
        this.ArticleImage = articleImage;
    }

    public String getArticleTitle() {
        return ArticleTitle;
    }

    public void setArticleTitle(String articleTitle) {
        ArticleTitle = articleTitle;
    }

    public String getCreatedAt() {
        return CreatedAt;
    }

    public void setCreatedAt(String createdAt) {
        CreatedAt = createdAt;
    }
}
