package com.jpventura.footballscores.app;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.text.format.Time;

import com.jpventura.footballscores.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MyPageAdapter extends FragmentStatePagerAdapter {
    private FragmentActivity mActivity;
    private List<MainScreenFragment> mViewFragments;

    public MyPageAdapter(FragmentActivity activity, FragmentManager fm) {
        super(fm);
        mActivity = activity;
        mViewFragments = new ArrayList<>();
    }

    public void addItem(MainScreenFragment fragment) {
        mViewFragments.add(fragment);
    }

    @Override
    public Fragment getItem(int i) {
        return mViewFragments.get(i);
    }

    @Override
    public int getCount() {
        return mViewFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return getDayName(mActivity,System.currentTimeMillis()+((position-2)*86400000));
    }

    public String getDayName(Context context, long dateInMillis) {
        // If the date is today, return the localized version of "Today" instead of the actual
        // day name.

        Time t = new Time();
        t.setToNow();
        int julianDay = Time.getJulianDay(dateInMillis, t.gmtoff);
        int currentJulianDay = Time.getJulianDay(System.currentTimeMillis(), t.gmtoff);

        if (julianDay == currentJulianDay) {
            return context.getString(R.string.today).toUpperCase(Locale.getDefault());
        } else if ( julianDay == currentJulianDay +1 ) {
            return context.getString(R.string.tomorrow).toUpperCase(Locale.getDefault());
        } else if ( julianDay == currentJulianDay -1) {
            return context.getString(R.string.yesterday).toUpperCase(Locale.getDefault());
        } else {
            Time time = new Time();
            time.setToNow();
            // Otherwise, the format is just the day of the week (e.g "Wednesday".
            SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE");
            return dayFormat.format(dateInMillis).toUpperCase(Locale.getDefault());
        }
    }
}
