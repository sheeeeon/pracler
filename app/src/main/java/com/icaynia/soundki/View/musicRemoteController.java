package com.icaynia.soundki.View;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.icaynia.soundki.R;

/**
 * Created by icaynia on 2017. 2. 8..
 */

public class musicRemoteController extends LinearLayout
{
    public musicRemoteController(Context context)
    {
        super(context);
        this.onCreate();
    }

    public musicRemoteController(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        this.onCreate();
    }

    private void onCreate()
    {
        this.viewInitialize();
        String infService = Context.LAYOUT_INFLATER_SERVICE;
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(infService);
        View v = li.inflate(R.layout.view_music_remote_controller, this, false);
        addView(v);
    }

    private void viewInitialize()
    {

    }
}
