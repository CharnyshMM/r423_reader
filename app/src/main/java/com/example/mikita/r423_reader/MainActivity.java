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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.webkit.ValueCallback;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int CONTENTS_ACTIVITY_RESULT = 1;

    private static final String TAG = "MainActivity";
    private static final int MAXIMUM_TEXT_ZOOM = 150;
    private static final int MINIMUM_TEXT_ZOOM = 50;
    private static final String ANDROID_ASSET_PATH_START = "file:///android_asset";
    private static final String BOOKS_ASSET_PATH = "file:///android_asset/books";
    private static final String BOOK_INDEX_FILENAME = "index.htm";

    private static final String JsScripts =
            "function scrollToElement(id) {\n" +
            "        var elem = document.getElementById(id);\n" +
            "        console.log(elem);\n" +
            "        elem.scrollIntoView(true);\n" +
            "    }" +
            "(function() {\n" +
            "        function getChapters() {\n" +
            "        let resultArr = [];\n" +
                    "        let idsArr = [];\n" +
                    "\n" +
                    "        let hArr = getChaptersTags();\n" +
                    "        if (hArr === null || hArr === undefined ||  hArr.length === 0) return ;\n" +
                    "\n" +
                    "        let retText = \"\";\n" +
                    "        let size = hArr.length;\n" +
                    "        for (let i=0; i<size; i++) {\n" +
                    "            if (!isNaN(+hArr[i].innerText.substr(0,1))) {\n" +
                    "                if (retText.trim() !== \"\") {\n" +
                    "                    resultArr.push(retText);\n" +
                    "                    retText = \"\";\n" +
                    "                }\n" +
                    "                let id = generateId();\n" +
                    "                hArr[i].setAttribute(\"id\", id);\n" +
                    "                hArr[i].style.backgroundColor = \"red\";\n" +
                    "                idsArr.push(id);\n" +
                    "                retText += hArr[i].innerText;\n" +
                    "            } else {\n" +
                    "                if (hArr[i].innerText.trim() !== \"\") {\n" +
                    "                    retText += \" \" + hArr[i].innerText;\n" +
                    "                }\n" +
                    "            }\n" +
                    "            if (i === size-1) {\n" +
                    "                resultArr.push(retText);\n" +
                    "            }\n" +
                    "        }"+
            "        console.log(\"SCRIPT IS RUNNING!!!!\");\n" +
                    "resultArr = resultArr.filter(elem => elem.trim()!==\"\");"+
            "        return addToJson(idsArr, resultArr);\n" +
            "        }\n" +
            "        function addToJson(ids, texts) {\n" +
            "        if (ids===null || texts===null || ids.length!==texts.length) {\n" +
            "        throw new DOMException(\"ids size != texts size\");\n" +
            "        }\n" +
            "        let objects = [];\n" +
            "        for (let i=0; i < ids.length; i++) {\n" +
            "        objects.push({id: ids[i], text: texts[i]});\n" +
            "        }\n" +
            "            return objects;\n" +
            "        }\n" +
            "        function getChaptersTags() {\n" +
            "            return Array.prototype.slice.call(document.querySelectorAll(\"h1, h2, h3, .MsoTitle\"));\n" +
            "        }\n" +
            "        function generateId () {\n" +
            "            return \"_\" + Math.random().toString(36).substr(2, 9);\n" +
            "        }\n" +
            "        return getChapters();\n" +
            "        }());";

    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        webView = findViewById(R.id.main__webView);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        Intent i = getIntent();
        String book = i.getStringExtra("book");
        String chapter = i.getStringExtra("chapter");


        String bookName = getBookPathToOpen(book, chapter);
        setTitle(bookName);


        webView.loadUrl(BOOKS_ASSET_PATH + "/" + bookName + BOOK_INDEX_FILENAME);
        webView.setWebViewClient(new WebViewClient() {

            public void onPageFinished(WebView view, String url) {
                getChaptersJson();
            }
        });

    }

    public void getChaptersJson() {
        // this code is to run JavaScript code from the string above and get its result
        Log.d(TAG, "getChaptersJson:  ");
        webView.evaluateJavascript("javascript:" + this.JsScripts
                , new ValueCallback<String>() {
            @TargetApi(Build.VERSION_CODES.HONEYCOMB)
            @Override
            public void onReceiveValue(String s) {
                Gson gson = new Gson();
                Type listType = new TypeToken<ArrayList<Chapter>>(){}.getType();
                List<Chapter> chapterList = gson.fromJson(s, listType);
                CurrentBookStorage.getInstance().setChapters(chapterList);
            }
        });
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

            return book+ "/";
        }

        AssetManager assetManager = getAssets();
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
                for (String file: assetFiles) {
                    if (file.endsWith(BOOK_INDEX_FILENAME)) {
                        return currentPath.toString();
                    }
                }

                currentPath.append(assetFiles[0]);
                assetFiles = assetManager.list("books/"+currentPath.toString());
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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.contents:
                Intent intent = new Intent(getApplicationContext(), ContentsActivity.class);
                startActivityForResult(intent, CONTENTS_ACTIVITY_RESULT);
                return true;
            case R.id.increase_text: {
                int textZoom = webView.getSettings().getTextZoom();
                if (textZoom >= MAXIMUM_TEXT_ZOOM) {
                    Toast.makeText(getApplicationContext(), "Maximum achieved", Toast.LENGTH_SHORT).show();
                } else {
                    webView.getSettings().setTextZoom(textZoom + 5);
                }
                return true;
            }
            case R.id.decrease_text:{
                int textZoom = webView.getSettings().getTextZoom();
                if (textZoom <= MINIMUM_TEXT_ZOOM) {
                    Toast.makeText(getApplicationContext(), "Minimum achieved", Toast.LENGTH_SHORT).show();
                } else {
                    webView.getSettings().setTextZoom(textZoom - 5);
                }
                return true;
            }
            case R.id.books: {
                Intent i = new Intent(getApplicationContext(), BooksActivity.class);
                startActivity(i);
                finish();
                return true;
            }
            case R.id.gallery:
                Intent i = new Intent(getApplicationContext(), GalleryActivity.class);
                startActivity(i);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CONTENTS_ACTIVITY_RESULT) {
            if (resultCode == RESULT_OK) {
                String headerId = data.getExtras().getString("HeaderID", null);
                if (headerId != null) {
                    Log.d(TAG, "onActivityResult: headerId != null "+headerId);
                    webView.evaluateJavascript("javascript:scrollToElement('"+headerId+"')", new ValueCallback<String>() {
                        @TargetApi(Build.VERSION_CODES.HONEYCOMB)
                        @Override
                        public void onReceiveValue(String s) {
                            Log.d(TAG, "onReceiveValue: js evaluated");
                            JsonReader reader = new JsonReader(new StringReader(s));

                            // Must set lenient to parse single values
                            reader.setLenient(true);

                            try {
                                if(reader.peek() != JsonToken.NULL) {
                                    if(reader.peek() == JsonToken.STRING) {
                                        String msg = reader.nextString();
                                        if(msg != null) {
                                            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                                        }
                                    }
                                }
                            } catch (IOException e) {
                                Log.e("TAG", "MainActivity: IOException", e);
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
    }
}
