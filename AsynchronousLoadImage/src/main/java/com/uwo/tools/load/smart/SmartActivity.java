package com.uwo.tools.load.smart;

import android.app.Activity;
import android.os.Bundle;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.uwo.tools.load.R;

/**
 * Created by SRain on 2015/12/1.
 * <p/>
 * 替代ImageView
 * http://loopj.com/android-smart-image-view/
 */
public class SmartActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volley);
        init();
    }

    private void init() {
        TextView textView = (TextView) findViewById(R.id.header);
        textView.setText("smart-image-view");
        GridView gridView = (GridView) findViewById(R.id.gridview);
        BaseAdapter adapter = new SmartAdapter(this);
        gridView.setAdapter(adapter);
    }
}
