package com.example.igor.ctrs;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

public class main_activity extends AppCompatActivity implements View.OnClickListener
{
    RelativeLayout sliderLayout;
    LinearLayout mainLayout;

    Button  btn_D66, btn_D0031, btn_IS, btn_ITA_R, btn_ITA_L,
            btn_IO4, btn_IL34_L, btn_IL34_R, btn_IO3B_R,
            btn_IO3A_R, btn_IO3B_L, btn_IO3A_L, btn_D39,
            btn_D38A, btn_KP2, btn_D61, btn_D55_R, btn_D55_L,
            btn_MSHU_R, btn_MSHU_L, btn_D54_R, btn_D54_L,
            btn_D53B_R, btn_D53B_L, btn_D0039, btn_D0040;

    Button  o1_48A;
    Button  o2_48, o2_480, o2_2x480, o2_2048;
    Button  r1_48, r1_480, r1_2x480, r1_2048;
    Button  r2_48, r2_480, r2_2x480, r2_2048;
    Button  u1_480, u1_2x480;
    Button  u2_480dir1, u2_2x480dir1, u2_2048dir1;
    Button  u2_480dir2, u2_2x480dir2, u2_2048dir2;

    Button btnFilter, btnMenu;

    FilterAnimation sliderAnimation;

    @Override
    public void onClick(View v)
    {
        int id = v.getId();
        if(!(CheckForModeChosen(id) || CheckForBlockChosen(id)))
        {
            int i = v.getId();
            if (i == R.id.btn_menu) {
                Flags.image = R.drawable.main_pic;
                finish();
            } else if (i == R.id.btn_filter) {
                sliderAnimation.toggleSliding();
            }
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main_activity);

        sliderLayout = findViewById(R.id.slide_menu_layout);
        mainLayout = findViewById(R.id.main_layout);
        mainLayout.setBackground(getResources().getDrawable(Flags.image));

        initializeButtons();

        sliderAnimation = new FilterAnimation(this);
        sliderAnimation.initializeAnimations(sliderLayout, mainLayout, 0);
    }

    private void initializeButtons()
    {
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

        btnMenu = ButtonInit(R.id.btn_menu);
        btn_D66 = ButtonInit(R.id.btn_D66);
        btn_D0031 = ButtonInit(R.id.btn_D0031);
        btn_IS = ButtonInit(R.id.btn_IS);
        btn_ITA_R = ButtonInit(R.id.btn_ITA_R);
        btn_ITA_L = ButtonInit(R.id.btn_ITA_L);
        btn_IO4 = ButtonInit(R.id.btn_IO4);
        btn_IL34_L = ButtonInit(R.id.btn_IL34_L);
        btn_IL34_R = ButtonInit(R.id.btn_IL34_R);
        btn_IO3B_R = ButtonInit(R.id.btn_IO3B_R);
        btn_IO3A_R = ButtonInit(R.id.btn_IO3A_R);
        btn_IO3B_L = ButtonInit(R.id.btn_IO3B_L);
        btn_IO3A_L = ButtonInit(R.id.btn_IO3A_L);
        btn_D39 = ButtonInit(R.id.btn_D39);
        btn_D38A = ButtonInit(R.id.btn_D38A);
        btn_KP2 = ButtonInit(R.id.btn_KP2);
        btn_D61 = ButtonInit(R.id.btn_D61);
        btn_D55_R = ButtonInit(R.id.btn_D55_R);
        btn_D55_L = ButtonInit(R.id.btn_D55_L);
        btn_MSHU_R = ButtonInit(R.id.btn_MSHU_R);
        btn_MSHU_L = ButtonInit(R.id.btn_MSHU_L);
        btn_D54_R = ButtonInit(R.id.btn_D54_R);
        btn_D54_L = ButtonInit(R.id.btn_D54_L);
        btn_D53B_R = ButtonInit(R.id.btn_D53B_R);
        btn_D53B_L = ButtonInit(R.id.btn_D53B_L);
        btn_D0039 = ButtonInit(R.id.btn_D0039);
        btn_D0040 = ButtonInit(R.id.btn_D0040);

        btnFilter = findViewById(R.id.btn_filter);
        if(Flags.isButtonModesEnable)
            btnFilter.setOnClickListener(this);
        else
            btnFilter.setVisibility(View.INVISIBLE);
    }

