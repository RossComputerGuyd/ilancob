package com.uwo.tools.aibum.local;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.uwo.tools.aibum.R;
import com.uwo.tools.aibum.local.datetime.slide.SlideDateTimeListener;
import com.uwo.tools.aibum.local.datetime.slide.SlideDateTimePicker;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by SRain on 2015/12/28.
 */
public class SlideActivity extends FragmentActivity {

    public static void actionStart(LocalActivity activity) {
        Intent intent = new Intent(activity, SlideActivity.class);
        activity.startActivity(intent);
    }

    private SimpleDateFormat mFormatter = new SimpleDateFormat("MMMM dd yyyy hh:mm aa");
    private Button mButton;

    private SlideDateTimeListener listener = new SlideDateTimeListener() {

        @Override
        public void onDateTimeSet(Date date) {
            Toast.makeText(SlideActivity.this, mFormatter.format(date), Toast.LENGTH_SHORT).show();
        }

        // Optional cancel listener
        @Override
        public void onDateTimeCancel() {
            Toast.makeText(SlideActivity.this, "Canceled", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample);

        mButton = (Button) findViewById(R.id.button);
        mButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new SlideDateTimePicker.Builder(getSupportFragmentManager())
                        .setListener(listener)
                        .setInitialDate(new Date())
                                //.setMinDate(minDate)
                                //.setMaxDate(maxDate)
                                //.setIs24HourTime(true)
                                //.setTheme(SlideDateTimePicker.HOLO_DARK)
                                //.setIndicatorColor(Color.parseColor("#990000"))
                        .build()
                        .show();
            }
        });
    }
}
