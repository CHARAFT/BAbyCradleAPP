package com.example.babycradleapp.model;

public class Article {
    private String title;
    private String description;
    private String imageUrl;
    private boolean isFavorite;
    private String linkUrl;

    // Constructeur par défaut requis par Firebase
    public Article() {
    }

    // Constructeur, getters et setters
    public Article(String title, String description, String imageUrl, boolean isFavorite) {
        this.title = title;
        this.description = description;
        this.imageUrl = imageUrl;
        this.isFavorite = isFavorite;
    }
    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }
    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }
}
