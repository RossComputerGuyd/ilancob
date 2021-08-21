package com.uwo.tools.aibum.imagescan;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.uwo.tools.aibum.R;
import com.uwo.tools.aibum.cropper.MyCropperActivity;

import java.util.List;

public class ShowGridImageActivity extends Activity implements AdapterView.OnItemClickListener {
	private GridView mGridView;
	private List<String> list;
	private ChildAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.show_image_activity);
		
		mGridView = (GridView) findViewById(R.id.child_grid);
		list = getIntent().getStringArrayListExtra("data");
		
		adapter = new ChildAdapter(this, list, mGridView);
		mGridView.setAdapter(adapter);
		mGridView.setOnItemClickListener(this);
	}

	@Override
	public void onBackPressed() {
		Toast.makeText(this, "选中 " + adapter.getSelectItems().size() + " item", Toast.LENGTH_LONG).show();
		super.onBackPressed();
	}


	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		MyCropperActivity.actionStart(ShowGridImageActivity.this, list.get(position));
	}
}
