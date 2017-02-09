package com.icaynia.soundki;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.os.IBinder;

import com.icaynia.soundki.Service.MusicService;

/**
 * Created by icaynia on 2017. 2. 8..
 */

public class Global extends Application
{
    public MusicService musicService;
    public Intent musicServiceIntent;

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
}
