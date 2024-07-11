package com.example.krldailyandroidapp.model;

public class ViewAllModel {
    private String UID;
    String ArticleImage;
    String ArticleTitle;
    String ArticleCategory;
    String CreatedAt;

    public ViewAllModel() {
    }

    public ViewAllModel(String UID, String articleImage, String articleTitle, String articleCategory, String createdAt) {
        this.UID = UID;
        ArticleImage = articleImage;
        ArticleTitle = articleTitle;
        ArticleCategory = articleCategory;
        CreatedAt = createdAt;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public String getArticleImage() {
        return ArticleImage;
    }

    public void setArticleImage(String articleImage) {
        ArticleImage = articleImage;
    }

    public String getArticleTitle() {
        return ArticleTitle;
    }

    public void setArticleTitle(String articleTitle) {
        ArticleTitle = articleTitle;
    }

    public String getArticleCategory() {
        return ArticleCategory;
    }

    public void setArticleCategory(String articleCategory) {
        ArticleCategory = articleCategory;
    }

    public String getCreatedAt() {
        return CreatedAt;
    }

    public void setCreatedAt(String createdAt) {
        CreatedAt = createdAt;
    }
}
