package com.icaynia.pracler.View;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.SeekBar;

import com.icaynia.pracler.R;

/**
 * Created by icaynia on 11/02/2017.
 */

public class MusicSeekBar extends SeekBar
{
    public MusicSeekBar(Context context) {
        super(context);
        this.setProgress(0);
        this.invalidate();
        initializeView();
    }

    public MusicSeekBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.setProgress(0);
        this.invalidate();
        initializeView();
    }

    public MusicSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initializeView();
    }

    public void initializeView()
    {
        this.setPadding(0, 0, 0, 0);
        this.setProgressDrawable(getResources().getDrawable(R.drawable.seekbar_music_timebar));
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldh, oldw);
    }
    public void updateThumb(){
        onSizeChanged(getWidth(), getHeight(), 0, 0);
    }

    public void setMaxValue(int maxValue) {
        this.setMax(maxValue);
        this.setProgress(0);
        this.updateThumb();
    }
}
