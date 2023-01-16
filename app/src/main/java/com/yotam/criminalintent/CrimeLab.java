package com.yotam.criminalintent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

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
        m_crimes = new HashMap<>(100);
        for (int i = 0; i < 100; ++i)
        {
            Crime crime = new Crime();
            crime.SetTitle("Crime #" + i);
            crime.SetSolved(i % 2 == 0);
            crime.setRequiredPolice(i % 2 == 0);
            m_crimes.put(crime.GetId(), crime);
        }
    }

    private static CrimeLab s_instance;

    private Map<UUID, Crime> m_crimes;

    public List<Crime> getCrimes()
    {
        return new ArrayList<>(m_crimes.values());
    }

    public Crime getCrime(UUID crimeId)
    {
        return m_crimes.get(crimeId);
    }
}