    private Button ButtonInit(int id)
    {
        Button button = (Button) findViewById(id);
        button.setOnClickListener(this);
        return button;
    }

    private void HandlePress(int picId)
    {
        LinearLayout bgElement;
        bgElement = findViewById(R.id.main_layout);
        bgElement.setBackground(getResources().getDrawable(picId));
        sliderAnimation.onAnimationEnd(null);
    }

    private void intentView(int R_str_info, int R_str_title, boolean is_need_struct, int R_drawable_img, int R_drawable_struct)
    {
        Intent intent = new Intent(this, BlockInfo.class);
        intent.putExtra("string_title", R_str_title);
        intent.putExtra("string_info", R_str_info);
        intent.putExtra("drawableIdImage", R_drawable_img);
        intent.putExtra("drawableIdStructure", R_drawable_struct);
        if(is_need_struct)
            intent.putExtra("isNeedStructure", true);
        else
            intent.putExtra("isNeedStructure", false);
        startActivity(intent);
    }

    private boolean CheckForBlockChosen(int id)
    {
        if (id == R.id.btn_D66) {
            intentView(R.string.d66_info, R.string.d66_title, false, R.drawable.image_d66, 0);
        } else if (id == R.id.btn_D0031) {
            intentView(R.string.d0031_info, R.string.d0031_title, false, R.drawable.image_d0031, 0);
        } else if (id == R.id.btn_IS) {
            intentView(R.string.is_info, R.string.is_title, true, R.drawable.image_is, R.drawable.structure_internal);
        } else if (id == R.id.btn_ITA_R) {
            intentView(R.string.ita_r_info, R.string.ita_r_title, true, R.drawable.image_it_a, R.drawable.structure_internal);
        } else if (id == R.id.btn_ITA_L) {
            intentView(R.string.ita_l_info, R.string.ita_l_title, true, R.drawable.image_it_a, R.drawable.structure_internal);
        } else if (id == R.id.btn_IO4) {
            intentView(R.string.io4_info, R.string.io4_title, true, R.drawable.image_io_4, R.drawable.structure_internal);
        } else if (id == R.id.btn_IL34_L) {
            intentView(R.string.il34_l_info, R.string.il34_l_title, true, R.drawable.image_il3_4, R.drawable.structure_internal);
        } else if (id == R.id.btn_IL34_R) {
            intentView(R.string.il34_r_info, R.string.il34_r_title, true, R.drawable.image_il3_4, R.drawable.structure_internal);
        } else if (id == R.id.btn_IO3B_R) {
            intentView(R.string.io3b_r_info, R.string.io3b_r_title, true, R.drawable.image_io_3b, R.drawable.structure_internal);
        } else if (id == R.id.btn_IO3A_R) {
            intentView(R.string.io3a_r_info, R.string.io3a_r_title, true, R.drawable.image_io_3a, R.drawable.structure_internal);
        } else if (id == R.id.btn_IO3B_L) {
            intentView(R.string.io3b_l_info, R.string.io3b_l_title, true, R.drawable.image_io_3b, R.drawable.structure_internal);
        } else if (id == R.id.btn_IO3A_L) {
            intentView(R.string.io3a_l_info, R.string.io3a_l_title, true, R.drawable.image_io_3a, R.drawable.structure_internal);
        } else if (id == R.id.btn_D39) {
            intentView(R.string.d39_info, R.string.d39_title, true, R.drawable.image_d39, R.drawable.structure_d39);
        } else if (id == R.id.btn_D38A) {
            intentView(R.string.d38a_info, R.string.d38a_title, true, R.drawable.image_d38a, R.drawable.structure_d38a);
        } else if (id == R.id.btn_KP2) {
            intentView(R.string.kp3_info, R.string.kp3_title, true, R.drawable.image_p2, R.drawable.structure_afss_p2);
        } else if (id == R.id.btn_D61) {
            intentView(R.string.d61_info, R.string.d61_title, true, R.drawable.image_d61, R.drawable.structure_d61);
        } else if (id == R.id.btn_D55_R) {
            intentView(R.string.d55_r_info, R.string.d55_r_title, true, R.drawable.image_d55, R.drawable.structure_d55m);
        } else if (id == R.id.btn_D55_L) {
            intentView(R.string.d55_l_info, R.string.d55_l_title, true, R.drawable.image_d55, R.drawable.structure_d55m);
        } else if (id == R.id.btn_MSHU_R) {
            intentView(R.string.mshu_r_info, R.string.mshu_r_title, true, R.drawable.image_mshu, R.drawable.structure_mshu);
        } else if (id == R.id.btn_MSHU_L) {
            intentView(R.string.mshu_l_info, R.string.mshu_l_title, true, R.drawable.image_mshu, R.drawable.structure_mshu);
        } else if (id == R.id.btn_D54_R) {
            intentView(R.string.d54_r_info, R.string.d54_r_title, false, R.drawable.image_d54, 0);
        } else if (id == R.id.btn_D54_L) {
            intentView(R.string.d54_l_info, R.string.d54_l_title, false, R.drawable.image_d54, 0);
        } else if (id == R.id.btn_D53B_R) {
            intentView(R.string.d53b_r_info, R.string.d53b_r_title, true, R.drawable.image_d53b, R.drawable.structure_d53b);
        } else if (id == R.id.btn_D53B_L) {
            intentView(R.string.d53b_l_info, R.string.d53b_l_title, true, R.drawable.image_d53b, R.drawable.structure_d53b);
        } else if (id == R.id.btn_D0039) {
            intentView(R.string.d0039_info, R.string.d0039_title, true, R.drawable.image_d0040, R.drawable.structure_d0039);
        } else if (id == R.id.btn_D0040) {
            intentView(R.string.d0040_info, R.string.d0040_title, true, R.drawable.image_d0040, R.drawable.structure_d0040);
        } else {
            return false;
        }
        return true;
    }

