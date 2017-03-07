package com.icaynia.soundki.Model;

import android.content.Intent;
import android.util.Log;

/**
 * Created by icaynia on 2017. 2. 8..
 *
 * 주로 로컬에서만 사용하며, 원격 데이터베이스에 저장할때는 MusicRes를 이용하여 내보낸다.
 */

public class MusicDto
{
    private String uid_local;
    private String uid_remote;
    private String album;
    private String album_id;
    private String title;
    private String artist;
    private int length; // millis.

    public void setUid_local(String uid_local)
    {
        this.uid_local = uid_local;
    }

    public void setUid_remote(String uid_remote)
    {
        this.uid_remote = (uid_remote);
    }

    public void setAlbum(String albumText)
    {
        this.album = (albumText);
    }

    public void setAlbumId(String album_id)
    {
        this.album_id = (album_id);
    }

    public void setTitle(String titleText)
    {
        this.title = (titleText);
    }

    public void setArtist(String artistText)
    {
        this.artist = (artistText);
    }

    public void setLength(int length)
    {
        this.length = length;
    }

    public String getUid_local()
    {
        return this.uid_local;
    }

    public String getUid_remote()
    {
        return (this.uid_remote);
    }

    public String getAlbum()
    {
        return (this.album);
    }

    public String getAlbumId()
    {
        return (this.album_id);
    }

    public String getTitle()
    {
        return this.title;
    }

    public String getArtist()
    {
        return this.artist;
    }

    public int getLength()
    {
        return this.length;
    }

    public static String replaceForInput(String string)
    {
        String str = string.replace(".", "&DT");
        str = str.replace("#", "&SH");
        str = str.replace("$", "&DO");
        str = str.replace("[", "&BL");
        str = str.replace("]", "&BR");

        Log.e("MusicDto", str);

        return str;
    }

    public static String replaceForOutput(String string)
    {
        String str = string.replace("&DT", ".");
        str = str.replace("&SH", "#" );
        str = str.replace("&DO", "$" );
        str = str.replace("&BL", "[" );
        str = str.replace("&BR", "]" );

        Log.e("MusicDto", str);

        return str;
    }


}
