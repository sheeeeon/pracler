package com.icaynia.pracler.models;

import java.util.ArrayList;

/**
 * Created by icaynia on 29/03/2017.
 */

public class SongList
{
    private ArrayList<Song> list = new ArrayList<>();

    public SongList()
    {

    }

    public Song get(int position)
    {
        return list.get(position);
    }

    public void add(Song song)
    {
        list.add(song);
    }

    public int size()
    {
        return list.size();
    }

}
