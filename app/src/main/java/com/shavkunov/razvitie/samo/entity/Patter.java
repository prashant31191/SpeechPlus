package com.shavkunov.razvitie.samo.entity;

public class Patter {

    private int id;
    private String imageUrl;
    private String title;
    private boolean isFavorite;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }
}
