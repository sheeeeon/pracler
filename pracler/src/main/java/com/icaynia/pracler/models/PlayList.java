package com.icaynia.pracler.models;

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
    private String playListDescription = "플레이리스트 설명";
    private ArrayList<String> list = new ArrayList<>(); // 곡의 uid가 들어감.
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

    public void setDescription(String description)
    {
        this.playListDescription = description;
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
        if (list.size() < position)
        {
            this.addItem(uid);
        }
        else
        {
            list.add(position, uid + "");
        }
    }

    public void addItem(String uid)
    {
        list.add(uid);
    }

    public String getName()
    {
        return this.playListName;
    }

    public String getDescription()
    {
        return this.playListDescription;
    }

    public int size()
    {
        return list.size();
    }

    public String get(int position)
    {
        if (position >= list.size()) {
            return null;
        }
        String uid = list.get(position);
        return uid;
    }

    public void remove(int index)
    {
        list.remove(index);
    }

}
