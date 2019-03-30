package com.example.mikita.r423_reader;

import android.os.Parcel;
import android.os.Parcelable;

public class GalleryImage implements Parcelable {
    protected String imageUrl;
    protected String textDescription;

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTextDescription() {
        return textDescription;
    }

    public void setTextDescription(String textDescription) {
        this.textDescription = textDescription;
    }

    public GalleryImage() {
        this.imageUrl = null;
        this.textDescription = null;
    }

    public  GalleryImage(String imageUrl, String textDescription) {
        this.imageUrl = imageUrl;
        this.textDescription = textDescription;
    }

    private GalleryImage(Parcel source) {
        imageUrl = source.readString();
        textDescription = source.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(imageUrl);
        dest.writeString(textDescription);
    }

    public static final Parcelable.Creator<GalleryImage> CREATOR = new Parcelable.Creator<GalleryImage>() {

        @Override
        public GalleryImage createFromParcel(Parcel source) {
            return new GalleryImage(source);
        }

        @Override
        public GalleryImage[] newArray(int size) {
            return new GalleryImage[size];
        }
    };

}
