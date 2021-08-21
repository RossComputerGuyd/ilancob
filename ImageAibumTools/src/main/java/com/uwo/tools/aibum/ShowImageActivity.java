package com.uwo.tools.aibum;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by SRain on 2015/12/8.
 *
 * 显示一张图片
 */
public class ShowImageActivity extends Activity {

    private ImageView imageView;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_image);
        init();
    }

    private void init() {
        String oldURI = this.getIntent().getStringExtra("oldUri");
        String oldPath = this.getIntent().getStringExtra("oldPath");
        String cropUri = this.getIntent().getStringExtra("cropUri");
        String realFilePath = this.getIntent().getStringExtra("realFilePath");
        Uri uri = Uri.parse(cropUri);
        String show = "oldURI:" + oldURI + "\n" + "cropUri:" + cropUri + "\n";
        show += "oldPath:" + oldPath + "\n" + "realFilePath:" + realFilePath;
        imageView = (ImageView) findViewById(R.id.showImage);
        imageView.setImageURI(uri);
        textView = (TextView) findViewById(R.id.textShow);
        textView.setText(show);
    }

    public static void actionStart(Context context, String oldPath, String oldUri, String realFilePath, String cropUri) {
        Intent intent = new Intent();
        intent.putExtra("oldPath", oldPath);
        intent.putExtra("oldUri", oldUri);
        intent.putExtra("realFilePath", realFilePath);
        intent.putExtra("cropUri", cropUri);
        intent.setClass(context, ShowImageActivity.class);
        context.startActivity(intent);
    }
}
