package com.uwo.tools.aibum.local.datetime.circle;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.uwo.tools.aibum.R;


public class CircleMainActivity extends ActionBarActivity implements CircleTimerView.CircleTimerListener {

    public static void actionStart(Activity activity) {
        Intent intent = new Intent(activity, CircleMainActivity.class);
        activity.startActivity(intent);
    }

    CircleTimerView timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circle_main);
        timer = (CircleTimerView) findViewById(R.id.ctv);
        timer.setCircleTimerListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void start(View v) {
        timer.startTimer();
    }

    public void pause(View v) {
        timer.pauseTimer();
    }

    @Override
    public void onTimerStop() {
        Toast.makeText(this, "onTimerStop", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onTimerStart(int currentTime) {
        Toast.makeText(this, "onTimerStart", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onTimerPause(int currentTime) {
        Toast.makeText(this, "onTimerPause", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onTimerValueChanged(int time) {

    }

    @Override
    public void onTimerValueChange(int time) {

    }
}
