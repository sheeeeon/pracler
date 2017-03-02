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
import com.icaynia.soundki.Global;
import com.icaynia.soundki.Model.MusicDto;
import com.icaynia.soundki.Model.PlayList;
import com.icaynia.soundki.R;

import java.util.ArrayList;

/**
 * Created by icaynia on 02/03/2017.
 */

public class ProfileMenuAdapter extends BaseAdapter
{
    public Context context;
    public ArrayList<String> list;
    private LayoutInflater inflater;

    private Global global;

    public ProfileMenuAdapter(Context context, ArrayList<String> list)
    {
        this.context = context;
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
    public Object getItem(int position)
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
            convertView = inflater.inflate(R.layout.view_list_profilemenurows, parent, false);
            ListView.LayoutParams layoutParams = new ListView.LayoutParams(ListView.LayoutParams.MATCH_PARENT, ListView.LayoutParams.MATCH_PARENT);
            convertView.setLayoutParams(layoutParams);
        }

        TextView title = (TextView) convertView.findViewById(R.id.view_title);
        title.setText(list.get(position));

        return convertView;
    }
}
