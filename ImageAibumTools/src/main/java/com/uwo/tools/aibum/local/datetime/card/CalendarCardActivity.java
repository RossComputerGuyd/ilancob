package com.uwo.tools.aibum.local.datetime.card;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.uwo.tools.aibum.R;
import com.uwo.tools.aibum.local.LocalActivity;

/**
 * https://github.com/kenumir/android-calendar-card
 */
public class CalendarCardActivity extends Activity {

	public static void actionStart(LocalActivity activity){
		Intent intent = new Intent(activity, CalendarCardActivity.class);
		activity.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_card_main);
	}
	
	public void handleSample1(View v) {
		startActivity(new Intent(this, Sample1.class));
	}

	public void handleSample2(View v) {
		startActivity(new Intent(this, Sample2.class));
	}
	
}
