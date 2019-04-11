package com.example.mikita.r423_reader;

import android.os.Parcel;
import android.os.Parcelable;

public class GalleryImageItem extends GalleryListItem {

    private GalleryImage image;

    public GalleryImageItem(GalleryImage image){
        this.image = image;
    }

    public GalleryImage getImage(){
        return image;
    }

    @Override
    public int getType() {
        return TYPE_IMAGE;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(image.imageUrl);
        dest.writeString(image.imageName);
    }

    public static final Parcelable.Creator<GalleryImageItem> CREATOR = new Parcelable.Creator<GalleryImageItem>() {

        @Override
        public GalleryImageItem createFromParcel(Parcel source) {
            return new GalleryImageItem(new GalleryImage(source));
        }

        @Override
        public GalleryImageItem[] newArray(int size) {
            return new GalleryImageItem[size];
        }
    };
}
