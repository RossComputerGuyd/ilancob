package com.tools.bitmap;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.cuter.CuterBitmap;

/**
 * Created by SRain on 2015/11/26.
 */
public class CuterActivity extends Activity implements View.OnClickListener {
    private CuterBitmap cview;
    int id = R.drawable.ggf;
    private RelativeLayout content;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cuter_main);
        content = (RelativeLayout) findViewById(R.id.content);
        cview = new CuterBitmap(this);
        // 图片源，支持uri
        cview.setSource(id);
        // 画布背景
        cview.setBgColor(Color.BLUE);
        // 裁剪选框颜色
        cview.setBorderColor(Color.GRAY);
        // 裁剪选框的边框粗细
        cview.setBorderSize(3);
        // 裁剪选框的大小
        cview.setBorderWH(200, 200);
        // 圆形裁剪选框半径
        // cview.setRadius(120);
        // 设置移动图片，固定选框
        // cview.setMovePic(true);
        // 矩形选框四个角，用于拖动时缩放选框大小
        // Bitmap bit = BitmapFactory.decodeResource(getResources(),
        // R.drawable.icon_data_select);
        // cview.setRoundBitmap(bit);
        // 矩形选框四个角，用于拖动时缩放选框大小
        cview.setRoundColor(Color.BLACK);
        cview.setRoundSize(14);
        // 设置是否可拖动缩放
        // cview.setZoom(false);
        content.addView(cview);
        findViewById(R.id.save).setOnClickListener(this);
        findViewById(R.id.cencal).setOnClickListener(this);
        findViewById(R.id.conver).setOnClickListener(this);
        findViewById(R.id.change).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.save:
                try {
                    Bitmap bitmap = cview.getBitmap();
                    ImageView imv = new ImageView(this);
                    imv.setImageBitmap(bitmap);
                    content.addView(imv);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.conver:
                // 90度旋转
                cview.conver();
                break;
            case R.id.change:
                cview.changeSource(++id);
                if (id == R.drawable.ggy) {
                    id = R.drawable.ggf;
                    --id;
                }
                break;
            case R.id.cencal:
                CuterActivity.this.finish();
                break;
        }
    }
}
