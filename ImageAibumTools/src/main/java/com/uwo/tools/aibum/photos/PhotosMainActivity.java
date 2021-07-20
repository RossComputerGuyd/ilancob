package com.uwo.tools.aibum.photos;

import java.io.File;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.view.Window;
import android.widget.GridView;

import com.uwo.tools.aibum.R;

/**
 * 照片墙主活动，使用GridView展示照片墙。
 * 
 */
public class PhotosMainActivity extends Activity {

	/**
	 * 用于展示照片墙的GridView
	 */
	private GridView mPhotoWall;

	/**
	 * GridView的适配器
	 */
	private PhotoWallAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		mPhotoWall = (GridView) findViewById(R.id.photo_wall);
		adapter = new PhotoWallAdapter(this, 0, Images.imageUrls, mPhotoWall);
		mPhotoWall.setAdapter(adapter);
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		// 退出程序时结束所有的下载任务
		adapter.cancelAllTasks();
	}

}
