package com.icaynia.pracler.services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.icaynia.pracler.Global;
import com.icaynia.pracler.remote.models.PraclerAlert;

/**
 * Created by icaynia on 2017. 2. 9..
 */

public class AlertService extends Service
{
    private Global global;
    /**
     *  NOTE :
     *  The variable of 'playing' changes the following state:
     *  when it playing something = true.
     *  when it paused = false.
     *  when it stopped = false.
     */
    private boolean playing = false;
    private OnAddedAlertListener listener;
    private String songId;

    //binder
    private final IBinder musicBind = new AlertBinder();

    Runnable task = new Runnable() {
        public void run(){
            try
            {

            }
            catch (Exception e) {

            }
        }
    };

    public void setOnAddedAlertListener(OnAddedAlertListener listener)
    {
        this.listener = listener;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        global = (Global) getApplicationContext();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("test", "onStartCommand");

        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public IBinder onBind(Intent intent) {
        return musicBind;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    //binder
    public class AlertBinder extends Binder
    {
        public AlertService getService() {
            return AlertService.this;
        }
    }

    public boolean isPlaying()
    {
        return this.playing;
    }


    public int getPlayingMusic()
    {
        if (songId == null)
        {
            return 0;
        }
        return Integer.parseInt(songId);
    }

    public interface OnAddedAlertListener
    {
        void onAdded(PraclerAlert alert);
    }

}
