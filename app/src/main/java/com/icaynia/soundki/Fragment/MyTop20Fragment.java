package com.icaynia.soundki.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.icaynia.soundki.Global;
import com.icaynia.soundki.Model.LocalPlayHistory;
import com.icaynia.soundki.Model.PlayCount;
import com.icaynia.soundki.Model.PlayList;
import com.icaynia.soundki.R;
import com.icaynia.soundki.View.TopMusicAdapter;

import java.util.ArrayList;

/**
 * Created by icaynia on 16/03/2017.
 */

public class MyTop20Fragment extends Fragment
{
    private Global global;

    private View v;
    private ListView mainListView;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        global = (Global) getContext().getApplicationContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        v = inflater.inflate(R.layout.fragment_top20, container, false);
        setHasOptionsMenu(true);
        this.viewInitialize();
        this.prepare();
        return v;
    }

    public void viewInitialize()
    {
        mainListView = (ListView) v.findViewById(R.id.listview);
    }

    public void prepare()
    {
        final ArrayList<PlayCount> top20list  = global.localHistoryManager.getHistoryDesending();
        TopMusicAdapter adapter = new TopMusicAdapter(getContext(), top20list);
        mainListView.setAdapter(adapter);
        mainListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                global.playMusic(Integer.parseInt(top20list.get(i).uid+""));

                PlayList newNowPlayingList = new PlayList();
                for (int t = 0; t < top20list.size(); t++)
                {
                    newNowPlayingList.addItem(top20list.get(t).uid+"");
                }

                newNowPlayingList.setPosition(i);
                global.nowPlayingList = newNowPlayingList;
            }
        });
    }



}
