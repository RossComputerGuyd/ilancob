package com.uwo.tools.load.xutils;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.uwo.tools.load.R;
import com.uwo.tools.load.volley.VolleyGridAdapter;

/**
 * Created by SRain on 2015/11/30.
 */
public class XUtilsMainActivity extends Activity implements View.OnClickListener {

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
        adapter = new VolleyGridAdapter(this, 1);
    }

    private void initView() {
        TextView text = (TextView) findViewById(R.id.header);
        text.setText("XUtils");
        text.setOnClickListener(this);
        gridView = (GridView) findViewById(R.id.gridview);
        gridView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {

    }
}
