package com.icaynia.soundki;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;

import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.icaynia.soundki.Activity.LoginActivity;
import com.icaynia.soundki.Activity.MainActivity;
import com.icaynia.soundki.Activity.PlayListActivity;
import com.icaynia.soundki.Activity.PlayerActivity;
import com.icaynia.soundki.Data.LocalDatabaseManager;
import com.icaynia.soundki.Data.MusicFileManager;
import com.icaynia.soundki.Data.PlayListManager;
import com.icaynia.soundki.Data.RemoteDatabaseManager;
import com.icaynia.soundki.Model.MusicDto;
import com.icaynia.soundki.Model.MusicRes;
import com.icaynia.soundki.Model.PlayList;
import com.icaynia.soundki.Service.MusicService;
import com.icaynia.soundki.View.MusicRemoteController;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;4

/**
 * Created by icaynia on 2017. 2. 8..
 */

public class Global extends Application
{
    public int SORT_NAME = 0;
    public int SORT_ALBUM = 1;
    public int SORT_ARTIST = 2;
    public int SORT_LENGTH = 3;
    public int SORT_PLAYCOUNT = 4;

    public MusicService musicService;
    public Intent musicServiceIntent;
    public MusicFileManager mMusicManager;

    public MusicRemoteController mainActivityMusicRemoteController;

    public PlayListManager playListManager;

    public PlayList nowPlayingList = new PlayList();

    public OnCompleteListener completeListener = null;


    /* Firebase */
    public FirebaseAuth firebaseAuth;
    public FirebaseUser loginUser;

    private ServiceConnection musicServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service)
        {
            MusicService.MusicBinder binder = (MusicService.MusicBinder) service;
            musicService = binder.getService();
            musicService.getPlayingMusic();

            musicService.mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    int songid = musicService.getPlayingMusic();
                    playNextMusic();

                    MusicDto musicDto = mMusicManager.getMusicDto(songid+"");
                    MusicRes info = new MusicRes();
                    RemoteDatabaseManager rdm = new RemoteDatabaseManager();

                    DatabaseReference dr = rdm.getSongsReference()
                            .child(MusicDto.replaceForInput(musicDto.getArtist()))
                            .child(MusicDto.replaceForInput(musicDto.getAlbum()))
                            .child(MusicDto.replaceForInput(musicDto.getTitle()));

                    dr.child("info").setValue(info);
                    dr.child("play").push().setValue("icaynia");

                    // love
                    //dr.child("loves").push().setValue("icaynia");

                }
            });
            updateController();

        }

        @Override
        public void onServiceDisconnected(ComponentName name)
        {

        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        FacebookSdk.sdkInitialize(getApplicationContext());
        if (musicServiceIntent == null) {
            musicServiceIntent = new Intent(this, MusicService.class);
            bindService(musicServiceIntent, musicServiceConnection, Context.BIND_AUTO_CREATE);
            startService(musicServiceIntent);
        }

        mMusicManager = new MusicFileManager(getApplicationContext());
        playListManager = new PlayListManager(this);

        firebaseAuth = FirebaseAuth.getInstance();
        loginUser = firebaseAuth.getCurrentUser();

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
        updateController();
    }

    public void playPrevMusic()
    {
        if (musicService.getPlayingMusicCurrentPosition() < 3000)
        {
            nowPlayingList.delPositionCount();
        }

        musicService.playing = false;
        String nextmusic_uid = nowPlayingList.get(nowPlayingList.getPosition());
        if (nextmusic_uid != null)
        {
            musicService.playMusic(nextmusic_uid);
        }

        if (completeListener != null)
        {
            completeListener.onComplete();
        }

        updateController();
    }

    public void playNextMusic()
    {
        nowPlayingList.addPositionCount();
        musicService.playing = false;
        String nextmusic_uid = nowPlayingList.get(nowPlayingList.getPosition());
        if (nextmusic_uid != null)
        {
            musicService.playMusic(nextmusic_uid);
        }
        if (completeListener != null)
        {
            completeListener.onComplete();
        }
        updateController();
    }


    public void updateController()
    {
        if (mainActivityMusicRemoteController != null && musicService != null)
        {
            int songId = musicService.getPlayingMusic();
            if (songId == 0) return;
            Log.e("global.updateController", musicService.getPlayingMusic()+"");
            MusicDto song = mMusicManager.getMusicDto(songId+"");
            Bitmap albumArt = mMusicManager.getAlbumImage(getApplicationContext(), Integer.parseInt(song.getAlbumId()), 100);
            mainActivityMusicRemoteController.updateSongInfo(albumArt, song.getArtist(), song.getTitle());
        }
    }

    public void setOnCompleteListener(OnCompleteListener listener)
    {
        this.completeListener = listener;
    }

    public interface OnCompleteListener
    {
        void onComplete();
    }

}
