package com.yotam.criminalintent;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import java.util.List;
import java.util.UUID;

public class CrimePagerActivity extends AppCompatActivity
{
    private ViewPager m_viewPager;
    private Button m_firstButton;
    private Button m_lastButton;

    private List<Crime> m_crimeList;

    private static final String EXTRA_CRIME_INDEX = "com.yotam.criminalintent.crime_id";

    public static Intent newIntent(Context packageContext, int crime_index)
    {
        return new Intent(packageContext, CrimePagerActivity.class).putExtra(EXTRA_CRIME_INDEX, crime_index);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crime_pager);

        int crimeIndex = (int) getIntent().getSerializableExtra(EXTRA_CRIME_INDEX);

        m_viewPager = findViewById(R.id.crimes_container);
        m_crimeList = CrimeLab.getInstance().getCrimes();

        FragmentManager fragmentManager = getSupportFragmentManager();
        m_viewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager)
        {
            @NonNull
            @Override
            public Fragment getItem(int position)
            {
                Crime crime = m_crimeList.get(position);
                return CrimeFragment.newInstance(crime.GetId());
            }

            @Override
            public int getCount()
            {
                return m_crimeList.size();
            }
        });

        m_firstButton = findViewById(R.id.first_crime_button);
        m_firstButton.setOnClickListener(v -> m_viewPager.setCurrentItem(0));


        m_lastButton = findViewById(R.id.last_crime_button);
        m_lastButton.setOnClickListener(v -> m_viewPager.setCurrentItem(m_viewPager.getAdapter().getCount() - 1));
        m_viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener()
        {
            @Override
            public void onPageSelected(int position)
            {
                super.onPageSelected(position);
                setEnableForButtons(position);
            }
        });

        m_viewPager.setCurrentItem(crimeIndex);
        setEnableForButtons(crimeIndex);
    }

    private void setEnableForButtons(int index)
    {
        m_firstButton.setEnabled(true);
        m_lastButton.setEnabled(true);
        if (index == 0)
        {
            m_firstButton.setEnabled(false);
        }
        if (index == m_viewPager.getAdapter().getCount() - 1)
        {
            m_lastButton.setEnabled(false);
        }
    }
}
