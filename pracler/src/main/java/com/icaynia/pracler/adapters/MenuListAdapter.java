package com.icaynia.pracler.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.icaynia.pracler.R;

import java.util.ArrayList;

/**
 * Created by icaynia on 08/03/2017.
 */

public class MenuListAdapter extends BaseAdapter
{
    private Context context;
    private ArrayList<String> list = new ArrayList<>();
    private LayoutInflater inflater;

    public MenuListAdapter(Context context, ArrayList<String> list)
    {
        this.context = context;
        this.list = list;
        inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount()
    {
        return list.size();
    }

    @Override
    public String getItem(int position)
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
            convertView = inflater.inflate(R.layout.view_list_dialogmenu, parent, false);
            ListView.LayoutParams layoutParams = new ListView.LayoutParams(ListView.LayoutParams.MATCH_PARENT, ListView.LayoutParams.MATCH_PARENT);
            convertView.setLayoutParams(layoutParams);
        }

        TextView textView = (TextView) convertView.findViewById(R.id.textview);
        textView.setText(list.get(position));
        return convertView;
    }
}
