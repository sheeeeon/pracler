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

        addView(mainView);
        songAlbumImageView = (ImageView) mainView.findViewById(R.id.imageView);
        songTitleTextView = (TextView)  mainView.findViewById(R.id.titleview);
        songTitleTextView.setSelected(true);
        songInformationTextView = (TextView)  mainView.findViewById(R.id.nameView);
    }

    public void setSongTitleTextView(String str)
    {
        songTitleTextView.setText(str);
    }

    public void setSongAlbumImageView(Bitmap bitmap)
    {
        songAlbumImageView.setImageBitmap(bitmap);
    }

    public void setSongInformationTextView(String str)
    {
        songInformationTextView.setText(str);
    }

}
