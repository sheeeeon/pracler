package com.icaynia.soundki.Activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.icaynia.soundki.Global;
import com.icaynia.soundki.Model.MusicDto;
import com.icaynia.soundki.R;

/**
 * Created by icaynia on 2017. 2. 10..
 */

public class PlayerActivity extends AppCompatActivity
{
    private boolean ENABLE_NO_STATUSBAR = true;

    private Global global;
    private TextView artistView;
    private TextView album;
    private TextView titleView;
    private TextView alltimeView;
    private TextView nowTime;
    private ImageView albumImageBackgroundView;
    private ImageView albumImageView;
    private LinearLayout albumViewContainer;


    private LinearLayout BUTTON_FAVORITE;
        private ImageView IMAGE_FAVORITE;
    private LinearLayout BUTTON_PREVIOUS;
        private ImageView IMAGE_PREVIOUS;
    private LinearLayout BUTTON_PLAY;
        private ImageView IMAGE_PLAY;
    private LinearLayout BUTTON_NEXT;
        private ImageView IMAGE_NEXT;
    private LinearLayout BUTTON_MENU;
        private ImageView IMAGE_MENU;

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
        albumImageBackgroundView = (ImageView) findViewById(R.id.albumView_blur);
        albumViewContainer = (LinearLayout) findViewById(R.id.albumViewContainer);
        BUTTON_FAVORITE = (LinearLayout) findViewById(R.id.button_favorite);
        BUTTON_PREVIOUS = (LinearLayout) findViewById(R.id.button_previous);
        BUTTON_PLAY = (LinearLayout) findViewById(R.id.button_play);
        IMAGE_PLAY = (ImageView) findViewById(R.id.button_play_icon);
        BUTTON_PLAY.setOnClickListener(onClickPlayButton);
        BUTTON_NEXT = (LinearLayout) findViewById(R.id.button_next);
        BUTTON_MENU = (LinearLayout) findViewById(R.id.button_more);
        BUTTON_MENU.setOnClickListener(onClickMenuButton);
        Point point = getScreenSize();
        albumImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                global.musicService.pause();
            }
        });

        if (ENABLE_NO_STATUSBAR && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow(); // in Activity's onCreate() for instance
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
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
            Bitmap albumImage = global.mMusicManager.getAlbumImage(this, Integer.parseInt(playingSong.albumid), getScreenSize().y);

            /** when song haven't albumart */
            if (albumImage != null)
            {
                albumImageView.setImageBitmap(albumImage);
                albumImage = blur(this, albumImage, 15);
                Log.e("screensize", getScreenSize().x+" "+getScreenSize().y+" bitmap : "+albumImage.getWidth());
                albumImageBackgroundView.setImageBitmap(cropBitmap(albumImage));
            }
            IMAGE_PLAY.setImageDrawable(getResources().getDrawable(R.drawable.ic_pause_white));
        }
        else
        {
            IMAGE_PLAY.setImageDrawable(getResources().getDrawable(R.drawable.ic_play_white));
        }
    }

    /** BUTTON CLICK LISTENER REGION */
    public View.OnClickListener onClickPlayButton = new View.OnClickListener() {
        @Override
        public void onClick(View v)
        {
            boolean playState = global.musicService.playing;
            Drawable icon;
            if (playState)
            {
                global.musicService.pause();
                icon = getResources().getDrawable(R.drawable.ic_play_white);
            }
            else
            {
                global.musicService.start();
                icon = getResources().getDrawable(R.drawable.ic_pause_white);
            }
            IMAGE_PLAY.setImageDrawable(icon);
        }
    };

    public View.OnClickListener onClickMenuButton = new View.OnClickListener() {
        @Override
        public void onClick(View v)
        {

        }
    };


    /** IMAGE PROCESSING FUNCTION */
    public Point getScreenSize()
    {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        DisplayMetrics dm = getApplicationContext().getResources().getDisplayMetrics();
        int width = dm.widthPixels;
        int height = dm.heightPixels;

        Log.e("renewsize ", width + ", " + height);
        size.y = 1280;
        return size;
    }

    public Bitmap cropBitmap(Bitmap original) {
        int startX = original.getWidth() / 2 - getScreenSize().x / 2;
        Log.e("TAG", original.getHeight()+"");

        Bitmap result = Bitmap.createBitmap(original
                , startX //X 시작위치 (원본의 4/1지점)
                , 1 //Y 시작위치 (원본의 4/1지점)
                , getScreenSize().x // 넓이 (원본의 절반 크기)
                , getScreenSize().y - 1); // 높이 (원본의 절반 크기)
        if (result != original) {
            original.recycle();
        }
        return result;
    }

    public static Bitmap blur(Context context, Bitmap sentBitmap, int radius) {

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
            Bitmap bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);

            final RenderScript rs = RenderScript.create(context);
            final Allocation input = Allocation.createFromBitmap(rs, sentBitmap, Allocation.MipmapControl.MIPMAP_NONE,
                    Allocation.USAGE_SCRIPT);
            final Allocation output = Allocation.createTyped(rs, input.getType());
            final ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
            script.setRadius(radius); //0.0f ~ 25.0f
            script.setInput(input);
            script.forEach(output);
            output.copyTo(bitmap);
            return bitmap;
        } else {
            return sentBitmap;
        }

    }

}