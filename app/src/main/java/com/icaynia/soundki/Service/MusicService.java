package com.icaynia.soundki.Service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.TimedText;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.provider.MediaStore;
import android.util.Log;

import com.icaynia.soundki.Model.MusicDto;

/**
 * Created by icaynia on 2017. 2. 9..
 */

public class MusicService extends Service
{
    public MediaPlayer mediaPlayer = new MediaPlayer();
    /**
     *  NOTE :
     *  The variable of 'playing' changes the following state:
     *  when it playing something = true.
     *  when it paused = false.
     *  when it stopped = false.
     */
    public boolean playing = false;
    public int position;

    private String songId;

    //binder
    private final IBinder musicBind = new MusicBinder();

    Runnable task = new Runnable() {
        public void run(){
            try {
                Uri musicURI = Uri.withAppendedPath(
                        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, songId);
                mediaPlayer.reset();
                mediaPlayer.setDataSource(getApplicationContext(), musicURI);
                mediaPlayer.prepare();
                mediaPlayer.start();

                mediaPlayer.getCurrentPosition();

                Log.e("MusicService", "getCurrentPosition:\n" + mediaPlayer.getCurrentPosition()+
                        "\n\n getDuration():\n"+mediaPlayer.getDuration());
            }
            catch (Exception e) {
                Log.e("SimplePlayer.", e.getMessage());
            }
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                playing = false;
            }
        });

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
        mediaPlayer.stop();
        mediaPlayer.release();
        return false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    //binder
    public class MusicBinder extends Binder
    {
        public MusicService getService() {
            return MusicService.this;
        }
    }

    public void playMusic(MusicDto music) {
        new Thread(task).start();
        playing = true;
    }

    public void playMusic(String songId)
    {
        this.songId = songId;
        new Thread(task).start();
        playing = true;
        /** for test */
        getPlayingMusicCurrentPosition();
    }

    public void start() {
        mediaPlayer.start();
        playing = true;
    }

    public void pause() {
        mediaPlayer.pause();
        playing = false;
    }

    public int getPlayingMusic()
    {
        if (songId == null)
        {
            return 0;
        }
        return Integer.parseInt(songId);
    }

    public int getPlayingMusicDuration()
    {
        return mediaPlayer.getDuration();
    }

    public int getPlayingMusicCurrentPosition()
    {
        return mediaPlayer.getCurrentPosition();
    }
}
