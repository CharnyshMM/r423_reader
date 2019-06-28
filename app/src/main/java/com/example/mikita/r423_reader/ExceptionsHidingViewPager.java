package com.example.mikita.r423_reader;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import androidx.viewpager.widget.ViewPager;

public class ExceptionsHidingViewPager extends ViewPager {

    public  ExceptionsHidingViewPager(Context context) {
        super(context);
    }

    public  ExceptionsHidingViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        try {
            return super.onInterceptTouchEvent(ev);
        } catch (IllegalArgumentException e) {
            //uncomment if you really want to see these errors
            Log.d("EXHVP", "Catched IllegalArgumentException");
            return false;
        }
    }
}
