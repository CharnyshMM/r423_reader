package com.example.mikita.r423_reader;

import android.os.Parcel;
import android.os.Parcelable;

public class GalleryHeaderItem extends GalleryListItem {

    private String title;

    public GalleryHeaderItem(String title){
        this.title = title;
    }

    public String getTitle(){
        return title;
    }

    @Override
    public int getType() {
        return TYPE_HEADER;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
    }

    public static final Parcelable.Creator<GalleryHeaderItem> CREATOR = new Parcelable.Creator<GalleryHeaderItem>() {

        @Override
        public GalleryHeaderItem createFromParcel(Parcel source) {
            String title = source.readString();
            return new GalleryHeaderItem(title);
        }

        @Override
        public GalleryHeaderItem[] newArray(int size) {
            return new GalleryHeaderItem[size];
        }
    };
}
