package com.example.mikita.r423_reader;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int CONTENTS_ACTIVITY_RESULT = 1;

    private static final String TAG = "MainActivity";
    private static final int MAXIMUM_TEXT_ZOOM = 150;
    private static final int MINIMUM_TEXT_ZOOM = 50;
    WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        webView = findViewById(R.id.main__webView);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        Intent i = getIntent();
        String bookName = i.getStringExtra("directory");
        Log.d(TAG, "onCreate: book path "+bookName);
        if (bookName != null) {
            webView.loadUrl("file:///android_asset/books/"+bookName+"/index.htm");
            setTitle(bookName);
        } else {
            webView.loadUrl("file:///android_asset/books/Р423/index.htm");
        }

        webView.setWebViewClient(new WebViewClient() {

            public void onPageFinished(WebView view, String url) {
                getChaptersJson();
            }
        });

    }

//    public String getLastReadBook() {
//        Context context = getApplicationContext();
//        SharedPreferences sharedPref = context.getSharedPreferences(
//                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
//    }

    public void getChaptersJson() {
        Log.d(TAG, "getChaptersJson:  ");
        webView.evaluateJavascript("javascript:getGlobalContents()", new ValueCallback<String>() {
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
                Log.d(TAG, "is headerId == null??: "+headerId);
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
