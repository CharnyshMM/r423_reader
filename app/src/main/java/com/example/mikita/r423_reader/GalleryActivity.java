package com.example.mikita.r423_reader;

import android.content.Intent;
import android.content.res.AssetManager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

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

        setTitle(R.string.gallery);

        final ArrayList<GalleryListItem> data = new ArrayList<>();
        try{
            String[] paths = getAssets().list("gallery");
            for (String path: paths){
                boolean hasExtension = path.contains(".");
                if (!hasExtension){

                    String[] images = getAssets().list("gallery/" + path);
                    if (images.length != 0){
                        data.add(new GalleryHeaderItem(path));
                        for (String img: images){
                            data.add(new GalleryImageItem(new GalleryImage("gallery/" + path + "/" + img, img)));
                        }
                    }
                } else {
                    data.add(new GalleryImageItem(new GalleryImage("gallery/" + path, path)));
                }
            }

        } catch (IOException e){
            e.printStackTrace();
        }

        mRecyclerView = (RecyclerView) findViewById(R.id.gallery__images_recyclerView);
        GridLayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 3);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                switch (data.get(position).getType()){
                    case GalleryListItem.TYPE_HEADER: {
                        return 3;
                    }
                    case GalleryListItem.TYPE_IMAGE: {
                        return 1;
                    }
                    default:
                        throw new IllegalStateException("unsupported item type");
                }
            }
        });

        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true); // Helps improve performance

        GalleryAdapter.OnItemClickListener onItemClickListener = new GalleryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int  position) {
                Intent intent = new Intent(getApplicationContext(), FullscreenGalleryDetailActivity.class);
                ArrayList<GalleryImage> images = new ArrayList<>();
                for (GalleryListItem item: data){
                    if (item instanceof GalleryImageItem){
                        images.add(((GalleryImageItem) item).getImage());
                    }
                }

                intent.putParcelableArrayListExtra("data", images);
                // Intent intent = new Intent(getApplicationContext(), FullscreenGalleryDetailActivity.class);
                // intent.putParcelableArrayListExtra("data", data);
                intent.putExtra("position", position);
                startActivity(intent);
            }
        };
        mAdapter = new GalleryAdapter(GalleryActivity.this, data, onItemClickListener);
        mRecyclerView.setAdapter(mAdapter);
    }
}
