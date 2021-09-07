package com.uwo.tools.aibum.local.basic;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

import com.umeng.analytics.MobclickAgent;

/**
 * Created by SRain on 2015/12/11.
 */
public abstract class BasicActivity extends Activity {
    private Context mContext;
    private static String mPageName = "BasicActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        mContext = this;
        initUMen();
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
     * 初始化友盟
     */
    protected void initUMen() {
        MobclickAgent.setDebugMode(true);
        // SDK在统计Fragment时，需要关闭Activity自带的页面统计，
        // 然后在每个页面中重新集成页面统计的代码(包括调用了 onResume 和 onPause 的Activity)。
        MobclickAgent.openActivityDurationTrack(false);
        // MobclickAgent.setAutoLocation(true);
        // MobclickAgent.setSessionContinueMillis(1000);
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

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(mPageName);
        MobclickAgent.onResume(mContext);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(mPageName);
        MobclickAgent.onPause(mContext);
    }
}
