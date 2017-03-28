package com.icaynia.pracler.models;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by icaynia on 24/02/2017.
 */

public class MusicList implements Serializable
{
    private ArrayList<MusicDto> list = new ArrayList<>();

    public MusicList()
    {

    }

    public void addItem(MusicDto musicDto)
    {
        list.add(musicDto);
    }

    public MusicDto getItem(int index)
    {
        return list.get(index);
    }

    public int size()
    {
        return list.size();
    }

    public ArrayList<MusicDto> getList()
    {
        return list;
    }
}
