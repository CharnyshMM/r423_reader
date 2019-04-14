package com.example.mikita.r423_reader;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.os.Build;
import android.util.JsonToken;
import android.util.JsonReader;
import android.util.Log;
import android.view.*;
import android.webkit.ValueCallback;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

import static android.app.Activity.RESULT_OK;

public class BookFragment extends Fragment {

    public static final int CONTENTS_ACTIVITY_RESULT = 1;

    private static final String TAG = "BookFragment";
    private static final int MAXIMUM_TEXT_ZOOM = 150;
    private static final int MINIMUM_TEXT_ZOOM = 50;
    private static final String ANDROID_ASSET_PATH_START = "file:///android_asset";
    private static final String BOOKS_ASSET_PATH = "file:///android_asset/books";
    private static final String BOOK_INDEX_FILENAME = "index.htm";

    private static ReentrantLock locker = new ReentrantLock();
    private static BookFragment instance;

    private String mJsScripts;

    WebView webView;

    public static BookFragment getInstance(){
        locker.lock();
        try{
            if (instance == null){
                instance = new BookFragment();
            }

            return instance;
        }finally {
            locker.unlock();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_book, container, false);

        webView = view.findViewById(R.id.main__webView);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false);

        Intent i = getActivity().getIntent();
        String book = i.getStringExtra("book");
        String chapter = i.getStringExtra("chapter");

        String bookName = getBookPathToOpen(book, chapter);
        getActivity().setTitle(bookName);
        //getActivity().setTheme(R.style.AppTheme);

        webView.loadUrl(BOOKS_ASSET_PATH + "/" + bookName + BOOK_INDEX_FILENAME);
        webView.setWebViewClient(new WebViewClient() {

            public void onPageFinished(WebView view, String url) {
                mJsScripts = loadJavaScript("scripts.js");
                getChaptersJson();
            }
        });
        saveBookPathToSharedPreferences(bookName);


        return view;
    }


    public void getChaptersJson() {
        // this code is to run JavaScript code from the string above and get its result
        Log.d(TAG, "getChaptersJson:  ");
        webView.evaluateJavascript("javascript:" + mJsScripts
                , new ValueCallback<String>() {
                    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
                    @Override
                    public void onReceiveValue(String s) {
                        Gson gson = new Gson();
                        Type listType = new TypeToken<ArrayList<Chapter>>() {
                        }.getType();
                        List<Chapter> chapterList = gson.fromJson(s, listType);
                        CurrentBookStorage.getInstance().setChapters(chapterList);
                    }
                });
    }

    public void saveBookPathToSharedPreferences(String book) {
        SharedPreferences sharedPref = this.getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(getString(R.string.last_book_path), book);
        editor.apply();
    }

    public String tryGetBookPathFromSharedPreferences() {
        SharedPreferences sharedPref = this.getActivity().getPreferences(Context.MODE_PRIVATE);

        String book = sharedPref.getString(getString(R.string.last_book_path), null);
        //String wideViewMode = sharedPref.getBoolean(R.string.last_wideView_mode, false);
        return book;
    }

    public String getBookPathToOpen(String book, String chapter) {
        // if lastOpenedBookPath is null, than returning first book in books
        // for the first book drilling down till index file found.

        /*
         * ASSETS FOLDER STRUCTURE:
         *   android_asset/
         *                  book_name/
         *                           chapter_1_name/
         *                                          index.htm
         *                                         / *
         * */

        if (book != null && chapter != null) {
            return book + "/" + chapter + "/";
        }

        if (book != null) {
            return book + "/";
        }

        book = tryGetBookPathFromSharedPreferences();
        if (book != null) {
            return book;
        }

        AssetManager assetManager = this.getContext().getAssets();
        try {
            StringBuilder currentPath = new StringBuilder();
            String[] assetFiles = assetManager.list("books");
            if (assetFiles == null) {
                throw new Exception("No books found in assets!");
            }
            boolean indexFound = false;
            while (!indexFound) {
                // sorry, I didn't found a better way to check if the entry is file or directory
                // so if I didn't found index.htm, than I open first found file as a directory
                for (String file : assetFiles) {
                    if (file.endsWith(BOOK_INDEX_FILENAME)) {
                        return currentPath.toString();
                    }
                }

                currentPath.append(assetFiles[0]);
                assetFiles = assetManager.list("books/" + currentPath.toString());
                currentPath.append("/");
            }
        } catch (IOException e) {
            // show error dialog
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null; // if nothing found... not a good idea for error handling
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_activity_toolbar_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            /*case R.id.action_contents:
                Intent intent = new Intent(getContext().getApplicationContext(), ContentsActivity.class);
                startActivityForResult(intent, CONTENTS_ACTIVITY_RESULT);
                return true;

            case R.id.action_wide_view_checkable:
                item.setChecked(!item.isChecked());
                webView.getSettings().setUseWideViewPort(item.isChecked());
                return true;
            case R.id.action_increase_text: {
                int textZoom = webView.getSettings().getTextZoom();
                if (textZoom >= MAXIMUM_TEXT_ZOOM) {
                    Toast.makeText(getContext().getApplicationContext(), "Maximum achieved", Toast.LENGTH_SHORT).show();
                } else {
                    webView.getSettings().setTextZoom(textZoom + 5);
                }
                return true;
            }
            case R.id.action_decrease_text: {
                int textZoom = webView.getSettings().getTextZoom();
                if (textZoom <= MINIMUM_TEXT_ZOOM) {
                    Toast.makeText(getContext().getApplicationContext(), "Minimum achieved", Toast.LENGTH_SHORT).show();
                } else {
                    webView.getSettings().setTextZoom(textZoom - 5);
                }
                return true;
            }
            case R.id.action_books: {
                Intent i = new Intent(getContext().getApplicationContext(), BooksFragment.class);
                startActivity(i);
                finish();
                return true;
            }
            case R.id.action_gallery:
                Intent i = new Intent(getContext().getApplicationContext(), GalleryFragment.class);
                startActivity(i);
                finish();
                return true;*/
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onActivityResult(int resultCode, Intent data)
    {
        if (resultCode == RESULT_OK) {
            String headerId = data.getExtras().getString("HeaderID", null);
            if (headerId != null) {
                Log.d(TAG, "onActivityResult: headerId != null " + headerId);
                webView.evaluateJavascript("javascript:scrollToElement('" + headerId + "')", new ValueCallback<String>() {
                    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
                    @Override
                    public void onReceiveValue(String s) {
                        Log.d(TAG, "onReceiveValue: js evaluated");
                        JsonReader reader = new JsonReader(new StringReader(s));

                        // Must set lenient to parse single values
                        reader.setLenient(true);

                        try {
                            if (reader.peek() != JsonToken.NULL) {
                                if (reader.peek() == JsonToken.STRING) {
                                    String msg = reader.nextString();
                                    if (msg != null) {
                                        Toast.makeText(getContext().getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                                    }
                                }
                            }
                        } catch (IOException e) {
                            Log.e("TAG", "BookFragment: IOException", e);
                        } finally {
                            try {
                                reader.close();
                            } catch (IOException e) {
                                // NOOP
                            }
                        }
                    }
                });
            }
        }
    }

    private String loadJavaScript(String fileName) {
        StringBuilder builder = new StringBuilder();
        InputStream stream;

        try {
            stream = this.getContext().getAssets().open(fileName);
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8));

            String tempsString;
            while ((tempsString = reader.readLine()) != null) {
                builder.append(tempsString);
            }
            reader.close();
        } catch (IOException e) {
            // TODO: print valid error description
            e.printStackTrace();
        } 

        return builder.toString();
    }
}
