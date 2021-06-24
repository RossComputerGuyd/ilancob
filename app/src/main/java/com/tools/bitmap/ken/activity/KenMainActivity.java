package com.tools.bitmap.ken.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.tools.bitmap.R;

public class KenMainActivity extends Activity implements AdapterView.OnItemClickListener {

    private static final int POS_SINGLE_IMG = 0;
    private static final int POS_MULTI_IMG = 1;
    private static final int POS_FROM_URL = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ken_main);

        ListView listView = (ListView) findViewById(R.id.list);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.main_options, android.R.layout.simple_list_item_1);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case POS_SINGLE_IMG:
                startActivity(new Intent(this, SingleImageActivity.class));
                break;
            case POS_MULTI_IMG:
                startActivity(new Intent(this, MultiImageActivity.class));
                break;
            case POS_FROM_URL:
                startActivity(new Intent(this, FromURLActivity.class));
                break;
        }
    }
}
