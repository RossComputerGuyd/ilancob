package com.uwo.tools.aibum.local.progressbar.loadindicators;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;

import com.uwo.tools.aibum.R;


/**
 * https://github.com/adrian110288/LoadIndicators
 */
public class LoaderMainActivity extends FragmentActivity {

    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;
    private CustomPagerTabStrip mPagerTabStrip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loader_main);

        mPager = (ViewPager) findViewById(R.id.pager);
        mPagerTabStrip = (CustomPagerTabStrip) findViewById(R.id.pager_title_strip);
        mPagerTabStrip.setTabIndicatorColor(getResources().getColor(R.color.primary));
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        mPager.setPageTransformer(true, new DepthPageTransformer());

        sendAppOpen();
    }

    private void sendAppOpen() {
//        Tracker t = ((MyApplication) getApplication()).getTracker();
//        t.send(new HitBuilders.EventBuilder().
//                setCategory("APP OPENED").
//                setLabel(new Date().toString()).
//                build());
    }

    public static void actionStart(Activity activity) {
        Intent intent = new Intent();
        intent.setClass(activity, LoaderMainActivity.class);
        activity.startActivity(intent);
    }
}
