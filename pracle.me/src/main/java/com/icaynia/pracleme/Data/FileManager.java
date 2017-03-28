package com.icaynia.pracleme.Data;

import android.content.Context;
import android.util.Log;

import com.icaynia.pracleme.Model.PlayList;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

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
        this.context = context;
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
            Log.d(TAG, "Playlist name isn't set.");
            return;
        }

        String filename = dirPath+"/playlist/"+playlist.getName()+".soundki";
        if (isFileAvailable(filename))
        {
            Log.d(TAG, "File is already created : " + filename);
            //return;
        }

        try
        {
            Log.e(TAG, "Save PlayList : "+filename);
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
            Log.e(TAG, "Load PlayList : "+filename);
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename));
            playlist = (PlayList) ois.readObject();
            ois.close();
        }
        catch (Exception e)
        {
            Log.e(TAG, "Something was wrong! 호출하는 파일이 없을 수도 있음 : " + filename);
            e.printStackTrace();
        }

        return playlist;
    }

    public ArrayList<String> getPlayLists()
    {
        ArrayList<String> fileList = new ArrayList<>();

        String path = dirPath+"/playlist";
        File directory = new File(path);
        File[] files = directory.listFiles();

        for (int i = 0; i < files.length; i++)
        {
            PlayList playList = loadPlayList(files[i].getName().replace(".soundki", ""));
            fileList.add(playList.getName());
        }

        return fileList;
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


