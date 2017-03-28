package com.icaynia.pracler.provider;

import io.realm.Realm;

/**
 * Created by icaynia on 28/03/2017.
 */

public class PlaySongCount
{
    private Realm realm;

    public PlaySongCount()
    {
        realm = Realm.getDefaultInstance();
    }


}
