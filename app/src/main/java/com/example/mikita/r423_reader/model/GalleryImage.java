package com.example.mikita.r423_reader.model;

import android.os.Parcel;
import android.os.Parcelable;

public class GalleryImage implements Parcelable {
    protected String imageUrl;
    public String imageName;

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }



    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }


    public GalleryImage() {
        this.imageUrl = null;
        this.imageName = null;
    }

    public  GalleryImage(String imageUrl, String imageName) {
        this.imageUrl = imageUrl;
        this.imageName = imageName;
    }

    public GalleryImage(Parcel source) {
        imageUrl = source.readString();
        imageName = source.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(imageUrl);
        dest.writeString(imageName);
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
