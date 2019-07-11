package com.example.mikita.r423_reader;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.*;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

public class BooksFragment extends Fragment {

    LinearLayout booksScrollView;
    ExpandableListView expandableListView;
    ExpandableListAdapter expandableListAdapter;
    List<String> expandableListTitle;
    HashMap<String, List<String>> expandableListDetail;
    private OnBookSelectedListener onBookSelectedListener;

    private static ReentrantLock locker = new ReentrantLock();


    private static BooksFragment instance;

    public static BooksFragment getInstance(){
        locker.lock();
        try{
            if (instance == null){
                instance = new BooksFragment();
            }

            return instance;
        }finally {
            locker.unlock();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_books, container, false);
        getActivity().setTitle(R.string.books_string);

        booksScrollView = view.findViewById(R.id.books_linearLayout);
        expandableListView = (ExpandableListView) view.findViewById(R.id.expandableListView);

        expandableListTitle = new ArrayList<String>();
        expandableListDetail = new HashMap<String, List<String>>();
        expandableListAdapter = new CustomExpandableBooksListAdapter(
                getContext().getApplicationContext(),
                expandableListTitle,
                expandableListDetail
        );
        expandableListView.setAdapter(expandableListAdapter);

        AssetManager assetManager = getContext().getAssets();
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
                Collections.sort(chapters, new Comparator<String>() {
                    @Override
                    public int compare(String o1, String o2) {
                        Pattern p = Pattern.compile("^[0-9]+");
                        Matcher m1 = p.matcher(o1);
                        Matcher m2 = p.matcher(o2);
                        if (m1.lookingAt() && m2.lookingAt()) {
                            int n1 = Integer.parseInt(m1.group());
                            int n2 = Integer.parseInt(m2.group());
                            return Integer.compare(n1, n2);
                        }
                        return o1.compareTo(o2);
                    }
                });
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
                /*

                Intent intent = new Intent(getContext().getApplicationContext(), ReadingFragment.class);
                intent.putExtra("book", book);
                intent.putExtra("chapter", chapter);
                startActivity(intent);*/
                String book = expandableListTitle.get(groupPosition);
                String chapter = expandableListDetail.get(book).get(childPosition);
                if(onBookSelectedListener != null) {
                    onBookSelectedListener.onBookSelectedListener(book, chapter);
                }
                return false;
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnBookSelectedListener) {
            onBookSelectedListener = (OnBookSelectedListener) context;
        }
    }


    public interface OnBookSelectedListener {
        void onBookSelectedListener(String book, String chapter);
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_books, menu);
        return true;
    }*/


    /*@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_gallery:
                Intent i = new Intent(this, GalleryFragment.class);
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
        Intent i = new Intent(this, ReadingFragment.class);
        startActivity(i);
        finish();
    }*/
}
