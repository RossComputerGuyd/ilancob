package com.uwo.tools.aibum.local.basic;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

/**
 * Created by SRain on 2015/12/11.
 */
public abstract class BasicActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        init();
    }

    protected void init() {
        initView();
        initData();
    }

    /**
     * 初始化控件
     */
    protected abstract void initView();

    /**
     * 初始化数据
     */
    protected abstract void initData();

    @SuppressWarnings("unchecked")
    protected <T> T getParam(String key) {
        Intent intent = this.getIntent();
        Object obj = intent.getExtras().get(key);
        if (obj == null) {
            return null;
        }
        return (T) obj;
    }

    protected boolean hasParam(String key) {
        return this.getIntent().hasExtra(key);
    }
}
