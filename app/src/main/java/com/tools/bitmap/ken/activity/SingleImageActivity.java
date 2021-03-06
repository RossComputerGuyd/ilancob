/*
 * Copyright 2014 Flavio Faria
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.tools.bitmap.ken.activity;

import android.os.Bundle;

import com.tools.bitmap.R;
import com.tools.bitmap.ken.view.KenBurnsView;


public class SingleImageActivity extends KenBurnsActivity {

    private KenBurnsView mImg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_image);
        mImg = (KenBurnsView) findViewById(R.id.img);
    }


    @Override
    protected void onPlayClick() {
        mImg.resume();
    }


    @Override
    protected void onPauseClick() {
        mImg.pause();
    }
}
