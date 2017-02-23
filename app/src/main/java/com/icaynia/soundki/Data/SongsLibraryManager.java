package com.icaynia.soundki.Data;

/**
 *　Created by icaynia.
 *
 *  리모트 서버에서 곡의 정보를 가져오거나 내보내는 것.
 */

import com.icaynia.soundki.Model.MusicDto;

import java.util.ArrayList;

public class SongsLibraryManager
{
    public SongsLibraryManager()
    {
        
    }
    
    public MusicDto getSongLibrary(String songUid)
    {
        return null;
    }
    
    public ArrayList<String> findSong(String title, String artist, String albumTitle)
    {
        ArrayList<String> findUidList = new ArrayList<String>();
        return findUidList;
    }
    
    public void addSong(MusicDto song)
    {
        
    }
    
}
