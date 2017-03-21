package com.icaynia.soundki;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.facebook.FacebookSdk;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.icaynia.soundki.Activity.PlayerActivity;
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

    public PlayListManager playListManager;

    public PlayList nowPlayingList = new PlayList();

    public OnChangeListener onChangeListener = null;

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

            Log.e("Global", "onServiceConnected: called");

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

        Log.e("Global", "onCreate: called");
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

        setMusicNotification();
        if (onChangeListener != null)
        {
            onChangeListener.onChange();
        }
    }

    public void playPrevMusic()
    {
        musicService.pause();
        if (musicService.getPlayingMusicCurrentPosition() < 3000)
        {
            nowPlayingList.delPositionCount();
        }

        String nextmusic_uid = nowPlayingList.get(nowPlayingList.getPosition());
        if (nextmusic_uid != null)
        {
            playMusic(Integer.parseInt(nextmusic_uid));
        }

        if (onChangeListener != null)
        {
            onChangeListener.onChange();
        }

    }

    public void playNextMusic()
    {
        musicService.stop();
        nowPlayingList.addPositionCount();
        String nextmusic_uid = nowPlayingList.get(nowPlayingList.getPosition());
        if (nextmusic_uid != null)
        {
            playMusic(Integer.parseInt(nextmusic_uid));
        }
        if (onChangeListener != null)
        {
            onChangeListener.onChange();
        }
    }

    public void setMusicNotification()
    {
        Resources res = getResources();

        Intent notificationIntent = new Intent(this, PlayerActivity.class);
        notificationIntent.putExtra("notificationId", 9999); //전달할 값
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        int songId = musicService.getPlayingMusic();
        MusicDto musicDto = mMusicManager.getMusicDto(songId+"");
        Bitmap albumArt = mMusicManager.getAlbumImage(getApplicationContext(), Integer.parseInt(musicDto.getAlbumId()), 100);

        builder.setContentTitle("현재 Commit 중")
                .setContentText(musicDto.getArtist() + " - " + musicDto.getTitle() + " ")
                .setTicker(musicDto.getArtist() + " - " + musicDto.getTitle())
                .setSmallIcon(R.drawable.ic_headset_white)
                .setLargeIcon(albumArt)
                .setContentIntent(contentIntent)
                .setAutoCancel(false)
                .setWhen(System.currentTimeMillis())
                .setDefaults(Notification.DEFAULT_LIGHTS);



        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            builder.setCategory(Notification.CATEGORY_MESSAGE)
                    .setPriority(Notification.PRIORITY_HIGH)
                    .setVisibility(Notification.VISIBILITY_PUBLIC);
        }

        NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify(1234, builder.build());
    }


    public void setOnChangeListener(OnChangeListener listener)
    {
        this.onChangeListener = listener;
    }

    public interface OnChangeListener
    {
        void onChange();
    }

}