    private boolean CheckForModeChosen(int id)
    {

        if (id == R.id.o2_48) {
            HandlePress(R.drawable.okon2sp48);
        } else if (id == R.id.o2_480) {
            HandlePress(R.drawable.okon2sp480);
        } else if (id == R.id.o2_2x480) {
            HandlePress(R.drawable.okon2sp2x480);
        } else if (id == R.id.o2_2048) {
            HandlePress(R.drawable.okon2sp2048);
        } else if (id == R.id.r2_48) {
            HandlePress(R.drawable.retr2sp48);
        } else if (id == R.id.r2_480) {
            HandlePress(R.drawable.retr2sp480);
        } else if (id == R.id.r2_2x480) {
            HandlePress(R.drawable.retr2sp2x480);
        } else if (id == R.id.r2_2048) {
            HandlePress(R.drawable.retr2sp2048);
        } else if (id == R.id.u2_480dir1) {
            HandlePress(R.drawable.uzl2sp480dir_ctrs1);
        } else if (id == R.id.u2_480dir2) {
            HandlePress(R.drawable.uzl2sp480dir_ctrs2);
        } else if (id == R.id.u2_2x480dir1) {
            HandlePress(R.drawable.uzl2sp2x480dir_ctrs1);
        } else if (id == R.id.u2_2x480dir2) {
            HandlePress(R.drawable.uzl2sp2x480dir_ctrs2);
        } else if (id == R.id.u2_2048dir1) {
            HandlePress(R.drawable.uzl2sp2048dir_ctrs1);
        } else if (id == R.id.u2_2048dir2) {
            HandlePress(R.drawable.uzl2sp2048dir_ctrs2);
        } else if (id == R.id.o1_48A) {
            HandlePress(R.drawable.okon1sp48dira);
        } else if (id == R.id.r1_48) {
            HandlePress(R.drawable.retr1sp48);
        } else if (id == R.id.r1_480) {
            HandlePress(R.drawable.retr1sp480);
        } else if (id == R.id.r1_2x480) {
            HandlePress(R.drawable.retr1sp2x480);
        } else if (id == R.id.r1_2048) {
            HandlePress(R.drawable.retr1sp2048);
        } else if (id == R.id.u1_480) {
            HandlePress(R.drawable.uzl1sp2x480_a_b);
        } else if (id == R.id.u1_2x480) {
            HandlePress(R.drawable.uzl1sp2x480_a_b);
        } else {
            return false;
        }
        return true;
    }
}