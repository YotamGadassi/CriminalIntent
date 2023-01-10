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
        m_crimes = new ArrayList<>();
        for (int i = 0; i < 100; ++i)
        {
            Crime crime = new Crime();
            crime.SetTitle("Crime #" + i);
            crime.SetSolved(i % 2 == 0);
            crime.setRequiredPolice(i % 2 == 0);
            m_crimes.add(crime);
        }
    }

    private static CrimeLab s_instance;

    private List<Crime> m_crimes;

    public List<Crime> getCrimes()
    {
        return m_crimes;
    }

    public Crime getCrime(UUID crimeId)
    {
        for (Crime crime : m_crimes)
        {
            if (crimeId.equals(crime.GetId()))
            {
                return crime;
            }
        }
        return null;
    }
}
