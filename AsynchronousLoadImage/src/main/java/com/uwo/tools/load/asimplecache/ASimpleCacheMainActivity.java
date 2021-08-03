package com.uwo.tools.load.asimplecache;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.uwo.tools.load.R;

/**
 * @ClassName:
 * @Description: 主界面
 * @Author Yoson Hao
 * @WebSite www.haoyuexing.cn
 * @Email haoyuexing@gmail.com
 * @Date 2013-8-8 下午2:08:47
 * ASimpleCache图片异步加载缓存类库
 * http://github.com/yangfuhai/ASimpleCache
 */
public class ASimpleCacheMainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_un_main);
    }

    public void string(View v) {
        startActivity(new Intent().setClass(this, SaveStringActivity.class));
    }

    public void jsonobject(View v) {
        startActivity(new Intent().setClass(this, SaveJsonObjectActivity.class));
    }

    public void jsonarray(View v) {
        startActivity(new Intent().setClass(this, SaveJsonArrayActivity.class));
    }

    public void bitmap(View v) {
        startActivity(new Intent().setClass(this, SaveBitmapActivity.class));
    }

    public void media(View v) {
        startActivity(new Intent().setClass(this, SaveMediaActivity.class));
    }

    public void drawable(View v) {
        startActivity(new Intent().setClass(this, SaveDrawableActivity.class));
    }

    public void object(View v) {
        startActivity(new Intent().setClass(this, SaveObjectActivity.class));
    }

    public void about(View v) {
        startActivity(new Intent().setClass(this, AboutActivity.class));
    }

}
