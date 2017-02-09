package com.icaynia.soundki.View;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.design.widget.Snackbar;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.icaynia.soundki.R;

import org.w3c.dom.Text;

/**
 * Created by icaynia on 2017. 2. 8..
 */

public class MusicRemoteController extends LinearLayout
{
    private View v;

    private ImageView albumImageView;
    private TextView artistTextView;
    private TextView nameTextView;

    public MusicRemoteController(Context context)
    {
        super(context);
        this.onCreate();
    }

    public MusicRemoteController(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        this.onCreate();
    }

    private void onCreate()
    {
        String infService = Context.LAYOUT_INFLATER_SERVICE;
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(infService);
        v = li.inflate(R.layout.view_music_remote_controller, this, false);
        addView(v);
        this.viewInitialize();

    }

    private void viewInitialize()
    {
        albumImageView = (ImageView) v.findViewById(R.id.imageView);
        artistTextView = (TextView) v.findViewById(R.id.artistView);
        nameTextView = (TextView) v.findViewById(R.id.nameView);
    }

    public void setOnClickListener(OnClickListener listener)
    {
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.container);
        layout.setOnClickListener(listener);
    }

    public void updateSongInfo(Bitmap albumImage, String artist, String title)
    {
        albumImageView.setImageBitmap(albumImage);
        artistTextView.setText(artist);
        nameTextView.setText(title);
    }


}
