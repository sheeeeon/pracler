package com.icaynia.soundki.Model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by icaynia on 14/02/2017.
 *
 *
 */

public class PlayList implements Serializable
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

    public void setName(String name)
    {
        this.playListName = name;
    }

    public void addPositionCount()
    {
        this.position = this.position + 1;
    }

    public void delPositionCount()
    {
        this.position = this.position - 1;
    }

    public void addItem(MusicDto dto)
    {
        list.add(dto.getUid_local());
    }

    public void addItem(int uid)
    {
        list.add(uid+"");
    }

    public void addItem(int uid, int position)
    {
        list.add(position, uid+"");
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
