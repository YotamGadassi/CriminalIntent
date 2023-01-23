package com.yotam.criminalintent;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CrimeLab
{
    public static CrimeLab getInstance()
    {
        if (null == s_instance)
        {
            s_instance = new CrimeLab();
        }

        return s_instance;
    }

    private CrimeLab()
    {
        m_crimeList = new ArrayList<>();

        for (int i = 0; i < 5; ++i)
        {
            Crime crime = new Crime();
            crime.SetTitle("Crime #" + i);
            crime.SetSolved(i % 2 == 0);
            crime.setRequiredPolice(i % 2 == 0);
            addCrime(crime);
        }
    }
    public List<Crime> getCrimes()
    {
        return new ArrayList<>(m_crimeList);
    }

    public Crime getCrime(UUID crimeId)
    {
        int crimeIndex = getCrimeIndex(crimeId);
        if(crimeIndex < 0)
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
            if(crimeId.equals(currCrimeId))
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
        if(0 <= crimeIndex)
        {
            m_crimeList.remove(crimeIndex);
        }
    }

    private static CrimeLab s_instance;

    private ArrayList<Crime> m_crimeList;

}
