package com.example.krldailyandroidapp.model;

public class RecentModel {
    private String UID; // Add UID field
    private String ArticleTitle;
    private String ArticleCategory;
    private String ArticleImage;
    private String ArticleContent;
    private String CreatedAt;

    public RecentModel() {
    }

    public RecentModel(String UID, String articleTitle, String articleCategory, String articleImage, String articleContent, String createdAt) {
        this.UID = UID;
        this.ArticleTitle = articleTitle;
        this.ArticleCategory = articleCategory;
        this.ArticleImage = articleImage;
        this.ArticleContent = articleContent;
        this.CreatedAt = createdAt;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public String getArticleTitle() {
        return ArticleTitle;
    }

    public void setArticleTitle(String articleTitle) {
        this.ArticleTitle = articleTitle;
    }

    public String getArticleCategory() {
        return ArticleCategory;
    }

    public void setArticleCategory(String articleCategory) {
        this.ArticleCategory = articleCategory;
    }

    public String getArticleImage() {
        return ArticleImage;
    }

    public void setArticleImage(String articleImage) {
        this.ArticleImage = articleImage;
    }

    public String getArticleContent() {
        return ArticleContent;
    }

    public void setArticleContent(String articleContent) {
        this.ArticleContent = articleContent;
    }

    public String getCreatedAt() {
        return CreatedAt;
    }

    public void setCreatedAt(String createdAt) {
        CreatedAt = createdAt;
    }
}

