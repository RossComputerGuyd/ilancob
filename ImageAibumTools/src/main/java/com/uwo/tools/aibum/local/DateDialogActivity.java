package com.uwo.tools.aibum.local;

import android.content.Intent;
import android.os.Bundle;

import com.uwo.tools.aibum.R;
import com.uwo.tools.aibum.local.basic.BasicActivity;
import com.uwo.tools.aibum.local.datetime.silkcal.DatePickerController;
import com.uwo.tools.aibum.local.datetime.silkcal.DayPickerView;
import com.uwo.tools.aibum.local.datetime.silkcal.SimpleMonthAdapter;

/**
 * Created by SRain on 2015/12/28.
 * <p/>
 * 时间对话框
 */
public class DateDialogActivity extends BasicActivity implements DatePickerController {

    public static void actionStart(LocalActivity activity) {
        Intent intent = new Intent();
        intent.setClass(activity, DateDialogActivity.class);
        activity.startActivity(intent);
    }

    private DayPickerView calendarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_dialog);
    }

    @Override
    protected void initView() {
        calendarView = (DayPickerView) findViewById(R.id.calendar_view);
        calendarView.setController(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    public int getMaxYear() {
        return 0;
    }

    @Override
    public void onDayOfMonthSelected(int year, int month, int day) {

    }

    @Override
    public void onDateRangeSelected(SimpleMonthAdapter.SelectedDays<SimpleMonthAdapter.CalendarDay> selectedDays) {

    }
}
