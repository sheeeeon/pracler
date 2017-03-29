package com.icaynia.pracler.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.icaynia.pracler.Global;
import com.icaynia.pracler.adapters.SongListAdapter;
import com.icaynia.pracler.dataloader.SongLoader;
import com.icaynia.pracler.models.MusicList;
import com.icaynia.pracler.models.PlayList;
import com.icaynia.pracler.R;
import com.icaynia.pracler.View.CardHeader;
import com.icaynia.pracler.adapters.MusicListAdapter;
import com.icaynia.pracler.models.SongList;

/**
 * Created by icaynia on 16/03/2017.
 */

public class MyAlbumFragment extends Fragment
{
    private Global global;
    private View v;

    // TODO VIEW
    private ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        v = inflater.inflate(R.layout.fragment_myalbum, container, false);
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

        final SongList songList = SongLoader.getSongs(getContext());

        SongListAdapter adapter = new SongListAdapter(getContext(), songList);
        listView.setAdapter(adapter);

        CardHeader cardHeader = new CardHeader(getContext());
        cardHeader.setTitleIcon(getResources().getDrawable(R.drawable.ic_album));
        cardHeader.setTitleText("앨범 정렬");
        listView.addHeaderView(cardHeader);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                i -= 1;
                global.playMusic(songList.get(i).uid);

                PlayList newNowPlayingList = new PlayList();
                for (int t = 0; t < songList.size(); t++)
                {
                    newNowPlayingList.addItem(songList.get(t).uid+"");
                }

                newNowPlayingList.setPosition(i);
                global.nowPlayingList = newNowPlayingList;
            }
        });
    }


}
