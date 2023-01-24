package com.yotam.criminalintent;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.yotam.criminalintent.database.CrimeBaseHelper;
import com.yotam.criminalintent.database.CrimeDbSchema;

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
        List crimes = new ArrayList<>();
        CrimeCursorWrapper cursorWrapper = queryCrimes(null,null);
        try
        {
            cursorWrapper.moveToFirst();
            while(false == cursorWrapper.isAfterLast())
            {
                crimes.add(cursorWrapper.getCrime());
                cursorWrapper.moveToNext();
            }
        }
        finally
        {
            cursorWrapper.close();
        }

        return crimes;
    }

    public Crime getCrime(UUID crimeId)
    {
        CrimeCursorWrapper cursorWrapper = queryCrimes(CrimeDbSchema.CrimeTable.Cols.UUID + " = ?"
                , new String[]{crimeId.toString()});

        try
        {
            if(cursorWrapper.getCount() == 0)
            {
                return null;
            }
            cursorWrapper.moveToFirst();
            return cursorWrapper.getCrime();
        }
        finally
        {
            cursorWrapper.close();
        }

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
        ContentValues contentValues = getCrimeContentValues(crime);
        m_database.insert(CrimeDbSchema.CrimeTable.NAME, null, contentValues);
    }

    public void updateCrime(Crime crime)
    {
        String uuidString = crime.GetId().toString();
        ContentValues values = getCrimeContentValues(crime);

        m_database.update(CrimeDbSchema.CrimeTable.NAME, values,
                CrimeDbSchema.CrimeTable.Cols.UUID + " = ?",
                new String[] {uuidString});
    }

    public void removeCrime(UUID crimeId)
    {
        String crimeIdString = crimeId.toString();
        m_database.delete(CrimeDbSchema.CrimeTable.NAME
                , CrimeDbSchema.CrimeTable.Cols.UUID + " = ?"
                , new String[]{crimeIdString});
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

    private static ContentValues getCrimeContentValues(Crime crime)
    {
        ContentValues contentValues = new ContentValues();
        contentValues.put(CrimeDbSchema.CrimeTable.Cols.UUID, crime.GetId().toString());
        contentValues.put(CrimeDbSchema.CrimeTable.Cols.TITLE, crime.GetTitle());
        contentValues.put(CrimeDbSchema.CrimeTable.Cols.DATE, crime.GetDate().getTime());
        contentValues.put(CrimeDbSchema.CrimeTable.Cols.SOLVED, crime.IsSolved());
        contentValues.put(CrimeDbSchema.CrimeTable.Cols.SUSPECT, crime.GetSuspect());

        return contentValues;
    }

    private CrimeCursorWrapper queryCrimes(String whereClause, String[] whereArgs)
    {
        Cursor cursor = m_database.query(CrimeDbSchema.CrimeTable.NAME
                , null
                , whereClause
                , whereArgs
                ,null
                ,null
                ,null);

        return new CrimeCursorWrapper(cursor);
    }
}
