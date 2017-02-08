package com.icaynia.soundki.Fragment;

import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.icaynia.soundki.R;


/**
 * Created by icaynia on 2017. 2. 8..
 */

public class MyMusicListFragment extends Fragment
{
    private View v;

    private ListView listView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_mymusiclist, container, false);
        setHasOptionsMenu(true);

        listView = (ListView) v.findViewById(R.id.listview);
        listView.setAdapter(new ListAdapter() {
            @Override
            public boolean areAllItemsEnabled()
            {
                return false;
            }

            @Override
            public boolean isEnabled(int position)
            {
                return false;
            }

            @Override
            public void registerDataSetObserver(DataSetObserver observer)
            {

            }

            @Override
            public void unregisterDataSetObserver(DataSetObserver observer)
            {

            }

            @Override
            public int getCount()
            {
                return 0;
            }

            @Override
            public Object getItem(int position)
            {
                return null;
            }

            @Override
            public long getItemId(int position)
            {
                return 0;
            }

            @Override
            public boolean hasStableIds()
            {
                return false;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent)
            {
                return null;
            }

            @Override
            public int getItemViewType(int position)
            {
                return 0;
            }

            @Override
            public int getViewTypeCount()
            {
                return 0;
            }

            @Override
            public boolean isEmpty()
            {
                return false;
            }
        });

        return v;
    }


}
