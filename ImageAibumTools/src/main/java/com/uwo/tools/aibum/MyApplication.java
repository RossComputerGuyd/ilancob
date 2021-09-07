package com.uwo.tools.aibum;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;

import com.uwo.tools.aibum.cropper2.FontUtils;
import com.uwo.tools.aibum.local.skin.SkinConfig;
import com.uwo.tools.aibum.local.skin.SkinPackageManager;

public class MyApplication extends Application {
	
	private static Context instance;
	public Bitmap cropped = null;
	
	@Override
	public void onCreate() {
		super.onCreate();

		String skinPath = SkinConfig.getInstance(this).getSkinResourcePath();
		if (!TextUtils.isEmpty(skinPath)) {
			// 如果已经换皮肤，那么第二次进来时，需要加载该皮肤
			SkinPackageManager.getInstance(this).loadSkinAsync(skinPath, null);
		}

		instance = getApplicationContext();
		// load custom font
		FontUtils.loadFont(getApplicationContext(), "Roboto-Light.ttf");
	}

	public static Context getCtx() {
		return instance;
	}
}
