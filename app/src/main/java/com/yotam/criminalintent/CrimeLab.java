package com.yotam.criminalintent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
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
        m_crimesNodes = new HashMap<>();
        m_crimes = new LinkedList<>();

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
        return new ArrayList<>(m_crimes);
    }

    public Crime getCrime(UUID crimeId)
    {
        ListIterator<Crime> itr = m_crimesNodes.get(crimeId);
        return itr.previous();
    }

    public int getCrimeIndex(Crime crime)
    {
        ListIterator<Crime> itr = m_crimesNodes.get(crime.GetId());
        if(null == itr)
        {
            return -1;
        }
        return itr.previousIndex();
    }

    public void addCrime(Crime crime)
    {
        int currIndex = m_crimes.size();
        m_crimes.addLast(crime);
        ListIterator<Crime> lastIterator = m_crimes.listIterator(currIndex);
        m_crimesNodes.put(crime.GetId(), lastIterator);
    }

    public void removeCrime(Crime crime)
    {
        UUID crimeId = crime.GetId();
        Iterator<Crime> crimeNode = m_crimesNodes.get(crimeId);
        m_crimesNodes.remove(crimeId);
        crimeNode.remove();
    }

    private static CrimeLab s_instance;

    private Map<UUID, ListIterator<Crime>> m_crimesNodes;

    private LinkedList<Crime> m_crimes;
}
