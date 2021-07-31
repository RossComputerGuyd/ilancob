package com.uwo.tools.load.picasso;

import android.app.Activity;
import android.os.Bundle;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.uwo.tools.load.R;
import com.uwo.tools.load.volley.VolleyGridAdapter;

/**
 * Created by SRain on 2015/12/1.
 */
public class PicassoMainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volley);
        init();
    }

    private void init() {
        initView();
    }

    private void initView() {
        TextView textView = (TextView) findViewById(R.id.header);
        textView.setText("Picasso");
        GridView gridView = (GridView) findViewById(R.id.gridview);
        BaseAdapter adapter = new VolleyGridAdapter(this, 3);
        gridView.setAdapter(adapter);
    }
}
