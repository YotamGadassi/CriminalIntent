package com.yotam.criminalintent;

import java.util.Date;
import java.util.UUID;

public class Crime
{
    public boolean isRequiredPolice()
    {
        return m_requiredPolice;
    }

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
    public String GetSuspect()
    {
        return m_suspect;
    }

    public boolean IsSolved()
    {
        return m_solved;
    }
    public Crime()
    {
        this(UUID.randomUUID());
    }

    public Crime(UUID id)
    {
        m_id = id;
        m_date = new Date();
        m_title = "";
    }

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
    public void SetSuspect(String suspect)
    {
        this.m_suspect = m_suspect;
    }
    public void setRequiredPolice(boolean requiredPolice)
    {
        m_requiredPolice = requiredPolice;
    }

    private String m_title;
    private Date m_date;
    private boolean m_solved;
    private boolean m_requiredPolice;
    private UUID m_id;
    private String m_suspect;


}
