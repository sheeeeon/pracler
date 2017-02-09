package com.icaynia.soundki;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.IBinder;

import com.icaynia.soundki.Activity.MainActivity;
import com.icaynia.soundki.Data.MusicFileManager;
import com.icaynia.soundki.Model.MusicDto;
import com.icaynia.soundki.Service.MusicService;
import com.icaynia.soundki.View.MusicRemoteController;

/**
 * Created by icaynia on 2017. 2. 8..
 */

public class Global extends Application
{
    public MusicService musicService;
    public Intent musicServiceIntent;
    public MusicFileManager mMusicManager;

    public MusicRemoteController mainActivityMusicRemoteController;

    private ServiceConnection musicServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service)
        {
            MusicService.MusicBinder binder = (MusicService.MusicBinder) service;
            musicService = binder.getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name)
        {

        }
    };

    @Override
    public void onCreate() {
        super.onCreate();

        if (musicServiceIntent == null) {
            musicServiceIntent = new Intent(this, MusicService.class);
            bindService(musicServiceIntent, musicServiceConnection, Context.BIND_AUTO_CREATE);
            startService(musicServiceIntent);
        }

        mMusicManager = new MusicFileManager(getApplicationContext());

    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    public void playMusic(int songId)
    {
        musicService.playMusic(songId+"");

        if (mainActivityMusicRemoteController != null)
        {
            MusicDto song = mMusicManager.getMusicDto(songId+"");
            Bitmap albumArt = mMusicManager.getAlbumImage(getApplicationContext(), Integer.parseInt(song.albumid), 100);
            mainActivityMusicRemoteController.updateSongInfo(albumArt, song.artist, song.title);
        }
    }
}
