package com.uwo.tools.aibum.local.progressbar.avloading;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.uwo.tools.aibum.R;

/**
 * https://github.com/81813780/AVLoadingIndicatorView
 * <p/>
 * Nice loading animations for Android
 */
public class AVLoadMainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
    }

    public static void actionStart(Activity activity) {
        Intent intent = new Intent();
        intent.setClass(activity, AVLoadMainActivity.class);
        activity.startActivity(intent);
    }
}
