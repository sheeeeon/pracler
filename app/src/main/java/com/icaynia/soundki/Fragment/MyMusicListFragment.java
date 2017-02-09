package com.icaynia.soundki.Fragment;

import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.icaynia.soundki.Data.MusicFileManager;
import com.icaynia.soundki.Model.MusicDto;
import com.icaynia.soundki.R;
import com.icaynia.soundki.View.MusicListAdapter;


/**
 * Created by icaynia on 2017. 2. 8..
 */

public class MyMusicListFragment extends Fragment
{
    private View v;

    private ListView listView;
    private MusicFileManager mMusicManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_mymusic, container, false);
        setHasOptionsMenu(true);
        mMusicManager = new MusicFileManager(getContext());
        listView = (ListView) v.findViewById(R.id.listview);
        listView.setAdapter(new MusicListAdapter(getContext(), mMusicManager.getMusicList()));
        return v;
    }


}
