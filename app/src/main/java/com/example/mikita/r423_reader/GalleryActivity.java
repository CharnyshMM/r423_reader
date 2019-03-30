package com.example.mikita.r423_reader;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GalleryActivity extends AppCompatActivity {

    private static final String ANDROID_ASSET_PATH_START = "file:///android_asset";
    private static final String IMAGES_ASSET_PATH = "file:///android_asset/gallery";
    private RecyclerView mRecyclerView;
    private GalleryAdapter mAdapter;

    /*for each gallery item in folder assets/gallery
    * there must be an EQUALLY named .jpg file and .json decription_file:
    * Example:
    *   р423_кунг.jpg
    *   р423_кунг.txt
    *   */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        final ArrayList<GalleryImage> data = new ArrayList<GalleryImage>();
        setTitle(R.string.gallery);
        try {
            String[] images = getAssets().list("gallery");
            for (String image:images) {
                data.add(new GalleryImage("gallery/"+image, image));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        mRecyclerView = (RecyclerView) findViewById(R.id.gallery__images_recyclerView);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 3));
        mRecyclerView.setHasFixedSize(true); // Helps improve performance

        GalleryAdapter.OnItemClickListener onItemClickListener = new GalleryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int  position) {
                Intent intent = new Intent(getApplicationContext(), GalleryDetailActivity.class);
                intent.putParcelableArrayListExtra("data", data);
                intent.putExtra("position", position);
                startActivity(intent);
            }
        };
        mAdapter = new GalleryAdapter(GalleryActivity.this, data, onItemClickListener);
        mRecyclerView.setAdapter(mAdapter);
    }
}
