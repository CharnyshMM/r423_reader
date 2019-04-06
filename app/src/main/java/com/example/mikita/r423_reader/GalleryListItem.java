package com.example.mikita.r423_reader;

import android.os.Parcelable;

public abstract class GalleryListItem implements Parcelable {
    public static final int TYPE_HEADER = 0;
    public static final int TYPE_IMAGE = 1;

    abstract public int getType();
}
