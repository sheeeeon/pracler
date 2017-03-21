package com.icaynia.soundki.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.icaynia.soundki.Global;
import com.icaynia.soundki.Model.MusicList;
import com.icaynia.soundki.R;
import com.icaynia.soundki.View.MusicListAdapter;

/**
 * Created by icaynia on 16/03/2017.
 */

public class MyArtistFragment extends Fragment
{
    private Global global;
    private View v;

    // TODO VIEW
    private ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        v = inflater.inflate(R.layout.fragment_myartist, container, false);
        setHasOptionsMenu(true);
        viewInitialize();
        prepare();
        return v;
    }

    public void viewInitialize()
    {
        listView = (ListView) v.findViewById(R.id.listview);

    }

    public void prepare()
    {
        global = (Global) getContext().getApplicationContext();

        MusicList musicList = global.mMusicManager.getMusicList();

        MusicListAdapter adapter = new MusicListAdapter(getContext(), musicList);

        listView.setAdapter(adapter);
    }


}
