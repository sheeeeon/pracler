package com.icaynia.soundki.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.icaynia.soundki.Data.MusicFileManager;
import com.icaynia.soundki.Global;
import com.icaynia.soundki.Model.LocalPlayHistory;
import com.icaynia.soundki.Model.MusicDto;
import com.icaynia.soundki.Model.MusicList;
import com.icaynia.soundki.Model.PlayCount;
import com.icaynia.soundki.Model.PlayList;
import com.icaynia.soundki.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by icaynia on 2017. 2. 8..
 */

public class TopMusicAdapter extends BaseAdapter
{
    private Global global;
    private Context context;
    private LayoutInflater inflater;
    public  ArrayList<PlayCount> list;
    public MusicList musicList;
    private boolean CHOICEMODE = false;
    private ArrayList<Boolean> checkState = new ArrayList<>();

    public TopMusicAdapter(Context context, ArrayList<PlayCount> list)
    {
        this.context = context;
        Log.e("test", context.getPackageName());
        this.list = list;
        inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        global = (Global) context.getApplicationContext();
    }

    @Override
    public int getCount()
    {
        return list.size();
    }

    @Override
    public PlayCount getItem(int position)
    {
        return list.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        if (convertView == null)
        {
            convertView = inflater.inflate(R.layout.view_list_musicrows, parent, false);
            ListView.LayoutParams layoutParams = new ListView.LayoutParams(ListView.LayoutParams.MATCH_PARENT, ListView.LayoutParams.MATCH_PARENT);
            convertView.setLayoutParams(layoutParams);
        }

        RelativeLayout lv =  (RelativeLayout) convertView.findViewById(R.id.lv);
        ImageView album = (ImageView) convertView.findViewById(R.id.view_album);
        album.setImageDrawable(context.getResources().getDrawable(android.R.drawable.ic_menu_report_image));
        TextView title = (TextView) convertView.findViewById(R.id.view_title);
        TextView artist = (TextView) convertView.findViewById(R.id.view_artist);

        MusicDto musicDto = global.mMusicManager.getMusicDto(list.get(position).uid+"");

        title.setText(musicDto.getTitle());
        artist.setText(
                list.get(position).count + "회 재생 - " +
                musicDto.getArtist() + " - " +
                musicDto.getAlbum() + " - " +
                global.mMusicManager.convertToTime(musicDto.getLength()));

        convertView.setTag(musicDto.getUid_local());
        MyAsyncTask myAsyncTask = new MyAsyncTask();
        myAsyncTask.setImgView(album);
        myAsyncTask.execute(musicDto.getAlbumId());

        return convertView;
    }

    public boolean isChoiceMode()
    {
        return this.CHOICEMODE;
    }

    public void setChoiceMode(boolean state)
    {
        checkState = new ArrayList<>();
        for (int i = 0; i < list.size(); i++)
        {
            checkState.add(i, false);
        }
        this.CHOICEMODE = state;
    }

    public ArrayList<Boolean> getState()
    {
        return checkState;
    }

    public boolean getCheckState(int position)
    {
        return checkState.get(position);
    }

    public void setCheckState(int position, boolean state)
    {
        checkState.set(position, state);
    }

    public class MyAsyncTask extends AsyncTask<String, Void, Bitmap>
    {
        private static final String TAG = "AlbumImageTask";
        private int albumid;
        private ImageView imgView;
        private MusicFileManager mMusicFileManager = new MusicFileManager(context);

        public void setImgView(ImageView _imgView)
        {
            this.imgView = _imgView;
        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            Log.i(TAG, "PreExecute");
            mMusicFileManager = new MusicFileManager(context);

        }

        @Override
        protected Bitmap doInBackground(String... id)
        {
            Bitmap bitmap = null;
            for (String i : id)
            {
                this.albumid = Integer.parseInt(i);
                bitmap = mMusicFileManager.getAlbumImage(context, albumid, 100);
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap result)
        {
            super.onPostExecute(result);

            imgView.setImageBitmap(result);
        }

    }
}
