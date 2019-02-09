package com.example.mikita.r423_reader;

import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import androidx.core.widget.NestedScrollView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class BooksActivity extends AppCompatActivity {

    LinearLayout booksScrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_books);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        booksScrollView = findViewById(R.id.books_linearLayout);
        ArrayList<File> inFiles = new ArrayList<File>();

        AssetManager assetManager = getAssets();
        String[] files = new String[0];
        try {
            files = assetManager.list("books");
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (final String file:files) {
            Button button = new Button(getApplicationContext());
            button.setText(file);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.putExtra("directory", file);
                    startActivity(intent);
                    finish();
                }
            });
            try {
                booksScrollView.addView(button);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
