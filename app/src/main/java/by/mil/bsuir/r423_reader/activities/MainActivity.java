package by.mil.bsuir.r423_reader.activities;

import android.content.Intent;
import android.os.Bundle;

import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;

import by.mil.bsuir.r423_reader.R;
import by.mil.bsuir.r423_reader.fragments.BooksFragment;
import by.mil.bsuir.r423_reader.storage.BookEntry;
import com.example.igor.ctrs.Main_Menu;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import static androidx.navigation.ui.NavigationUI.setupWithNavController;

public class MainActivity extends AppCompatActivity implements BooksFragment.OnBookSelectedListener {


    NavController navController;
    String book;
    String chapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = findViewById(R.id.navigation);
        Menu menu = bottomNavigationView.getMenu();
        menu.setGroupCheckable(0, true, false);
        menu.getItem(0).setChecked(false);

        menu.setGroupCheckable(0, true, true);
        //bottomNavigationView.getMenu().setGroupCheckable(0, true, true);

        navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()) {
                    case R.id.schemes_app:
                        Intent i = new Intent(getApplicationContext(), Main_Menu.class);
                        startActivity(i);
                        return false;
                    case R.id.fragment_books:
                        navController.navigate(R.id.fragment_books);
                        return true;
                    case R.id.fragment_gallery:
                        navController.navigate(R.id.fragment_gallery);
                        return true;
                }
                return false;
            }
        });


        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
                if (destination.getId() == R.id.fragment_reading) {
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                } else {
                    getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                }
            }
        });
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

    boolean doubleBackToExitPressedOnce = false;
    @Override
    public void onBackPressed() {
        if (navController.getCurrentDestination().getId() == R.id.fragment_reading) {
            navController.navigate(R.id.fragment_books);
        } else if (!doubleBackToExitPressedOnce) {
            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this,R.string.message_press_back_again_to_exit, Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        } else {
            moveTaskToBack(true);
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(0);
        }
    }

    @Override
    public void onBookSelectedListener(BookEntry book) {
        Bundle bundle = new Bundle();
        bundle.putString("book", book.getName());
        bundle.putString("chapter", book.getCurrentChapter());
        navController.navigate(R.id.fragment_reading, bundle);
    }
}
