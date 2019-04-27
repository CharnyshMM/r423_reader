package com.example.mikita.r423_reader;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import static androidx.navigation.ui.NavigationUI.setupWithNavController;
import static com.example.mikita.r423_reader.BookFragment.CONTENTS_ACTIVITY_RESULT;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = findViewById(R.id.navigation);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        setupWithNavController(bottomNavigationView, navController);
    }


}
