package com.icaynia.soundki.Activity;

import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.icaynia.soundki.Global;
import com.icaynia.soundki.Model.MusicDto;
import com.icaynia.soundki.R;

/**
 * Created by icaynia on 2017. 2. 10..
 */

public class PlayerActivity extends AppCompatActivity
{
    private Global global;
    private TextView artistView;
    private TextView album;
    private TextView titleView;
    private TextView alltimeView;
    private TextView nowTime;
    private ImageView albumImageView;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        initializeView();
        global = (Global) getApplication();
        update();
    }

    public void initializeView()
    {
        artistView = (TextView) findViewById(R.id.artist);
        album = (TextView) findViewById(R.id.album);
        titleView = (TextView) findViewById(R.id.title);
        alltimeView = (TextView) findViewById(R.id.alltime);
        nowTime = (TextView) findViewById(R.id.nowtime);
        albumImageView = (ImageView) findViewById(R.id.albumView);

        albumImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                global.musicService.pause();
            }
        });
    }

    public void update()
    {
        int songId = global.musicService.getPlayingMusic();
        if (songId != 0)
        {
            MusicDto playingSong = global.mMusicManager.getMusicDto(songId+"");
            artistView.setText(playingSong.artist);
            album.setText(playingSong.album);
            titleView.setText(playingSong.title);
            Bitmap albumImage = global.mMusicManager.getAlbumImage(this, Integer.parseInt(playingSong.albumid), 200);
            albumImageView.setImageBitmap(albumImage);
        }
    }
}
