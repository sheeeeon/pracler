package com.icaynia.pracler.activities;

import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Display;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.icaynia.pracler.Data.UserManager;
import com.icaynia.pracler.Global;
import com.icaynia.pracler.models.MusicDto;
import com.icaynia.pracler.R;
import com.icaynia.pracler.View.MusicSeekBar;
import com.icaynia.pracler.utils.ImageUtils;
import com.wang.avi.AVLoadingIndicatorView;

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

    private TextView currentTimeView;
    private TextView durationTimeView;

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

    private AVLoadingIndicatorView loadingBar;

    private MusicSeekBar musicTimeBar;

    private Boolean likestate = false;

    private Context context;
    private UpdateTask updateTask;

    Thread myThread;
    private boolean threadController = true;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        context = this;
        initializeView();
        global = (Global) getApplication();



        update();
        global.setOnChangeListener(new Global.OnChangeListener() {
            @Override
            public void onChange()
            {
                update();
            }
        });
    }

    @Override
    public void onDestroy()
    {
        albumImageView = null;
        albumImageBackgroundView = null;
        musicTimeBar.setOnClickListener(null);
        global = null;
        Log.e("finish", "fin");
        threadController = false;
        myThread = null;
        System.gc();
        super.onDestroy();
    }


    public void initializeView()
    {
        loadingBar = (AVLoadingIndicatorView) findViewById(R.id.loadingBar);

        loadingBar.show();
        currentTimeView = (TextView) findViewById(R.id.currentTime);
        durationTimeView = (TextView) findViewById(R.id.durationTime);
        artistView = (TextView) findViewById(R.id.artist);
        album = (TextView) findViewById(R.id.album);
        titleView = (TextView) findViewById(R.id.title);
        alltimeView = (TextView) findViewById(R.id.alltime);
        nowTime = (TextView) findViewById(R.id.nowtime);
        albumImageView = (ImageView) findViewById(R.id.albumView);
        albumImageBackgroundView = (ImageView) findViewById(R.id.albumView_blur);
        albumViewContainer = (LinearLayout) findViewById(R.id.albumViewContainer);
        BUTTON_FAVORITE = (LinearLayout) findViewById(R.id.button_favorite);
        IMAGE_FAVORITE = (ImageView) findViewById(R.id.button_favorite_icon);
        BUTTON_FAVORITE.setOnClickListener(onClickLoveButton);
        BUTTON_PREVIOUS = (LinearLayout) findViewById(R.id.button_previous);
        BUTTON_PREVIOUS.setOnClickListener(onClickPrevButton);
        BUTTON_PLAY = (LinearLayout) findViewById(R.id.button_play);
        IMAGE_PLAY = (ImageView) findViewById(R.id.button_play_icon);
        BUTTON_PLAY.setOnClickListener(onClickPlayButton);
        BUTTON_NEXT = (LinearLayout) findViewById(R.id.button_next);
        BUTTON_NEXT.setOnClickListener(onClickNextButton);
        BUTTON_MENU = (LinearLayout) findViewById(R.id.button_more);
        registerForContextMenu(BUTTON_MENU);
        BUTTON_MENU.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                openContextMenu(v);
            }
        });
        musicTimeBar = (MusicSeekBar) findViewById(R.id.musicTimeBar);
        musicTimeBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                if (event.getAction() == MotionEvent.ACTION_UP)
                {
                    global.musicService.mediaPlayer.seekTo(musicTimeBar.getProgress());
                }
                return false;
            }
        });

        musicTimeBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
            {
                currentTimeView.setText(global.mMusicManager.convertToTime(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar)
            {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar)
            {

            }
        });
        if (ENABLE_NO_STATUSBAR && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow(); // in Activity's onCreate() for instance
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
    }

    public View.OnClickListener onClickMenuButton = new View.OnClickListener() {
        @Override
        public void onClick(View v)
        {
            openMenu();
        }
    };

    public void update()
    {
        int songid = global.musicService.getPlayingMusic();
        update(songid);
    }


    public void update(int songId)
    {
        musicTimeBar.setProgress(0);
        MusicDto playingSong = global.mMusicManager.getMusicDto(songId+"");
        /**음악 활성화와 관련 없음
        global.userManager.isLove(global.loginUser.getUid(), playingSong.getArtist(), playingSong.getAlbum(), playingSong.getTitle(), new UserManager.OnCompleteGetLikeState() {
            @Override
            public void onComplete(boolean likeState)
            {
                likestate = likeState;
                if (likeState)
                    IMAGE_FAVORITE.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorite));
                else
                    IMAGE_FAVORITE.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorite_border_white));

            }
        });*/

        likestate = global.localLikeManager.isLike(songId);
        if (likestate)
            IMAGE_FAVORITE.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorite));
        else
            IMAGE_FAVORITE.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorite_border_white));


        /** 음악이 활성화되어있을 때 */
        if (songId != 0)
        {
            artistView.setText(playingSong.getArtist());
            album.setText(playingSong.getAlbum());
            titleView.setText(playingSong.getTitle());

            updateTask = new UpdateTask();
            updateTask.setAlbumImageView(albumImageView);
            updateTask.setBackgroundImageView(albumImageBackgroundView);
            updateTask.execute(playingSong.getAlbumId());
            /** when song have albumart
             * tmpBitmap = global.mMusicManager.getAlbumImage(this, Integer.parseInt(playingSong.getAlbumId()), getScreenSize().y);
            if (tmpBitmap != null)
            {
                albumImageView.setImageBitmap(tmpBitmap);
                tmpBitmap = blur(this, tmpBitmap, 15);
                Log.e("screensize", getScreenSize().x+" "+getScreenSize().y+" bitmap : "+tmpBitmap.getWidth());
                albumImageBackgroundView.setImageBitmap(cropBitmap(getScreenSize(), tmpBitmap));

                tmpBitmap.recycle();
                tmpBitmap = null;
            }*/
            /** 음악이 일시정지되어 있을 때 */
            if (!global.musicService.isPlaying())
            {
                IMAGE_PLAY.setImageDrawable(getResources().getDrawable(R.drawable.ic_play_white));
            }
            /** 음악이 플레이 중일 때 */
            else
            {
                IMAGE_PLAY.setImageDrawable(getResources().getDrawable(R.drawable.ic_pause_white));
            }
        }

        /** 음악이 없을 때 */
        else if (songId == 0)
        {
            IMAGE_PLAY.setImageDrawable(getResources().getDrawable(R.drawable.ic_play_white));
        }

        /** 타임바 설정 */
        updateTimeBar();


        durationTimeView.setText(global.mMusicManager.convertToTime(global.musicService.getPlayingMusicDuration()));

        startTimebarThread();
    }

    public void startTimebarThread()
    {
        threadController = false;
        threadController = true;
        myThread = new Thread(new Runnable() {
            public void run() {
                while (threadController) {
                    try {
                        musicTimeBar.setProgress(global.musicService.getPlayingMusicCurrentPosition());
                        Thread.sleep(300);
                        currentTimeView.setText(global.mMusicManager.convertToTime(global.musicService.getPlayingMusicCurrentPosition()));
                    } catch (Throwable t) {

                    }
                }
            }
        });
        myThread.start();
    }

    public void updateTimeBar()
    {
        musicTimeBar.setMaxValue(global.musicService.getPlayingMusicDuration());
        musicTimeBar.setProgress(global.musicService.getPlayingMusicCurrentPosition());
    }

    /** BUTTON CLICK LISTENER REGION */
    public View.OnClickListener onClickPlayButton = new View.OnClickListener() {
        @Override
        public void onClick(View v)
        {
            boolean playState = global.musicService.isPlaying();
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

    public View.OnClickListener onClickPrevButton = new View.OnClickListener() {
        @Override
        public void onClick(View v)
        {
            global.musicService.mediaPlayer.reset();
            musicTimeBar.setProgress(0);
            global.playPrevMusic();
        }
    };

    public View.OnClickListener onClickNextButton = new View.OnClickListener() {
        @Override
        public void onClick(View v)
        {
            global.musicService.mediaPlayer.reset();
            musicTimeBar.setProgress(0);
            global.playNextMusic();
        }
    };

    public View.OnClickListener onClickLoveButton = new View.OnClickListener() {
        @Override
        public void onClick(View v)
        {
            int songId = global.musicService.getPlayingMusic();
            MusicDto musicDto = global.mMusicManager.getMusicDto(songId+"");
            if (likestate == true) likestate = false;
            else likestate = true;
            UserManager userManager = new UserManager();
            userManager.setLike(musicDto.getArtist(), musicDto.getAlbum(), musicDto.getTitle(), likestate, new UserManager.OnCompleteGetLikeState() {
                @Override
                public void onComplete(boolean likeState) {
                    if (likeState)
                        IMAGE_FAVORITE.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorite));
                    else
                        IMAGE_FAVORITE.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorite_border_white));

                }
            });

            global.localLikeManager.setLike(songId, likestate);
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_player, menu);
        return true;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {

        if(v.getId() == R.id.button_more) {

            getMenuInflater().inflate(R.menu.menu_player, menu);

        } else {

        }

        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_onnowplaying:
                onNowPlayingActivity();
                break;
        }

        return super.onContextItemSelected(item);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.action_onnowplaying:
                onNowPlayingActivity();
                break;
        }
        return true;
    }

    //android Context Menu---
    /** IMAGE PROCESSING FUNCTION */


    public void openMenu()
    {
        new Thread(new Runnable()
        {
            public void run()
            {
                KeyEvent event = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_MENU);
                new Instrumentation().sendKeySync(event);
                KeyEvent event2 = new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_MENU);
                new Instrumentation().sendKeySync(event2);
            }
        }).start();
    }

    public void onNowPlayingActivity()
    {
        Intent intent = new Intent(this, PlayListActivity.class);
        intent.putExtra("list", "0"); // '0' means now playlist.
        startActivity(intent);
    }

    public class UpdateTask extends AsyncTask<String, Void, Bitmap>
    {
        private ImageView BackgroundImageView;
        private ImageView AlbumImageView;

        public void setBackgroundImageView(ImageView img)
        {
            BackgroundImageView = img;
        }

        public void setAlbumImageView(ImageView img)
        {
            AlbumImageView = img;
        }
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
        }

        @Override
        protected Bitmap doInBackground(String... id)
        {
            Bitmap bitmap = null;
            for (String i : id)
            {
                bitmap = global.mMusicManager.getAlbumImage(context, Integer.parseInt(i), getScreenSize().y);
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap result)
        {
            super.onPostExecute(result);
            if (result != null)
            {
                Bitmap tmpBitmap = result;
                AlbumImageView.setImageBitmap(tmpBitmap);
                tmpBitmap = ImageUtils.blur(context, tmpBitmap, 15);

                BackgroundImageView.setImageBitmap(ImageUtils.cropBitmap(getScreenSize(), tmpBitmap));
                tmpBitmap.recycle();
                tmpBitmap = null;

                loadingBar.hide();
            }
            else
            {
                AlbumImageView.setImageBitmap(null);
                BackgroundImageView.setImageBitmap(null);
            }
        }

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
    }

}