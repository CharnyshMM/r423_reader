package com.example.igor.ctrs;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class BlockInfo extends AppCompatActivity implements View.OnClickListener {
    Button btn_image, btn_structure;
    int drawableIdImage, drawableIdStructure;
    int string_title, string_info;
    boolean isNeedStructure;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Bundle b = getIntent().getExtras();
        drawableIdImage = b.getInt("drawableIdImage");
        drawableIdStructure = b.getInt("drawableIdStructure");
        string_title = b.getInt("string_title");
        string_info = b.getInt("string_info");
        isNeedStructure = b.getBoolean("isNeedStructure");

        setContentView(R.layout.block_layout);

        TextView textViewInfo = (TextView) findViewById(R.id.textView_info);
        TextView textViewTitle = (TextView) findViewById(R.id.textView_title);
        textViewTitle.setText(string_title);
        textViewInfo.setText(string_info);

        btn_image =(Button) findViewById(R.id.button_id_image);
        btn_image.setOnClickListener(this);
        btn_structure =(Button) findViewById(R.id.button_id_structure);
        btn_structure.setOnClickListener(this);

        if(!isNeedStructure)
        {
            btn_structure.setEnabled(false);
            btn_structure.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        int viewId = v.getId();
        if(viewId == R.id.button_id_image){
            intent = new Intent(this, Activity_Picture.class);
            intent.putExtra("drawableId", drawableIdImage);
            startActivity(intent);
        }else if(viewId == R.id.button_id_structure){
            intent = new Intent(this, Activity_Picture.class);
            intent.putExtra("drawableId", drawableIdStructure);
            startActivity(intent);
        }
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }
}
