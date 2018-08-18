package com.example.x6.androidmachinetest.function.TimeSetting;

import android.os.Build;
import android.os.SystemClock;
import android.support.annotation.RequiresApi;

import java.util.Calendar;

/**
 * Created by 24179 on 2018/6/21.
 */

public class DateUtils {
    public DateUtils(){

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void setDate(int year, int month, int day) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, day);
        long when = c.getTimeInMillis();
        if (when / 1000 < Integer.MAX_VALUE) {
            SystemClock.setCurrentTimeMillis(when);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void setTime(int hourOfDay, int minute) {
        Calendar c = Calendar.getInstance();

        c.set(Calendar.HOUR_OF_DAY, hourOfDay);
        c.set(Calendar.MINUTE, minute);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        long when = c.getTimeInMillis();
        if (when / 1000 < Integer.MAX_VALUE) {
            SystemClock.setCurrentTimeMillis(when);
        }
    }
}
