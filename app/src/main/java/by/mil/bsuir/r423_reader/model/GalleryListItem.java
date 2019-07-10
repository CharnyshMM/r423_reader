package by.mil.bsuir.r423_reader.model;

import android.os.Parcelable;

public abstract class GalleryListItem implements Parcelable {
    public static final int TYPE_HEADER = 0;
    public static final int TYPE_IMAGE = 1;

    abstract public int getType();
}
