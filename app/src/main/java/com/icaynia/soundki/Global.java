package com.icaynia.soundki;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.icaynia.soundki.Data.MusicFileManager;
import com.icaynia.soundki.Data.PlayListManager;
import com.icaynia.soundki.Data.RemoteDatabaseManager;
import com.icaynia.soundki.Data.UserManager;
import com.icaynia.soundki.Model.AlbumRes;
import com.icaynia.soundki.Model.ArtistRes;
import com.icaynia.soundki.Model.MusicDto;
import com.icaynia.soundki.Model.MusicRes;
import com.icaynia.soundki.Model.PlayList;
import com.icaynia.soundki.Model.PlayHistory;
import com.icaynia.soundki.Service.MusicService;
import com.icaynia.soundki.View.MusicRemoteController;

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

    public UserManager userManager;


    /* Firebase */
    public FirebaseAuth firebaseAuth;
    public FirebaseUser loginUser;

    private ServiceConnection musicServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(final ComponentName name, IBinder service)
        {
            MusicService.MusicBinder binder = (MusicService.MusicBinder) service;
            musicService = binder.getService();
            musicService.getPlayingMusic();

            musicService.mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    int songid = musicService.getPlayingMusic();
                    playNextMusic();

                    // TODO ??????????????????

                    MusicDto musicDto = mMusicManager.getMusicDto(songid+"");
                    PlayHistory playHistory = new PlayHistory();
                    playHistory.artist = MusicDto.replaceForInput(musicDto.getArtist());
                    playHistory.album = MusicDto.replaceForInput(musicDto.getAlbum());
                    playHistory.title = MusicDto.replaceForInput(musicDto.getTitle());

                    userManager.addHistory(playHistory);


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

        userManager = new UserManager();

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
        MusicDto musicDto = mMusicManager.getMusicDto(songId+"");

        UserManager userManager = new UserManager();
        userManager.setNowlistening(musicDto.getArtist(), musicDto.getAlbum(), musicDto.getTitle());


        musicService.playMusic(songId+"");
        updateController();


        MusicRes info = new MusicRes();
        ArtistRes arres = new ArtistRes();
        AlbumRes albumRes = new AlbumRes();

        RemoteDatabaseManager rdm = new RemoteDatabaseManager();

        DatabaseReference dr = rdm.getSongsReference()
                .child(MusicDto.replaceForInput(musicDto.getArtist()))
                .child(MusicDto.replaceForInput(musicDto.getAlbum()))
                .child(MusicDto.replaceForInput(musicDto.getTitle()));

        DatabaseReference ar = rdm.getSongsReference()
                .child(MusicDto.replaceForInput(musicDto.getArtist()));

        DatabaseReference br = rdm.getSongsReference()
                .child(MusicDto.replaceForInput(musicDto.getArtist()))
                .child(MusicDto.replaceForInput(musicDto.getAlbum()));

        dr.child("&info").setValue(info);
        dr.child("&play").push().setValue("icaynia");

        ar.child("&info").setValue(arres);

        br.child("&info").setValue(albumRes);
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
