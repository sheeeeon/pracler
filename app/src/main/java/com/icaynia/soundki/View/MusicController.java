package com.icaynia.soundki.View;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.icaynia.soundki.R;

/**
 * Created by icaynia on 20/03/2017.
 */

public class MusicController extends RelativeLayout
{
    private View mainView;

    private ImageView songAlbumImageView;
    private TextView songTitleTextView;
    private TextView songInformationTextView;

    public MusicController(Context context)
    {
        super(context);
        onCreate();
    }

    public MusicController(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        onCreate();
    }

    private void onCreate()
    {
        viewInitialize();
    }

    private void viewInitialize()
    {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mainView = inflater.inflate(R.layout.view_music_controller, null);

        songAlbumImageView = (ImageView) findViewById(R.id.view_album);
        songTitleTextView = (TextView) findViewById(R.id.view_title);
        songInformationTextView = (TextView) findViewById(R.id.view_artist);
    }

    private void setSongTitleTextView(String str)
    {

    }

    private void setSongAlbumImageView(Bitmap bitmap)
    {

    }

    private void setSongInformationTextView(String str)
    {

    }

}
