package com.uwo.tools.aibum.local.progressbar.waveview;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.SeekBar;

import com.uwo.tools.aibum.R;

/**
 * Created by SRain on 6/17/14.
 * <p/>
 * https://github.com/john990/WaveView
 * WaveView实现水波注满效果，用于显示当前进度。
 */
public class WaveMainActivity extends Activity {

    private SeekBar seekBar;
    private WaveView waveView;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wave);

        seekBar = (SeekBar) findViewById(R.id.seek_bar);
        waveView = (WaveView) findViewById(R.id.wave_view);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                waveView.setProgress(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    public static void actionStart(Activity activity) {
        Intent intent = new Intent(activity, WaveMainActivity.class);
        activity.startActivity(intent);
    }
}