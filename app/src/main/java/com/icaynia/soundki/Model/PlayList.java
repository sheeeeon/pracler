package com.icaynia.soundki.Model;

import java.util.ArrayList;

/**
 * Created by icaynia on 14/02/2017.
 */

public class PlayList
{
    private String playListName = "이름 없는 재생목록";
    private ArrayList<String> list = new ArrayList<>();

    public PlayList()
    {

    }

    public ArrayList<String> getList()
    {
        return this.list;
    }

    public void addItem(String uid)
    {
        list.add(uid);
    }

    public String getName()
    {
        return this.playListName;
    }


}
