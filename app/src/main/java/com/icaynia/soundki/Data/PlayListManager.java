package com.icaynia.soundki.Data;

import android.content.Context;

import java.util.ArrayList;

/**
 * Created by icaynia on 14/02/2017.
 *
 * 플레이 리스트를 관리하는 곳.
 */

public class PlayListManager
{
    private Context context;

    public PlayListManager(Context context)
    {
        this.context = context;
    }

    //로컬에 있는 플레이리스트를 모두 가져온다.
    public ArrayList<String> getPlayListList()
    {
        ArrayList<String> playListList = new ArrayList<String>();
        playListList.add("TEST_playlist");
        return playListList;
    }



}
