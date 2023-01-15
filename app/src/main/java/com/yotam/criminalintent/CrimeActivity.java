package com.yotam.criminalintent;

import android.content.Context;
import android.content.Intent;

import androidx.fragment.app.Fragment;

import java.util.UUID;

public class CrimeActivity extends SingleFragmentActivity
{
    private static final String EXTRA_CRIME_ID = "com.yotam.criminalintent.crime_id";

    public static Intent newIntent(Context packageContext, UUID crimeId)
    {
        return new Intent(packageContext, CrimeActivity.class).putExtra(EXTRA_CRIME_ID, crimeId);
    }

    @Override
    protected Fragment createFragment()
    {
        UUID crimeId = (UUID)getIntent().getSerializableExtra(EXTRA_CRIME_ID);
        return CrimeFragment.newInstance(crimeId);
    }
}