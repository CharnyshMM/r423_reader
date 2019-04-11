package com.example.mikita.r423_reader;

import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import androidx.core.widget.NestedScrollView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BooksActivity extends AppCompatActivity {

    LinearLayout booksScrollView;
    ExpandableListView expandableListView;
    ExpandableListAdapter expandableListAdapter;
    List<String> expandableListTitle;
    HashMap<String, List<String>> expandableListDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_books);
        booksScrollView = findViewById(R.id.books_linearLayout);
        expandableListView = (ExpandableListView) findViewById(R.id.expandableListView);

        expandableListTitle = new ArrayList<String>();
        expandableListDetail = new HashMap<String, List<String>>();
        expandableListAdapter = new CustomExpandableBooksListAdapter(
                getApplicationContext(),
                expandableListTitle,
                expandableListDetail
        );
        expandableListView.setAdapter(expandableListAdapter);

        AssetManager assetManager = getAssets();
        String[] booksDirs = new String[0];
        try {
            booksDirs = assetManager.list("books");

            for (final String bookDir:booksDirs) {
                String[] chapterDirs = assetManager.list("books/"+bookDir);
                ArrayList<String> chapters = new ArrayList<String>();
                for (final String chapterDir:chapterDirs) {
                    // maybe the cycle is not optimal.. but I have to check if there is no index.htm file
                    // because small books may not have chapters at all
                    if (chapterDir.endsWith("index.htm")) {
                        chapters = new ArrayList<String>() {
                            {
                                add(bookDir);
                            }
                        };
                        break;
                    }
                    chapters.add(chapterDir);
                }
                expandableListTitle.add(bookDir);
                expandableListDetail.put(bookDir, chapters);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(
                    ExpandableListView parent,
                    View v,
                    int groupPosition,
                    int childPosition,
                    long id) {
                String book = expandableListTitle.get(groupPosition);
                String chapter = expandableListDetail.get(book).get(childPosition);
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("book", book);
                intent.putExtra("chapter", chapter);
                startActivity(intent);
                finish();
                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_books, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_gallery:
                Intent i = new Intent(this, GalleryActivity.class);
                startActivity(i);
                finish();
                return true;
            case R.id.action_reading_now:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }
}
