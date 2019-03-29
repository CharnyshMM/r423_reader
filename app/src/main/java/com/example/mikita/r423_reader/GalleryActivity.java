package com.example.mikita.r423_reader;

import android.graphics.drawable.Drawable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class GalleryActivity extends AppCompatActivity {

    private static final String ANDROID_ASSET_PATH_START = "file:///android_asset";
    private static final String IMAGES_ASSET_PATH = "file:///android_asset/images";
    private RecyclerView mRecyclerView;
    private GalleryAdapter mAdapter;

    /*for each gallery item in folder assets/images
    * there must be an EQUALLY named .jpg file and .json decription_file:
    * Example:
    *   р423_кунг.jpg
    *   р423_кунг.txt
    *   */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        List<GalleryImage> data = new ArrayList<GalleryImage>();
        GalleryImage im1 = new GalleryImage();
        im1.TextDescription = "OOO";

        try {
            InputStream stream = getAssets().open("images/Р-423-2.jpg");
            im1.setDrawableImage(Drawable.createFromStream(stream, null));
        } catch (IOException e) {
            e.printStackTrace();
        }


        data.add(im1);
        data.add(im1);
        data.add(im1);
        data.add(im1);



        mRecyclerView = (RecyclerView) findViewById(R.id.gallery__images_recyclerView);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 3));
        mRecyclerView.setHasFixedSize(true); // Helps improve performance

        GalleryAdapter.OnItemClickListener onItemClickListener = new GalleryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(GalleryImage item) {

            }
        };
        mAdapter = new GalleryAdapter(GalleryActivity.this, data, onItemClickListener);
        mRecyclerView.setAdapter(mAdapter);
    }
}
