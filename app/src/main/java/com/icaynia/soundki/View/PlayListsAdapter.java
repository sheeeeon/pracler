package com.icaynia.soundki.View;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.icaynia.soundki.Model.PlayList;
import com.icaynia.soundki.R;

import java.util.ArrayList;

/**
 * Created by icaynia on 14/03/2017.?
 *
 * 플레이리스트의 목록 어댑터
 */

public class PlayListsAdapter extends BaseAdapter
{
    private ArrayList<String> playList;
    private LayoutInflater inflater;

    public PlayListsAdapter(Context context, ArrayList<String> playList)
    {
        this.playList = playList;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount()
    {
        return playList.size();
    }

    @Override
    public Object getItem(int position)
    {
        return playList.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        if (convertView == null)
        {
            convertView = inflater.inflate(R.layout.view_list_playlistsrows, parent, false);
            ListView.LayoutParams layoutParams = new ListView.LayoutParams(ListView.LayoutParams.MATCH_PARENT, ListView.LayoutParams.MATCH_PARENT);
            convertView.setLayoutParams(layoutParams);
        }

        TextView textView = (TextView) convertView.findViewById(R.id.view_title);
        textView.setText(playList.get(position));
        return convertView;
    }

}
