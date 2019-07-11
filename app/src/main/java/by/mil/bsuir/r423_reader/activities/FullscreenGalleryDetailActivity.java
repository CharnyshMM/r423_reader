package by.mil.bsuir.r423_reader.activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import androidx.viewpager.widget.ViewPager;

import by.mil.bsuir.r423_reader.fragments.ExceptionsHidingViewPager;
import by.mil.bsuir.r423_reader.model.GalleryImage;
import by.mil.bsuir.r423_reader.R;
import by.mil.bsuir.r423_reader.fragments.adapters.GalleryDetailSectionsPagerAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class FullscreenGalleryDetailActivity extends AppCompatActivity {
    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler();
    private ExceptionsHidingViewPager mViewPager;
    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar

            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
            mViewPager.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };
    private View mControlsView;
    private Button mInfoButton;
    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
            mControlsView.setVisibility(View.VISIBLE);
        }
    };
    private boolean mVisible;
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };
    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    private final View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
            }
            return false;
        }
    };
    private ArrayList<GalleryImage> imageArrayList;
    private int currentPosition;
    private HashMap<String, String> imagesDescriptionsHashMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fullscreen_gallery_detail);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mVisible = true;
        mControlsView = findViewById(R.id.fullscreen_content_controls);
        mViewPager = findViewById(R.id.fullscreen_gallery_detail__container_viewPager);
        mInfoButton = findViewById(R.id.fullscreen_gallery_detail__info_button);

        mInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i = mViewPager.getCurrentItem();
                GalleryImage image = imageArrayList.get(i);
                if (imagesDescriptionsHashMap == null) {
                    Toast.makeText(getApplicationContext(), R.string.error__descriptions_not_loaded, Toast.LENGTH_LONG).show();
                    return;
                }
                String description = imagesDescriptionsHashMap.get(image.imageName.toLowerCase());
                // TODO: make it implicit to use lowercase keys here
                if (description == null) {
                    Toast.makeText(getApplicationContext(), R.string.error__no_description_for_image, Toast.LENGTH_LONG).show();
                    return;
                }
                showInfoDialog(image.getImageName(), description);
            }
        });

        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.


        imageArrayList = getIntent().getParcelableArrayListExtra("data");
        currentPosition = getIntent().getIntExtra("position", 0);
        setTitle(imageArrayList.get(currentPosition).getImageName());
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        GalleryDetailSectionsPagerAdapter mSectionsPagerAdapter = new GalleryDetailSectionsPagerAdapter(
                getSupportFragmentManager(),
                imageArrayList,
                new GalleryDetailSectionsPagerAdapter.PlaceholderFragment.OnImageTapListener() {
                    @Override
                    public void OnTap(View view) {
                        toggle();
                    }
                }

        );

        // Set up the ViewPager with the sections adapter
        mViewPager.setAdapter(mSectionsPagerAdapter);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                String name = imageArrayList.get(position).getImageName();
                int index = name.indexOf("/");
                if (index < 0 || index + 1 == name.length())
                {
                    setTitle(name);
                } else {
                    setTitle(name.substring(index+1));
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mViewPager.setCurrentItem(currentPosition);
        DetailsLoaderAsyncTask descriptionsLoaderTask = new DetailsLoaderAsyncTask(getApplicationContext());
        descriptionsLoaderTask.setOnDescriptionsLoadedListener(new DetailsLoaderAsyncTask.OnDescriptionsLoadedListener() {
            @Override
            public void onDescriptionsLoaded(HashMap<String, String> descriptions) {
                imagesDescriptionsHashMap = descriptions;
            }
        });
        descriptionsLoaderTask.execute();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100);
    }

    private void toggle() {
        if (mVisible) {
            hide();
        } else {
            show();
        }
    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mControlsView.setVisibility(View.GONE);
        mVisible = false;

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    @SuppressLint("InlinedApi")
    private void show() {
        // Show the system bar
        mViewPager.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        mVisible = true;

        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mHidePart2Runnable);
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
    }

    private void showInfoDialog(String name, String description) {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog__gallery_detail_info);
        dialog.setTitle("Title...");

        // set the custom dialog components - text, image and button
        TextView nameTextView = dialog.findViewById(R.id.gallery_detail_info__name_textView);
        nameTextView.setText(name);

        Button understoodButton = dialog.findViewById(R.id.gallery_detail_info__understood_button);
        understoodButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        TextView descriptionTextView = dialog.findViewById(
                R.id.gallery_detail_info__description_textView
        );
        descriptionTextView.setText(description);
        dialog.setCancelable(false);
        // text decription & image textDescription is empty somehow
        dialog.show();
    }

    /**
     * Schedules a call to hide() in delay milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.onBackPressed();
                return true;
        }
        return false;
    }

}

class DetailsLoaderAsyncTask extends AsyncTask<Void, Void, HashMap<String, String>> {

    private WeakReference<Context> contextRef;
    OnDescriptionsLoadedListener listener;

    public DetailsLoaderAsyncTask(Context context) {
        contextRef = new WeakReference<Context>(context);
    }

    public void setOnDescriptionsLoadedListener(OnDescriptionsLoadedListener listener) {
        this.listener = listener;
    }

    protected HashMap<String, String> getHasMapFromJsonObject(JSONObject object) {
        HashMap<String, String> map = new HashMap<>();
        Iterator<String> keysItr = object.keys();
        try {
            while (keysItr.hasNext()) {
                String key = keysItr.next();
                String value = (String) object.get(key);
                map.put(key.toLowerCase(), value);
            }
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
        return map;
    }

    @Override
    protected HashMap<String, String> doInBackground(Void... voids) {
        String json = null;
        try {
            InputStream is = contextRef.get().getAssets().open("gallery/descriptions.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
            JSONObject jsonObject = new JSONObject(json);
            return getHasMapFromJsonObject(jsonObject);
        } catch (IOException ex) {
            ex.printStackTrace();
            return new HashMap<>();
        } catch (JSONException ex) {
            ex.printStackTrace();
            return new HashMap<>();
        }
    }

    @Override
    protected void onPostExecute(HashMap<String, String> result) {
        super.onPostExecute(result);
        listener.onDescriptionsLoaded(result);
    }

    public interface OnDescriptionsLoadedListener {
        void onDescriptionsLoaded(HashMap<String, String> descriptions);
    }
}
