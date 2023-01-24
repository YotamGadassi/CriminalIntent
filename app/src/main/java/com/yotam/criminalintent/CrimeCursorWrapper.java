package com.yotam.criminalintent;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.yotam.criminalintent.database.CrimeDbSchema;

import java.util.Date;
import java.util.UUID;

public class CrimeCursorWrapper extends CursorWrapper
{
    /**
     * Creates a cursor wrapper.
     *
     * @param cursor The underlying cursor to wrap.
     */
    public CrimeCursorWrapper(Cursor cursor) {
        super(cursor);
    }
    public Crime getCrime()
    {
        String uuidString = getString(getColumnIndex(CrimeDbSchema.CrimeTable.Cols.UUID));
        String title = getString(getColumnIndex(CrimeDbSchema.CrimeTable.Cols.TITLE));
        long date = getLong(getColumnIndex(CrimeDbSchema.CrimeTable.Cols.DATE));
        int isSolved = getInt(getColumnIndex(CrimeDbSchema.CrimeTable.Cols.SOLVED));
        String suspect = getString(getColumnIndex(CrimeDbSchema.CrimeTable.Cols.SUSPECT));

        UUID crimeId = UUID.fromString(uuidString);
        Crime crime = new Crime(crimeId);
        crime.SetTitle(title);
        crime.SetDate(new Date(date));
        crime.SetSolved(isSolved != 0);
        crime.SetSuspect(suspect);

        return crime;
    }

}
