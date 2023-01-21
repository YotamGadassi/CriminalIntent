package com.yotam.criminalintent;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DatePickerFragment extends DialogFragment
{
    public static final String DATE_FOR_RESULT_KEY = "DateKeyForResult";

    private Button m_okButton;
    private DatePicker m_datePicker;

    public static DatePickerFragment newInstance()
    {
        return new DatePickerFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.dialog_date, container, false);
        m_datePicker = view.findViewById(R.id.dialog_date_picker);

        Intent intent = getActivity().getIntent();
        Date date = DatePickerActivity.getDateFromIntent(intent);
        initDatePicker(date, m_datePicker);

        m_okButton = view.findViewById(R.id.date_picker_button);
        m_okButton.setOnClickListener((View v) ->
                                      {
                                          Date date1 = getDate(m_datePicker);

                                          FragmentActivity parentActivity = getActivity();
                                          Intent intent1 = parentActivity.getIntent();
                                          setDateForIntent(intent1, date1);
                                          parentActivity.setResult(Activity.RESULT_OK, intent1);
                                          parentActivity.finish();
                                      });
        return view;
    }

    private void initDatePicker(Date date, DatePicker datePicker)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        datePicker.init(year, month, day, null);
    }

    private Date getDate(DatePicker datePicker)
    {
        int year = datePicker.getYear();
        int month = datePicker.getMonth();
        int day = datePicker.getDayOfMonth();

        return new GregorianCalendar(year, month, day).getTime();
    }

    public static void setDateForIntent(Intent intent, Date date)
    {
        intent.putExtra(DATE_FOR_RESULT_KEY, date);
    }
}
