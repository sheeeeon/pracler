package com.icaynia.pracler.Data;

import android.content.Context;
import android.util.Log;

import com.icaynia.pracler.models.LocalPlayHistory;
import com.icaynia.pracler.models.MusicList;
import com.icaynia.pracler.models.PlayCount;
import com.icaynia.pracler.models.SongLike;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by icaynia on 21/03/2017.
 */

public class LocalLikeManager
{
    private Context context;
    private Realm realm;

    public LocalLikeManager(Context context)
    {
        this.context = context;
        realm = Realm.getDefaultInstance();
    }

    public int getSongLikeCount()
    {
        final RealmResults<SongLike> results = realm.where(SongLike.class).findAll();
        Log.e("realm_count", results.size() + "");
        return results.size();
    }

    public boolean isLike(int uid)
    {
        final SongLike results = realm.where(SongLike.class).equalTo("local_uid", uid).findFirst();
        if (results != null)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public void setLike(int uid, boolean state)
    {
        boolean state_before = isLike(uid);
        realm.beginTransaction();

        if (state == true && state_before == false)
        {
            SongLike like = new SongLike();
            like.local_uid = uid;
            final SongLike manage_like = realm.copyToRealm(like);
        }
        else if (state == false && state_before == true)
        {
            final SongLike results = realm.where(SongLike.class).equalTo("local_uid", uid).findFirst();
            results.deleteFromRealm();
        }
        realm.commitTransaction();
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

    public void addSongLike(LocalPlayHistory playHistory)
    {
        realm.beginTransaction();
        final LocalPlayHistory manage_tmp = realm.copyToRealm(playHistory);
        realm.commitTransaction();
    }

    public void deleteHistory(String regdate)
    {

    }
}
