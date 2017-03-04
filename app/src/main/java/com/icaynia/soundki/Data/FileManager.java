package com.icaynia.soundki.Data;

import android.content.Context;
import android.media.audiofx.BassBoost;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.icaynia.soundki.Model.PlayList;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Created by icaynia on 01/03/2017.
 */

public class FileManager
{
    public Context context;
    public String TAG = "FileManager";

    String dirPath;

    public FileManager(Context context)
    {
        init();
    }

    public void init()
    {
        dirPath = context.getFilesDir().getPath();
        File file = new File(dirPath);
        if( !file.exists() ) {
            file.mkdirs();
        }
        file = new File(dirPath+"/playlist");
        if( !file.exists() ) {
            file.mkdirs();
        }
    }

    public void savePlaylist(PlayList playlist)
    {
        if (playlist.getName().equals(""))
        {
            Log.e(TAG, "Playlist name isn't set.");
            return;
        }

        String filename = dirPath+"/playlist/"+playlist.getName()+".soundki";
        if (isFileAvailable(filename))
        {
            Log.e(TAG, "File is already created : " + filename);
            return;
        }

        try
        {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename));
            oos.writeObject(playlist);
            oos.close();
        }
        catch (Exception e)
        {
            Log.e(TAG, "Something was wrong!");
            e.printStackTrace();
        }
    }

    public PlayList loadPlayList(String playlistName)
    {
        if (playlistName.equals(""))
        {
            Log.e(TAG, "Playlist name isn't set.");
        }
        PlayList playlist = null;
        String filename = dirPath+"/playlist/"+playlistName+".soundki";
        try
        {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename));
            playlist = (PlayList) ois.readObject();
            ois.close();
        }
        catch (Exception e)
        {
            Log.e(TAG, "Something was wrong!");
            e.printStackTrace();
        }

        return playlist;
    }

    public boolean isFileAvailable(String file)
    {
        File tmpFile = new File(file);
        if( tmpFile.exists() ) {
            return true;
        }
        return false;
    }
}


