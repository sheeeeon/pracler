package com.icaynia.soundki.Data;

import android.content.Context;

import com.icaynia.soundki.Model.PlayHistory;

import java.util.ArrayList;

/**
 * Created by icaynia on 21/03/2017.
 */

public class LocalHistoryManager
{
    private Context context;
    private LocalDatabaseManager dm;
    public LocalHistoryManager(Context context)
    {
        this.context = context;
        dm = new LocalDatabaseManager(this.context);
    }

    public int getHistoryCount()
    {

        return 0;
    }

    public PlayHistory getHistory(String regdate)
    {
        return null;
    }

    public ArrayList<PlayHistory> getHistoryLast(int num)
    {
        return null;
    }

    public void setHistory(PlayHistory playHistory)
    {

    }

    public void deleteHistory(String regdate)
    {

    }
}
