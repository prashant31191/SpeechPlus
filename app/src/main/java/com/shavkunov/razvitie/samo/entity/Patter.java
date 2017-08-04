package com.shavkunov.razvitie.samo.entity;

public class Patter {
    private long id;
    private String imageUrl;
    private String title;
    private int isFavorite;

    public Long getId() {
        return id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public int getFavorite() {
        return isFavorite;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setFavorite(int favorite) {
        isFavorite = favorite;
    }
}
