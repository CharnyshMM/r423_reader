package com.example.mikita.r423_reader;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class GalleryDetailSectionsPagerAdapter extends FragmentPagerAdapter {
    public ArrayList<GalleryImage> data = new ArrayList<>();
    PlaceholderFragment.OnImageTapListener onImageTapListener;

    public GalleryDetailSectionsPagerAdapter(FragmentManager fm, ArrayList<GalleryImage> data, PlaceholderFragment.OnImageTapListener listener) {
        super(fm);
        this.data = data;
        onImageTapListener = listener;
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        return PlaceholderFragment.newInstance(
                position,
                data.get(position).getImageUrl(),
                data.get(position).getImageUrl(),
                onImageTapListener
        );
    }

    @Override
    public int getCount() {
        // Show 3 total pages.
        return data.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return data.get(position).getImageUrl();
    }

    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */

        private String name, url;
        private int pos;
        private OnImageTapListener onImageTapListener;

        public static final String ARG_SECTION_NUMBER = "section_number";
        public static final String ARG_IMG_TITLE = "image_title";
        public static final String ARG_IMG_URL = "image_url";

        @Override
        public void setArguments(Bundle args) {
            super.setArguments(args);
            this.pos = args.getInt(ARG_SECTION_NUMBER);
            this.name = args.getString(ARG_IMG_TITLE);
            this.url = args.getString(ARG_IMG_URL);
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber, String name, String url, OnImageTapListener listener) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            args.putString(ARG_IMG_TITLE, name);
            args.putString(ARG_IMG_URL, url);
            fragment.setArguments(args);
            fragment.setOnImageTapListener(listener);
            return fragment;
        }

        public PlaceholderFragment() { }

        @Override
        public void onStart() {
            super.onStart();
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
            final PhotoView imageView = (PhotoView) rootView.findViewById(R.id.gallery__detail_imageView);
            imageView.setMinimumScale(1f);
            imageView.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
                @Override
                public void onPhotoTap(View view, float x, float y) {
                    if (onImageTapListener != null) {
                        onImageTapListener.OnTap(view);
                    }
                }
            });
            try {
                InputStream stream = getActivity().getAssets().open(url);
                Drawable d = Drawable.createFromStream(stream, null);
                imageView.setImageDrawable(d);
            }
            catch (IOException e) {
                e.printStackTrace();
                imageView.setImageResource(R.drawable.not_found);
                Toast.makeText(getActivity(), R.string.error_image_not_loaded, Toast.LENGTH_LONG).show();
            }
            return rootView;
        }

        public void setOnImageTapListener(OnImageTapListener onImageTapListener) {
            this.onImageTapListener = onImageTapListener;
        }

        public interface OnImageTapListener {
            void OnTap(View view);
        }

    }
}
