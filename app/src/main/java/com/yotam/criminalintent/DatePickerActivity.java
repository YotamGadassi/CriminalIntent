package com.yotam.criminalintent;

import android.content.Context;
import android.content.Intent;

import androidx.fragment.app.Fragment;

import java.util.Date;

public class DatePickerActivity extends SingleFragmentActivity
{
    private static final String DATE_FOR_PICKER_KEY = "DateKeyForPicker";

    public static Intent newIntent(Context packageContext, Date date)
    {
        return new Intent(packageContext, DatePickerActivity.class).putExtra(DATE_FOR_PICKER_KEY, date);
    }

    public static Date getDateFromIntent(Intent intent)
    {
        return (Date)intent.getSerializableExtra(DATE_FOR_PICKER_KEY);
    }

    @Override
    protected Fragment createFragment()
    {
        return DatePickerFragment.newInstance();
    }
}
