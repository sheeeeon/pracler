package com.icaynia.soundki.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.icaynia.soundki.Data.PlayListManager;
import com.icaynia.soundki.Global;
import com.icaynia.soundki.Model.PlayList;
import com.icaynia.soundki.R;
import com.icaynia.soundki.View.PlayListAdapter;
import com.icaynia.soundki.View.PlayListsAdapter;

import java.util.ArrayList;

/**
 * Created by icaynia on 21/03/2017.
 *
 */

public class PlayListFragment extends Fragment
{
    private Global global;
    private View v;
    private PlayListManager playListManager;
    private String playListName;
    private ListView listView;
    private PlayList playList;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        global = (Global) getContext().getApplicationContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        v = inflater.inflate(R.layout.fragment_playlist, container, false);
        setHasOptionsMenu(true);
        viewInitialize();
        return v;
    }

    public void viewInitialize()
    {
        listView = (ListView) v.findViewById(R.id.listview);
    }

    public void setPlayList(String playListName)
    {
        this.playListName = playListName;
    }

    public void prepare()
    {
        playList = global.playListManager.getPlayList(playListName);
        PlayListAdapter playListAdapter = new PlayListAdapter(getContext(), this.playList);
        listView.setAdapter(playListAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });
    }



}
