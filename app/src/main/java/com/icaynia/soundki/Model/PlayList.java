package com.icaynia.soundki.Model;

import java.util.ArrayList;

/**
 * Created by icaynia on 14/02/2017.
 */

public class PlayList
{
    private String playListName = "이름 없는 재생목록";
    private ArrayList<MusicDto> list = new ArrayList<>();

    public PlayList()
    {

    }

    public void addItem(MusicDto dto)
    {
        list.add(dto);
    }

    public String getName()
    {
        return this.playListName;
    }

    public int size()
    {
        return list.size();
    }

    public MusicDto get(int position)
    {
        return list.get(position);
    }

    public void remove(int index)
    {
        list.remove(index);
    }

}
