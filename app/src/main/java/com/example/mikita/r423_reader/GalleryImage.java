package com.example.mikita.r423_reader;

import android.graphics.drawable.Drawable;

public class GalleryImage {
    protected String ImageUrl;

    public Drawable getDrawableImage() {
        return drawableImage;
    }

    public void setDrawableImage(Drawable drawableImage) {
        this.drawableImage = drawableImage;
    }

    protected Drawable drawableImage;

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }

    public String getTextDescription() {
        return TextDescription;
    }

    public void setTextDescription(String textDescription) {
        TextDescription = textDescription;
    }

    protected String TextDescription;

    public GalleryImage() {
        this.ImageUrl = null;
        this.TextDescription = null;
        this.drawableImage = null;
    }

    public  GalleryImage(String imageUrl, String textDescription) {
        this.ImageUrl = imageUrl;
        this.TextDescription = textDescription;
    }
}
