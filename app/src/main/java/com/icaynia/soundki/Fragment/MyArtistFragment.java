package com.icaynia.soundki.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.icaynia.soundki.Global;
import com.icaynia.soundki.Model.MusicList;
import com.icaynia.soundki.Model.PlayList;
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

        final MusicList musicList = global.mMusicManager.getMusicList();

        MusicListAdapter adapter = new MusicListAdapter(getContext(), musicList);

        listView.setAdapter(adapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                global.playMusic(Integer.parseInt(musicList.getItem(i).getUid_local()));

                PlayList newNowPlayingList = new PlayList();
                for (int t = 0; t < musicList.size(); t++)
                {
                    newNowPlayingList.addItem(musicList.getItem(t).getUid_local());
                }

                newNowPlayingList.setPosition(i);
                global.nowPlayingList = newNowPlayingList;
            }
        });
    }


}
