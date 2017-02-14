package com.icaynia.soundki.View;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.icaynia.soundki.Data.MusicFileManager;
import com.icaynia.soundki.Model.MusicDto;
import com.icaynia.soundki.Model.PlayList;
import com.icaynia.soundki.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by icaynia on 14/02/2017.
 */

public class PlayListAdapter extends BaseAdapter
{
    private Context context;
    private LayoutInflater inflater;


    public ArrayList<PlayList> list;

    public PlayListAdapter(Context context, ArrayList<PlayList> list)
    {
        this.context = context;
        Log.e("test", context.getPackageName());
        this.list = list;
        inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount()
    {
        return list.size();
    }

    @Override
    public Object getItem(int position)
    {
        return position;
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
            convertView = inflater.inflate(R.layout.view_list_playlistrows, parent, false);
            ListView.LayoutParams layoutParams = new ListView.LayoutParams(ListView.LayoutParams.MATCH_PARENT, ListView.LayoutParams.MATCH_PARENT);
            convertView.setLayoutParams(layoutParams);
        }

        TextView playListTitle = (TextView) convertView.findViewById(R.id.playlist_title);
        playListTitle.setText(list.get(position).getName());

        return convertView;
    }

}
