package com.uwo.tools.aibum.local.guide.producttour;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.uwo.tools.aibum.R;

/**
 * https://github.com/matrixxun/ProductTour
 * ProductTour实现很帅气d引导页
 */
public class SplashActivity extends AppCompatActivity {

    public static void actionStart(Activity activity) {
        activity.startActivity(new Intent(activity, SplashActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, ProductTourMainActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        finish();
                    }
                }, 300);
            }
        }, 500);
    }


}
