package com.uwo.tools.aibum.localphoto;
import java.util.List;

import com.uwo.tools.aibum.R;
import com.uwo.tools.aibum.localphoto.PhotoFolder.OnPageLodingClickListener;
import com.uwo.tools.aibum.localphoto.PhotoGrid.OnPhotoClickListener;
import com.uwo.tools.aibum.localphoto.bean.PhotoInfo;
import com.uwo.tools.aibum.localphoto.bean.PhotoList;
import com.uwo.tools.aibum.localphoto.utils.CheckImageLoaderConfiguration;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;

@SuppressLint("NewApi")
public class AlbumActivity extends FragmentActivity implements OnPhotoClickListener, OnPageLodingClickListener {

	private PhotoFolder photoFolder;
	private PhotoGrid photoGrid;
	private RelativeLayout rlLayout;
	private FragmentManager manager;
	private int backInt = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_album);
		rlLayout=(RelativeLayout) findViewById(R.id.ll_container);
		manager = getSupportFragmentManager();

		photoFolder = new PhotoFolder();

		FragmentTransaction transaction = manager.beginTransaction();
		transaction.add(R.id.ll_container, photoFolder);
		transaction.addToBackStack(null);
		transaction.commit();
	}

	@Override
	protected void onStart() {
		super.onStart();
		CheckImageLoaderConfiguration.checkImageLoaderConfiguration(this);
	}

	@Override
	public void onPageLodingClickListener(List<PhotoInfo> list) {
		// TODO Auto-generated method stub
		FragmentTransaction transaction = manager.beginTransaction();
		photoGrid = new PhotoGrid();
		Bundle args = new Bundle();
		PhotoList photo = new PhotoList();
		photo.setList(list);
		args.putSerializable("list", photo);
		photoGrid.setArguments(args);
		transaction = manager.beginTransaction();
		transaction.hide(photoFolder).commit();
		transaction = manager.beginTransaction();
		transaction.add(R.id.ll_container, photoGrid);
		transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
		transaction.addToBackStack(null);
		// Commit the transaction
		transaction.commit();
		backInt++;
	}

	@Override
	public void onPhotoClickListener(String path_absolute) {
		rlLayout.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);  
		FragmentTransaction transaction = manager.beginTransaction();
		ImageMatrix img = new ImageMatrix();
		Bundle args = new Bundle();
		args.putSerializable("path_absolute", path_absolute);
		img.setArguments(args);
		transaction = manager.beginTransaction();
		transaction.hide(photoGrid).commit();
		transaction = manager.beginTransaction();
		transaction.add(R.id.ll_container, img);
		transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
		transaction.addToBackStack(null);
		// Commit the transaction
		transaction.commit();
		backInt++;
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode == KeyEvent.KEYCODE_BACK&&backInt==0){
			finish();
		}else if(keyCode == KeyEvent.KEYCODE_BACK&&backInt==1){
			backInt--;
			FragmentTransaction transaction = manager.beginTransaction();
			transaction.show(photoFolder).commit();
			manager.popBackStack(backInt, 0);
		}else if(keyCode == KeyEvent.KEYCODE_BACK&&backInt==2){
			rlLayout.setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
			backInt--;
			FragmentTransaction transaction = manager.beginTransaction();
			transaction.show(photoGrid).commit();
			manager.popBackStack(backInt, 0);
		}
		return false;
	}
}
