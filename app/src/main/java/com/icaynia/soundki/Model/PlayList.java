package com.icaynia.soundki.Model;

import java.util.ArrayList;

/**
 * Created by icaynia on 14/02/2017.
 *
 *
 */

public class PlayList
{
    private String playListName = "이름 없는 재생목록";
    private ArrayList<String> list = new ArrayList<>();
    private int position = 0;

    public PlayList()
    {

    }

    public int getPosition()
    {
        return position;
    }

    public void setPosition(int position)
    {
        this.position = position;
    }

    public void addItem(MusicDto dto)
    {
        list.add(dto.uid_local);
    }

    public void addItem(int uid)
    {
        list.add(uid+"");
    }

    public void addItem(String uid)
    {
        list.add(uid);
    }

    public String getName()
    {
        return this.playListName;
    }

    public int size()
    {
        return list.size();
    }

    public String get(int position)
    {
        return list.get(position);
    }

    public void remove(int index)
    {
        list.remove(index);
    }

}
