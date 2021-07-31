package com.uwo.tools.load.hy;

import android.support.v4.app.Fragment;

import com.uwo.tools.load.R;

public class HYMainActivity extends AbsSingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new ListImgsFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_single_fragment;
    }

}
