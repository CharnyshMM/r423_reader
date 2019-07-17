package com.example.igor.ctrs;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;


public class Main_Menu  extends AppCompatActivity implements View.OnClickListener {

    RelativeLayout sliderLayout;
    LinearLayout mainLayout;

    Button main_scheme, modes;

    Button  o1_48A;
    Button  o2_48, o2_480, o2_2x480, o2_2048;
    Button  r1_48, r1_480, r1_2x480, r1_2048;
    Button  r2_48, r2_480, r2_2x480, r2_2048;
    Button  u1_480, u1_2x480;
    Button  u2_480dir1, u2_2x480dir1, u2_2048dir1;
    Button  u2_480dir2, u2_2x480dir2, u2_2048dir2;

    FilterAnimation sliderAnimation;

    private void initializeButtons() {
        main_scheme = ButtonInit(R.id.btn_main_scheme);
        modes = ButtonInit(R.id.btn_modes);

        o2_48 = ButtonInit(R.id.o2_48);
        o2_480 = ButtonInit(R.id.o2_480);
        o2_2x480 = ButtonInit(R.id.o2_2x480);
        o2_2048 = ButtonInit(R.id.o2_2048);
        r2_48 = ButtonInit(R.id.r2_48);
        r2_480 = ButtonInit(R.id.r2_480);
        r2_2x480 = ButtonInit(R.id.r2_2x480);
        r2_2048 = ButtonInit(R.id.r2_2048);
        u2_480dir1 = ButtonInit(R.id.u2_480dir1);
        u2_480dir2 = ButtonInit(R.id.u2_480dir2);
        u2_2x480dir1 = ButtonInit(R.id.u2_2x480dir1);
        u2_2x480dir2 = ButtonInit(R.id.u2_2x480dir2);
        u2_2048dir1 = ButtonInit(R.id.u2_2048dir1);
        u2_2048dir2 = ButtonInit(R.id.u2_2048dir2);
        o1_48A = ButtonInit(R.id.o1_48A);
        r1_48 = ButtonInit(R.id.r1_48);
        r1_480 = ButtonInit(R.id.r1_480);
        r1_2x480 = ButtonInit(R.id.r1_2x480);
        r1_2048 = ButtonInit(R.id.r1_2048);
        u1_480 = ButtonInit(R.id.u1_480);
        u1_2x480 = ButtonInit(R.id.u1_2x480);
    }

    private Button ButtonInit(int id){
        Button button = (Button) findViewById(id);
        button.setOnClickListener(this);
        return button;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main_menu_activity);

        sliderLayout = (RelativeLayout)findViewById(R.id.slide_menu_layout);
        mainLayout = (LinearLayout)findViewById(R.id.main_menu_layout);

        initializeButtons();


        sliderAnimation = new FilterAnimation(this);
        sliderAnimation.initializeAnimations(sliderLayout, mainLayout, 0);
    }

    public void HandlePress(int imageId)
    {
        sliderAnimation.onAnimationEnd(null);
        Flags.isButtonModesEnable = true;
        Flags.image = imageId;
        startActivity(new Intent(this, main_activity.class));
    }

    @Override
    public void onClick(View v)
    {
        int i = v.getId();
        if (i == R.id.btn_main_scheme) {
            Flags.isButtonModesEnable = false;
            Flags.image = R.drawable.main_pic;
            startActivity(new Intent(this, main_activity.class));
        } else if (i == R.id.btn_modes) {
            sliderAnimation.toggleSliding();
        } else if (i == R.id.o2_48) {
            HandlePress(R.drawable.okon2sp48);
        } else if (i == R.id.o2_480) {
            HandlePress(R.drawable.okon2sp480);
        } else if (i == R.id.o2_2x480) {
            HandlePress(R.drawable.okon2sp2x480);
        } else if (i == R.id.o2_2048) {
            HandlePress(R.drawable.okon2sp2048);
        } else if (i == R.id.r2_48) {
            HandlePress(R.drawable.retr2sp48);
        } else if (i == R.id.r2_480) {
            HandlePress(R.drawable.retr2sp480);
        } else if (i == R.id.r2_2x480) {
            HandlePress(R.drawable.retr2sp2x480);
        } else if (i == R.id.r2_2048) {
            HandlePress(R.drawable.retr2sp2048);
        } else if (i == R.id.u2_480dir1) {
            HandlePress(R.drawable.uzl2sp480dir_ctrs1);
        } else if (i == R.id.u2_480dir2) {
            HandlePress(R.drawable.uzl2sp480dir_ctrs2);
        } else if (i == R.id.u2_2x480dir1) {
            HandlePress(R.drawable.uzl2sp2x480dir_ctrs1);
        } else if (i == R.id.u2_2x480dir2) {
            HandlePress(R.drawable.uzl2sp2x480dir_ctrs2);
        } else if (i == R.id.u2_2048dir1) {
            HandlePress(R.drawable.uzl2sp2048dir_ctrs1);
        } else if (i == R.id.u2_2048dir2) {
            HandlePress(R.drawable.uzl2sp2048dir_ctrs2);
        } else if (i == R.id.o1_48A) {
            HandlePress(R.drawable.okon1sp48dira);
        } else if (i == R.id.r1_48) {
            HandlePress(R.drawable.retr1sp48);
        } else if (i == R.id.r1_480) {
            HandlePress(R.drawable.retr1sp480);
        } else if (i == R.id.r1_2x480) {
            HandlePress(R.drawable.retr1sp2x480);
        } else if (i == R.id.r1_2048) {
            HandlePress(R.drawable.retr1sp2048);
        } else if (i == R.id.u1_480) {
            HandlePress(R.drawable.uzl1sp2x480_a_b);
        } else if (i == R.id.u1_2x480) {
            HandlePress(R.drawable.uzl1sp2x480_a_b);
        }
    }
}
