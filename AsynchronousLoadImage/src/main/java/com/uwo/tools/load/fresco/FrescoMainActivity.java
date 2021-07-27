package com.uwo.tools.load.fresco;

import android.app.Activity;
import android.os.Bundle;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.uwo.tools.load.R;

/**
 * Created by SRain on 2015/12/1.
 */
public class FrescoMainActivity extends Activity{

    private BaseAdapter adapter;
    private GridView gridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volley);
        init();
    }

    private void init() {
        initData();
        initView();
    }

    private void initData() {
        adapter = new FrescoAdapter(this);
    }

    private void initView() {
        TextView text = (TextView) findViewById(R.id.header);
        text.setText("Fresco");
        gridView = (GridView) findViewById(R.id.gridview);
        gridView.setAdapter(adapter);
    }
}
