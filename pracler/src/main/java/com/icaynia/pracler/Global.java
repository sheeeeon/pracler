package com.icaynia.pracler;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.facebook.FacebookSdk;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.icaynia.pracler.Service.AlertService;
import com.icaynia.pracler.activities.PlayerActivity;
import com.icaynia.pracler.Data.LocalHistoryManager;
import com.icaynia.pracler.Data.LocalLikeManager;
import com.icaynia.pracler.Data.MusicFileManager;
import com.icaynia.pracler.Data.PlayListManager;
import com.icaynia.pracler.Data.RemoteDatabaseManager;
import com.icaynia.pracler.Data.UserManager;
import com.icaynia.pracler.models.AlbumRes;
import com.icaynia.pracler.models.ArtistRes;
import com.icaynia.pracler.models.LocalPlayHistory;
import com.icaynia.pracler.models.MusicDto;
import com.icaynia.pracler.models.MusicRes;
import com.icaynia.pracler.models.PlayList;
import com.icaynia.pracler.models.PlayHistory;
import com.icaynia.pracler.Service.MusicService;
import com.icaynia.pracler.models.User;
import com.icaynia.pracler.remote.FirebaseUserManager;
import com.icaynia.pracler.remote.listener.OnCompleteGetFirebaseUserListener;
import com.icaynia.pracler.remote.models.PraclerAlert;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import io.realm.Realm;

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

    public AlertService alertService;
    public MusicService musicService;
    public Intent musicServiceIntent;
    public Intent alertServiceIntent;
    public MusicFileManager mMusicManager;

    public PlayListManager playListManager;

    public PlayList nowPlayingList = new PlayList();

    public OnChangeListener onChangeListener = null;
    public LocalHistoryManager localHistoryManager;
    public LocalLikeManager localLikeManager;

    public UserManager userManager;

    public OnMusicFinishListener finishListener;
    private PlayStateChangeListener playStateChangeListener;


    /* Firebase */
    public String loginUid;
    public FirebaseAuth firebaseAuth;
    public FirebaseUser loginUser;

    private ServiceConnection musicServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(final ComponentName name, IBinder service)
        {
            MusicService.MusicBinder binder = (MusicService.MusicBinder) service;
            musicService = binder.getService();
            musicService.getPlayingMusic();

            musicService.mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener()
            {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer)
                {

                }
            });

            Log.e("Global", "onServiceConnected: called");

        }

        @Override
        public void onServiceDisconnected(ComponentName name)
        {

        }
    };

    private ServiceConnection alertServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(final ComponentName name, IBinder service)
        {
            AlertService.AlertBinder binder = (AlertService.AlertBinder) service;
            alertService = binder.getService();
            alertService.getPlayingMusic();

            alertService.setOnAddedAlertListener(new AlertService.OnAddedAlertListener()
            {
                @Override
                public void onAdded(PraclerAlert alert)
                {
                    newAlert(alert);
                }
            });

            Log.e("Global", "onServiceConnected: called");

        }

        @Override
        public void onServiceDisconnected(ComponentName name)
        {

        }
    };

    public void newAlert(final PraclerAlert alert)
    {
        Intent notificationIntent = new Intent(this, PlayerActivity.class);
        notificationIntent.putExtra("notificationId", 3333); //전달할 값
        final PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        final NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        int songId = musicService.getPlayingMusic();
        MusicDto musicDto = mMusicManager.getMusicDto(songId+"");
        //Bitmap albumArt = mMusicManager.getAlbumImage(getApplicationContext(), Integer.parseInt(musicDto.getAlbumId()), 100);
        FirebaseUserManager.getUser(alert.user_uid, new OnCompleteGetFirebaseUserListener()
        {
            @Override
            public void onComplete(User user)
            {
                builder.setContentTitle(alert.messages)
                        .setContentText(alert.messages)
                        .setTicker(alert.messages)
                        .setSmallIcon(R.drawable.ic_headset_white)
                        .setContentIntent(contentIntent)
                        .setAutoCancel(false)
                        .setWhen(System.currentTimeMillis());



                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    builder.setCategory(Notification.CATEGORY_MESSAGE)
                            .setPriority(Notification.PRIORITY_HIGH)
                            .setVisibility(Notification.VISIBILITY_PUBLIC);
                }

                NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                nm.notify(3333, builder.build());
            }
        });

    }

    @Override
    public void onCreate() {
        super.onCreate();

        Realm.init(getApplicationContext());
        FacebookSdk.sdkInitialize(getApplicationContext());
        if (musicServiceIntent == null)
        {
            musicServiceIntent = new Intent(this, MusicService.class);
            bindService(musicServiceIntent, musicServiceConnection, Context.BIND_AUTO_CREATE);
            startService(musicServiceIntent);
        }
        if (alertServiceIntent == null)
        {
            alertServiceIntent = new Intent(this, AlertService.class);
            bindService(alertServiceIntent, alertServiceConnection, Context.BIND_AUTO_CREATE);
            startService(alertServiceIntent);
        }

        mMusicManager = new MusicFileManager(getApplicationContext());
        playListManager = new PlayListManager(this);
        firebaseAuth = FirebaseAuth.getInstance();
        loginUser = firebaseAuth.getCurrentUser();
        userManager = new UserManager();
        localHistoryManager = new LocalHistoryManager(this);
        localLikeManager = new LocalLikeManager(this);
        if (loginUser != null)
        loginUid = loginUser.getUid();

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

    public void addNewSongInfoToRemote(MusicDto musicDto)
    {
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

    public void setNowListening(MusicDto musicDto)
    {
        UserManager userManager = new UserManager();
        userManager.setNowlistening(musicDto.getArtist(), musicDto.getAlbum(), musicDto.getTitle());
    }

    public void addHistory(int songid)
    {
        // Regdate
        String format = new String("yyyyMMddHHmmss");
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.KOREA);
        String Regdate = sdf.format(new Date());

        // Local Save
        LocalPlayHistory localPlayHistory = new LocalPlayHistory();
        localPlayHistory.uid = songid;
        localPlayHistory.Regdate = Regdate;

        localHistoryManager.addHistory(localPlayHistory);
        localHistoryManager.getHistoryDesending();

        // Remote Server save
        MusicDto musicDto = mMusicManager.getMusicDto(songid+"");
        PlayHistory playHistory = new PlayHistory();
        playHistory.artist = MusicDto.replaceForInput(musicDto.getArtist());
        playHistory.album = MusicDto.replaceForInput(musicDto.getAlbum());
        playHistory.title = MusicDto.replaceForInput(musicDto.getTitle());

        userManager.addHistory(playHistory);
    }

    public void playMusic(long songId)
    {
        MusicDto musicDto = mMusicManager.getMusicDto(songId+"");

        this.musicService.playMusic(songId+"");
        this.setNowListening(musicDto);
        this.addNewSongInfoToRemote(musicDto);
        this.setMusicNotification();
        generatePlayerChangeEvent();

    }

    /** 주로 재생 곡이 바뀔 때 컨트롤러 뷰 업데이트를 위해 사용 */
    public void generatePlayerChangeEvent()
    {
        if (onChangeListener != null)
        {
            onChangeListener.onChange();
        }
    }

    public void generateMusicFinishedEvent()
    {
        if (finishListener != null)
        {
            finishListener.onFinish();
        }
    }

    public void generatePlayStateChangeEvent(boolean state)
    {
        if (playStateChangeListener != null)
        {
            playStateChangeListener.onChange(state);
        }
    }


    public void playPrevMusic()
    {
        musicService.pause();
        musicService.mediaPlayer.reset();
        if (musicService.getPlayingMusicCurrentPosition() < 3000)
        {
            nowPlayingList.delPositionCount();
        }

        String nextmusic_uid = nowPlayingList.get(nowPlayingList.getPosition());
        if (nextmusic_uid != null)
        {
            playMusic(Integer.parseInt(nextmusic_uid));
        }
        generatePlayerChangeEvent();
    }

    public void playNextMusic()
    {
        musicService.stop();
        musicService.mediaPlayer.reset();
        nowPlayingList.addPositionCount();
        String nextmusic_uid = nowPlayingList.get(nowPlayingList.getPosition());
        if (nextmusic_uid != null)
        {
            playMusic(Integer.parseInt(nextmusic_uid));
        }
        generatePlayerChangeEvent();
    }

    public void setFinishListener(OnMusicFinishListener listener)
    {
        this.finishListener = listener;
    }

    public void setPlayStateChangeListener(PlayStateChangeListener listener)
    {
        playStateChangeListener = listener;
    }

    public void setMusicNotification()
    {
        Intent notificationIntent = new Intent(this, PlayerActivity.class);
        notificationIntent.putExtra("notificationId", 9999); //전달할 값
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        int songId = musicService.getPlayingMusic();
        MusicDto musicDto = mMusicManager.getMusicDto(songId+"");
        Bitmap albumArt = mMusicManager.getAlbumImage(getApplicationContext(), Integer.parseInt(musicDto.getAlbumId()), 100);

        builder.setContentTitle("현재 기록 중")
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

    public interface OnMusicFinishListener
    {
        void onFinish();
    }

    public interface PlayStateChangeListener
    {
        void onChange(boolean state);
    }

}
