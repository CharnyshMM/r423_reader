package com.example.mikita.r423_reader.fragments;

import android.content.Intent;
import android.view.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mikita.r423_reader.activities.FullscreenGalleryDetailActivity;
import com.example.mikita.r423_reader.fragments.adapters.GalleryAdapter;
import com.example.mikita.r423_reader.model.GalleryHeaderItem;
import com.example.mikita.r423_reader.model.GalleryImage;
import com.example.mikita.r423_reader.model.GalleryImageItem;
import com.example.mikita.r423_reader.model.GalleryListItem;
import com.example.mikita.r423_reader.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;

public class GalleryFragment extends Fragment {

    private static final String ANDROID_ASSET_PATH_START = "file:///android_asset";
    private static final String IMAGES_ASSET_PATH = "file:///android_asset/gallery";
    private RecyclerView mRecyclerView;
    private GalleryAdapter mAdapter;

    private static ReentrantLock locker = new ReentrantLock();

    private static GalleryFragment instance;

    public static GalleryFragment getInstance(){
        locker.lock();
        try{
            if (instance == null){
                instance = new GalleryFragment();
            }

            return instance;
        }finally {
            locker.unlock();
        }
    }

    /*for each gallery item in folder assets/gallery
     * there must be an EQUALLY named .jpg file and .json decription_file:
     * Example:
     *   р423_кунг.jpg
     *   р423_кунг.txt
     *   */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gallery, container, false);
        getActivity().setTitle(R.string.gallery);

        final ArrayList<GalleryListItem> data = new ArrayList<>();
        try{
            String[] paths = getContext().getAssets().list("gallery");
            for (String path: paths){
                boolean hasExtension = path.contains(".");
                if (!hasExtension){
                    String[] images = getContext().getAssets().list("gallery/" + path);
                    if (images.length != 0){
                        data.add(new GalleryHeaderItem(path));
                        for (String img: images){
                            String name = path + "/" + img;
                            data.add(new GalleryImageItem(new GalleryImage("gallery/" + name, name.replaceFirst("[.][^.]+$", ""))));
                        }
                    }
                }
            }

        } catch (IOException e){
            e.printStackTrace();
        }

        mRecyclerView = (RecyclerView) view.findViewById(R.id.gallery__images_recyclerView);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext().getApplicationContext(), 3);
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
                Intent intent = new Intent(getActivity(), FullscreenGalleryDetailActivity.class);
                ArrayList<GalleryImage> images = new ArrayList<>();
                int dataPositionCounter = -1;
                int imagesOnlyPositionCounter = -1;
                for (GalleryListItem item: data){
                    if (item instanceof GalleryImageItem){
                        if (dataPositionCounter < position) {
                            imagesOnlyPositionCounter++;
                        }
                        images.add(((GalleryImageItem) item).getImage());
                    }
                    dataPositionCounter++;
                }

                intent.putParcelableArrayListExtra("data", images);
                // Intent intent = new Intent(getApplicationContext(), FullscreenGalleryDetailActivity.class);
                // intent.putParcelableArrayListExtra("data", data);
                intent.putExtra("position", imagesOnlyPositionCounter);
                startActivity(intent);
            }
        };
        mAdapter = new GalleryAdapter(getContext(), data, onItemClickListener);
        mRecyclerView.setAdapter(mAdapter);

        return view;
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_gallery, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_books: {
                Intent i = new Intent(this, BooksFragment.class);
                startActivity(i);
                finish();
                return true;
            }
            case R.id.action_reading_now: {
                Intent i = new Intent(this, ReadingFragment.class);
                startActivity(i);
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(this, ReadingFragment.class);
        startActivity(i);
        finish();
    }*/
}
