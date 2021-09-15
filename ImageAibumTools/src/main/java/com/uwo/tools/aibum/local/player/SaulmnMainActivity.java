/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.uwo.tools.aibum.local.player;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.uwo.tools.aibum.local.player.saulmn.MediaFragment;


public class SaulmnMainActivity extends AppCompatActivity {

    public static void actionStart(Activity activity) {
        activity.startActivity(new Intent(activity, SaulmnMainActivity.class));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initMediaFragment();
    }

    private void initMediaFragment() {
        getSupportFragmentManager().beginTransaction()
                .add(android.R.id.content, MediaFragment.newInstance())
                .commit();
    }
}
