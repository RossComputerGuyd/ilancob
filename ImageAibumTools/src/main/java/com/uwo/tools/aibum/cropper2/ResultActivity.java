package com.uwo.tools.aibum.cropper2;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.uwo.tools.aibum.MyApplication;
import com.uwo.tools.aibum.R;


public class ResultActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        // apply custom font
        FontUtils.setFont((ViewGroup)findViewById(R.id.layout_root));
        // get cropped bitmap from Application
        Bitmap cropped = ((MyApplication)getApplication()).cropped;
        // set cropped bitmap to ImageView
        ((ImageView)findViewById(R.id.result_image)).setImageBitmap(cropped);
    }
}
