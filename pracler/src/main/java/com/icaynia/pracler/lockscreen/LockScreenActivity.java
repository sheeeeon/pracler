package com.icaynia.pracler.lockscreen;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.icaynia.pracler.Global;
import com.icaynia.pracler.R;
import com.icaynia.pracler.models.MusicDto;
import com.icaynia.pracler.utils.ImageUtils;

/**
 * Created by icaynia on 31/03/2017.
 */

public class LockScreenActivity extends Activity
{
    private boolean ENABLE_NO_STATUSBAR = true;

    private Global global;

    private ImageView lockbtn;

    private ImageView backgroundView;
    private ImageView albumView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED|WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        setContentView(R.layout.activity_lock_screen);
        this.viewInitialize();
        this.prepare();

        update();
    }

    private void viewInitialize()
    {
        backgroundView = (ImageView) findViewById(R.id.albumBackgroundImage);
        albumView = (ImageView) findViewById(R.id.albumView);
    }

    private void prepare()
    {
        global = (Global) getApplication();
        lockbtn = (ImageView) findViewById(R.id.lockButton);
        lockbtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                finish();
            }
        });

        if (ENABLE_NO_STATUSBAR && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
    }

    private void update()
    {
        int songId = global.musicService.getPlayingMusic();
        MusicDto musicDto = global.mMusicManager.getMusicDto(songId+"");

        UpdateTask updateTask = new UpdateTask();
        updateTask.setAlbumImageView(albumView);
        updateTask.setBackgroundImageView(backgroundView);
        updateTask.execute(musicDto.getAlbumId());
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
                bitmap = global.mMusicManager.getAlbumImage(getBaseContext(), Integer.parseInt(i), getScreenSize().y);
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
                tmpBitmap = ImageUtils.blur(getBaseContext(), tmpBitmap, 15);

                BackgroundImageView.setImageBitmap(ImageUtils.cropBitmap(getScreenSize(), tmpBitmap));
                tmpBitmap.recycle();
                tmpBitmap = null;
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
