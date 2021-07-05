package com.tools.bitmap.drawable;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;

import com.tools.bitmap.R;
import com.tools.bitmap.drawable.view.CircleImageDrawable;


public class CircleImageDrawableActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawable_main);

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.mv);
        ImageView iv = (ImageView) findViewById(R.id.id_one);
        iv.setImageDrawable(new CircleImageDrawable(bitmap));
        iv = (ImageView) findViewById(R.id.id_two);
        iv.setImageDrawable(new CircleImageDrawable(bitmap));
    }
}
