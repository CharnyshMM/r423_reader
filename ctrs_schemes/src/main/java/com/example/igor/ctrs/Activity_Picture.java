package com.example.igor.ctrs;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Window;

import androidx.appcompat.app.AppCompatActivity;


public class Activity_Picture extends AppCompatActivity
{
    ZoomableImageView mImageView;
    int drawableId;

    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        Bundle b = getIntent().getExtras();

        setContentView(R.layout.picture_layout);
        drawableId = b.getInt("drawableId");
        mImageView = (ZoomableImageView) findViewById(R.id.image_id);

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), drawableId);
        mImageView.setImageBitmap(bitmap);
        initScreenOrientationSensor();
    }

    private void initScreenOrientationSensor() {
        SensorManager sensorManager = (SensorManager) this.getSystemService(Context.SENSOR_SERVICE);
        sensorManager.registerListener(new SensorEventListener() {
            int orientation = -1;

            @Override
            public void onSensorChanged(SensorEvent event) {
                if (event.values[1] < 6.5 && event.values[1] > -6.5) {
                    if (orientation != 1) {
                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                    }
                    orientation = 1;
                } else {
                    if (orientation != 0) {
                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    }
                    orientation = 0;
                }
            }
            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {}
        }, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_GAME);
    }
}
