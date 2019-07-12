package com.example.igor.ctrs;

import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;


public class FilterAnimation extends AppCompatActivity implements AnimationListener
{
    Context context;
    RelativeLayout sliderLayout;
    LinearLayout mainLayout;

    int kostylCounter = 0;

    private Animation sliderSlideIn, sliderSlideOut, mainSlideIn, mainSlideOut, kostylSliderIn;

    private static int mainLayoutWidth, mainLayoutHeight;
    private boolean isMainSlideOut = false;
    private int deviceWidth;
    private int margin;

    public FilterAnimation(){

    }

    public FilterAnimation(Context context)
    {
        this.context = context;
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        deviceWidth = displayMetrics.widthPixels;
    }

    public void initializeAnimations(final RelativeLayout slider_layout, final LinearLayout main_layout, int kostylCout)
    {
        kostylCounter = kostylCout;
        //Setting GlobolLayoutListener,
        // when layout is completely set this function will get called and we can have our layout object with correct width & height,
        // else if you simply try to get width/height of your layout in onCreate it will return 0
        final ViewTreeObserver sliderObserver = slider_layout.getViewTreeObserver();
        sliderObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                slider_layout.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                int filterLayoutWidth = (deviceWidth * 40) / 100;
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(filterLayoutWidth, RelativeLayout.LayoutParams.MATCH_PARENT);

                //here im setting the layout params for my filter.xml because its has width 260 dp,
                // so work it across all screen i first make layout adjustments so that it work across all screens resolution
                slider_layout.setLayoutParams(params);
                initializeFilterAnimations(slider_layout);
            }
        });

        final ViewTreeObserver mainObserver = main_layout.getViewTreeObserver();
        mainObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                main_layout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                initializeMainAnimations(main_layout);
            }
        });
    }


    public void initializeFilterAnimations(RelativeLayout sliderLayout)
    {
        this.sliderLayout = sliderLayout;
        sliderSlideIn = AnimationUtils.loadAnimation(context, R.anim.slider_slide_in);
        sliderSlideOut = AnimationUtils.loadAnimation(context, R.anim.slider_slide_out);
        kostylSliderIn = AnimationUtils.loadAnimation(context, R.anim.kostyl_slider_in);
    }

    public void initializeMainAnimations(LinearLayout mainLayout)
    {
        this.mainLayout = mainLayout;
        mainLayoutWidth = mainLayout.getWidth();
        mainLayoutHeight = mainLayout.getHeight();

        mainSlideIn = AnimationUtils.loadAnimation(context, R.anim.main_slide_in);
        mainSlideIn.setAnimationListener(this);

        mainSlideOut = AnimationUtils.loadAnimation(context, R.anim.main_slide_out);
        mainSlideOut.setAnimationListener(this);
    }

    public void toggleSliding()
    {
        if(isMainSlideOut)
        {
            sliderLayout.startAnimation(sliderSlideOut);
            sliderLayout.setVisibility(View.INVISIBLE);
            mainLayout.startAnimation(mainSlideIn);
        }
        else
        {
            mainLayout.startAnimation(mainSlideOut);
            sliderLayout.setVisibility(View.VISIBLE);
            if(kostylCounter == 0)
                sliderLayout.startAnimation(kostylSliderIn);
            else
                sliderLayout.startAnimation(sliderSlideIn);
        }
        kostylCounter++;
    }

    @Override
    public void onAnimationStart(Animation animation) {}

    @Override
    public void onAnimationEnd(Animation animation)
    {
        margin = (deviceWidth * 40) / 100;

        RelativeLayout.LayoutParams paramsMain = new RelativeLayout.LayoutParams(mainLayoutWidth, mainLayoutHeight);
        RelativeLayout.LayoutParams paramsSlider = new RelativeLayout.LayoutParams(mainLayoutWidth, mainLayoutHeight);

        //Now here we will actually move our view to the new position,
        // because animations just move the pixels not the view
        if(isMainSlideOut)
        {
            mainLayout.setLayoutParams(paramsMain);
            paramsSlider.leftMargin = -margin;
            paramsSlider.rightMargin = deviceWidth;
            sliderLayout.setLayoutParams(paramsSlider);
            isMainSlideOut = false;
        }
        else
        {
            paramsMain.leftMargin = margin;
            paramsMain.rightMargin = -margin;
            mainLayout.setLayoutParams(paramsMain);
            paramsSlider.leftMargin = 0;
            paramsSlider.rightMargin = deviceWidth*60/100;

            sliderLayout.setLayoutParams(paramsSlider);
            isMainSlideOut = true;
        }
    }

    @Override
    public void onAnimationRepeat(Animation animation) {}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.slide_menu);
    }
}