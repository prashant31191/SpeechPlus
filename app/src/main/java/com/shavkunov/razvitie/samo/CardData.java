package com.shavkunov.razvitie.samo;

public class CardData {

    private int imageResId;
    private String title;
    private boolean isClick;

    public CardData(int imageResId, String title, boolean isClick) {
        this.imageResId = imageResId;
        this.title = title;
        this.isClick = isClick;
    }

    public boolean isClick() {
        return isClick;
    }

    public void setClick(boolean click) {
        isClick = click;
    }

    public int getImageResId() {
        return imageResId;
    }

    public void setImageResId(int imageResId) {
        this.imageResId = imageResId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
