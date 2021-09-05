package com.uwo.tools.aibum;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;

import com.uwo.tools.aibum.cropper2.FontUtils;

public class MyApplication extends Application {
	
	private static Context instance;
	public Bitmap cropped = null;
	
	@Override
	public void onCreate() {
		super.onCreate();
		instance = getApplicationContext();
		// load custom font
		FontUtils.loadFont(getApplicationContext(), "Roboto-Light.ttf");
	}

	public static Context getCtx() {
		return instance;
	}
}
