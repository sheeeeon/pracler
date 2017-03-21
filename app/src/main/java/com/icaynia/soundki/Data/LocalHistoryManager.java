package com.icaynia.soundki.Data;

import android.content.Context;
import android.util.Log;

import com.icaynia.soundki.Model.LocalPlayHistory;
import com.icaynia.soundki.Model.PlayHistory;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

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

    public PlayHistory getHistory(String regdate)
    {
        final RealmResults<LocalPlayHistory> results = realm.where(LocalPlayHistory.class).equalTo("Regdate", regdate).findAll();
        Log.e("realm_test", results.size() + "");
        return null;
    }

    public ArrayList<PlayHistory> getHistoryLast(int num)
    {
        return null;
    }

    public void setHistory(LocalPlayHistory playHistory)
    {
        realm.beginTransaction();
        final LocalPlayHistory manage_tmp = realm.copyToRealm(playHistory);
        realm.commitTransaction();
    }

    public void deleteHistory(String regdate)
    {

    }
}
