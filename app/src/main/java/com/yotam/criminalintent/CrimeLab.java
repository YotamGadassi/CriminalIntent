package com.yotam.criminalintent;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.yotam.criminalintent.database.CrimeBaseHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CrimeLab
{
    public static CrimeLab getInstance(Context context)
    {
        if (null == s_instance)
        {
            s_instance = new CrimeLab(context);
        }

        return s_instance;
    }

    public List<Crime> getCrimes()
    {
        return new ArrayList<>(m_crimeList);
    }

    public Crime getCrime(UUID crimeId)
    {
        int crimeIndex = getCrimeIndex(crimeId);
        if (crimeIndex < 0)
        {
            return null;
        }
        return m_crimeList.get(crimeIndex);
    }

    public int getCrimeIndex(UUID crimeId)
    {
        for (int i = 0; i < m_crimeList.size(); ++i)
        {
            Crime currCrime = m_crimeList.get(i);
            UUID currCrimeId = currCrime.GetId();
            if (crimeId.equals(currCrimeId))
            {
                return i;
            }
        }
        return -1;
    }

    public void addCrime(Crime crime)
    {
        m_crimeList.add(crime);
    }

    public void removeCrime(Crime crime)
    {
        UUID crimeId = crime.GetId();
        int crimeIndex = getCrimeIndex(crimeId);
        if (0 <= crimeIndex)
        {
            m_crimeList.remove(crimeIndex);
        }
    }

    private CrimeLab(Context context)
    {
        m_crimeList = new ArrayList<>();
        m_context = context.getApplicationContext();
        m_database = new CrimeBaseHelper(m_context).getWritableDatabase();
    }

    private static CrimeLab s_instance;

    private ArrayList<Crime> m_crimeList;

    private Context m_context;
    private SQLiteDatabase m_database;

}
