package com.icaynia.soundki.Model;

import java.util.ArrayList;

/**
 * Created by icaynia on 24/02/2017.
 */

public class MusicList
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
}
