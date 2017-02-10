package com.icaynia.soundki.Fragment;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.icaynia.soundki.Activity.MainActivity;
import com.icaynia.soundki.Data.MusicFileManager;
import com.icaynia.soundki.Global;
import com.icaynia.soundki.Model.MusicDto;
import com.icaynia.soundki.R;
import com.icaynia.soundki.View.MusicListAdapter;

import java.util.ArrayList;


/**
 * Created by icaynia on 2017. 2. 8..
 */

public class MyMusicListFragment extends Fragment
{
    private View v;
    private Global global;

    private ListView listView;
    private MusicFileManager mMusicManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_mymusic, container, false);
        setHasOptionsMenu(true);
        mMusicManager = new MusicFileManager(getContext());
        ArrayList<MusicDto> list = mMusicManager.getMusicList();
        listView = (ListView) v.findViewById(R.id.listview);
        listView.setAdapter(new MusicListAdapter(getContext(), list));
        global = (Global) getContext().getApplicationContext();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                String songId = view.getTag().toString();
                MusicDto song = mMusicManager.getMusicDto(songId);
                ((MainActivity)getContext()).onSnackbarController();

                Log.e("MyMusicListFragment", "Song : " + song.title + " Artist : " + song.artist);
                global.playMusic(Integer.parseInt(songId));
            }
        });

        return v;
    }
}
