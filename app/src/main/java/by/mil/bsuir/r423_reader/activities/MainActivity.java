package by.mil.bsuir.r423_reader.activities;

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
        setupWithNavController(bottomNavigationView, navController);


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
    public void onBookSelectedListener(String book, String chapter) {
        this.book = book;
        this.chapter = chapter;
        Bundle bundle = new Bundle();
        bundle.putString("book", book);
        bundle.putString("chapter", chapter);
        navController.navigate(R.id.fragment_reading, bundle);
    }
}
