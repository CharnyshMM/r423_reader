package by.mil.bsuir.r423_reader.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.MenuItem;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import by.mil.bsuir.r423_reader.R;
import by.mil.bsuir.r423_reader.fragments.adapters.ContentsListAdapter;
import by.mil.bsuir.r423_reader.storage.Chapter;
import by.mil.bsuir.r423_reader.storage.CurrentBookStorage;

import java.util.List;

public class ContentsActivity extends AppCompatActivity {

    private static final String TAG = "ContentsActivity";
    private RecyclerView contentsListView;
    private ContentsListAdapter.OnItemClickListener onContentsListItemClickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contents);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        contentsListView = findViewById(R.id.content__listView);
        contentsListView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        onContentsListItemClickListener = new ContentsListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Chapter item) {
                Intent data = new Intent();
                //---set the data to pass back---
                Log.d(TAG, "onItemClick: "+item.getText());
                data.putExtra("HeaderID", item.getId());
                setResult(RESULT_OK, data);
                //---close the activity---
                finish();
            }
        };

        List<Chapter> chapterList = CurrentBookStorage.getInstance().getChapters();
        if (chapterList == null || chapterList.size() == 0) {
            showNoChaptersDetectedDialog();
        } else {
            ContentsListAdapter adapter = new ContentsListAdapter(
                    getApplicationContext(),
                    chapterList,
                    onContentsListItemClickListener
            );
            contentsListView.setAdapter(adapter);
        }
    }

    private void showNoChaptersDetectedDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ContentsActivity.this);
        builder.setTitle(R.string.error_title);
        builder.setMessage(R.string.error_message_no_chapters_generated);
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                finish();
            }
        });
        builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.create().show();
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
