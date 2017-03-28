package com.icaynia.pracleme.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.icaynia.pracleme.Global;
import com.icaynia.pracleme.Model.MusicList;
import com.icaynia.pracleme.Model.PlayList;
import com.icaynia.pracleme.R;
import com.icaynia.pracleme.View.CardHeader;
import com.icaynia.pracleme.adapters.MusicListAdapter;

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

        final MusicList musicList = global.mMusicManager.sort(global.mMusicManager.getMusicList(), 1); // 1 means sort by album name.

        MusicListAdapter adapter = new MusicListAdapter(getContext(), musicList);
        listView.setAdapter(adapter);

        CardHeader cardHeader = new CardHeader(getContext());
        cardHeader.setTitleIcon(getResources().getDrawable(R.drawable.ic_album));
        cardHeader.setTitleText("앨범 정렬");
        listView.addHeaderView(cardHeader);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                i -= 1;
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
