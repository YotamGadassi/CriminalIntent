package com.yotam.criminalintent;

import java.util.Date;
import java.util.UUID;

public class Crime
{
    public UUID GetId()
    {
        return m_id;
    }

    public String GetTitle()
    {
        return m_title;
    }

    public Date GetDate()
    {
        return m_date;
    }

    public boolean IsSolved()
    {
        return m_solved;
    }

    private UUID m_id;

    public void SetTitle(String title)
    {
        m_title = title;
    }

    public void SetDate(Date date)
    {
        m_date = date;
    }

    public void SetSolved(boolean solved)
    {
        m_solved = solved;
    }

    private String m_title;
    private Date m_date;
    private boolean m_solved;

    public Crime()
    {
        m_id = UUID.randomUUID();
        m_date = new Date();
    }
}
