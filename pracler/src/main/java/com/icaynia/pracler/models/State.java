package com.icaynia.pracler.models;

/**
 * Created by icaynia on 16/03/2017.
 */

public class State
{
    private boolean like = false;

    public void setState(boolean state)
    {
        this.like = state;
    }

    public boolean getState()
    {
        return like;
    }
}
