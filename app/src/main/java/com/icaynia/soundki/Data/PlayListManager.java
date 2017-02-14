package com.icaynia.soundki.Data;

import android.content.Context;

import java.util.ArrayList;

/**
 * Created by icaynia on 14/02/2017.
 */

public class PlayListManager
{
    private Context context;

    public PlayListManager(Context context)
    {
        this.context = context;
    }

    public ArrayList<String> getPlayListList()
    {
        ArrayList<String> playListList = new ArrayList<String>();
        playListList.add("TEST_playlist");
        return playListList;
    }



}
