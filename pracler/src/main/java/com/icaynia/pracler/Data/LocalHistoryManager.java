package com.icaynia.pracler.Data;

import android.content.Context;
import android.util.Log;

import com.icaynia.pracler.models.LocalPlayHistory;
import com.icaynia.pracler.models.MusicList;
import com.icaynia.pracler.models.PlayCount;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by icaynia on 21/03/2017.
 */

public class LocalHistoryManager
{
    private Context context;
    private Realm realm;
    public LocalHistoryManager(Context context)
    {
        this.context = context;
        realm = Realm.getDefaultInstance();
    }

    public int getHistoryCount()
    {
        final RealmResults<LocalPlayHistory> results = realm.where(LocalPlayHistory.class).findAll();
        Log.e("realm_test", results.size() + "");
        return results.size();
    }

    public LocalPlayHistory getHistory(String regdate)
    {
        final LocalPlayHistory results = realm.where(LocalPlayHistory.class).equalTo("Regdate", regdate).findFirst();
        return results;
    }

    public ArrayList<LocalPlayHistory> getHistoryLast(int num)
    {
        final RealmResults<LocalPlayHistory> results = realm.where(LocalPlayHistory.class).findAllSorted("Regdate", Sort.DESCENDING).sort("Regdate", Sort.DESCENDING);
        return null;
    }

    public ArrayList<PlayCount> getHistoryDesending()
    {
        MusicFileManager musicFileManager = new MusicFileManager(context);
        MusicList list = musicFileManager.getMusicList();

        ArrayList<PlayCount> pcount = new ArrayList<>();

        for (int i = 0; i < list.size(); i++)
        {
            PlayCount playCount = new PlayCount();
            playCount.uid = Integer.parseInt(list.getItem(i).getUid_local());
            playCount.count = this.getHistorySongCount(playCount.uid);
            pcount.add(i, playCount);
        }

        // bubble sort
        for (int i = 0; i < list.size() - 1; i++)
        {
            for (int j = i; j < list.size(); j++)
            {
                if (pcount.get(i).count < pcount.get(j).count)
                {
                    PlayCount tmp = pcount.get(i);
                    pcount.set(i, pcount.get(j));
                    pcount.set(j, tmp);
                }
            }

            Log.e("getHistoryDecending", pcount.get(i).uid + " - " + pcount.get(i).count);
        }
        return pcount;
    }

    public int getHistorySongCount(int local_uid)
    {
        return realm.where(LocalPlayHistory.class).equalTo("uid", local_uid).findAll().size();
    }

    public void addHistory(LocalPlayHistory playHistory)
    {
        realm.beginTransaction();
        final LocalPlayHistory manage_tmp = realm.copyToRealm(playHistory);
        realm.commitTransaction();
    }

    public void deleteHistory(String regdate)
    {

    }
}
