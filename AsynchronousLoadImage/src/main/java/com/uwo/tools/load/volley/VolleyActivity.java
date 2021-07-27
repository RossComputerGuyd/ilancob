package com.uwo.tools.load.volley;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.uwo.tools.load.R;


/**
 * Created by SRain on 2015/11/30.
 * <p/>
 * 使用Volley框架加载网络图片
 */
public class VolleyActivity extends Activity implements View.OnClickListener {

    private BaseAdapter adapter;
    private GridView gridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_volley);
        init();
    }

    private void init() {
        initData();
        initView();
    }

    private void initData() {
        
    }

    private void initView() {
        TextView text = (TextView) findViewById(R.id.header);
        text.setOnClickListener(this);
        gridView = (GridView) findViewById(R.id.gridview);
//        gridView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.header:
                if (adapter != null) {
                    adapter = null;
                }
                String[] strs = new String[]{"Volley使用ImageRequest下载图片", "Volley使用ImageLoader", "Volley使用NetworkImageView加载图片"};
                final Dialog alertDialog = new AlertDialog.Builder(this).
                        setTitle("请选择查看方式")
                        .setItems(strs, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0:
                                        adapter = new VolleyGridAdapter(VolleyActivity.this, 1);
                                        break;
                                    case 1:
                                        adapter = new VolleyGridAdapter(VolleyActivity.this, 2);
                                        break;
                                    case 2:
                                        adapter = new VolleyGridNetAdapter(VolleyActivity.this, 3);
                                        break;
                                }
                                gridView.setAdapter(adapter);
                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub
                            }
                        }).create();
                alertDialog.show();
                break;
        }
    }
}
