package com.tools.bitmap;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;

import com.tools.bitmap.bitmap.BitmapRotating;

/**
 * Created by SRain on 2015/11/25.
 */
public class EasyRoundActivity extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawable_main);

        ImageView imageView = (ImageView) findViewById(R.id.id_one);
        Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.personal_center_photo);
        imageView.setImageBitmap(BitmapRotating.toRoundCorner(b, 30));

        ImageView image = (ImageView) findViewById(R.id.id_two);
        image.setImageBitmap(BitmapRotating.getCropped2Bitmap(b));
    }
}
