package com.icaynia.soundki.Fragment;

import android.app.ActionBar;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.icaynia.soundki.Activity.MainActivity;
import com.icaynia.soundki.Data.FileManager;
import com.icaynia.soundki.Data.MusicFileManager;
import com.icaynia.soundki.Data.PlayListManager;
import com.icaynia.soundki.Global;
import com.icaynia.soundki.Model.MusicDto;
import com.icaynia.soundki.Model.MusicList;
import com.icaynia.soundki.Model.PlayList;
import com.icaynia.soundki.R;
import com.icaynia.soundki.View.MusicListAdapter;
import com.icaynia.soundki.View.PlayListAdapter;

import java.util.ArrayList;



/**
 * Created by icaynia on 2017. 2. 8..
 */

public class MyMusicListFragment extends Fragment
{
    private String TAG = "MyMusicListFragment";
    private View v;
    private Global global;

    private ListView listView;
    private MusicFileManager mMusicManager;
    private MusicList list;

    private MusicListAdapter musicListAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_mymusic, container, false);
        setHasOptionsMenu(true);
        mMusicManager = new MusicFileManager(getContext());
        list = mMusicManager.getMusicList();
        listView = (ListView) v.findViewById(R.id.listview);
        musicListAdapter = new MusicListAdapter(getContext(), list);
        listView.setAdapter(musicListAdapter);
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
                PlayList nowPlayingList = new PlayList();

                for (int pos = 0; pos < listView.getAdapter().getCount(); pos++)
                {
                    MusicDto dto = musicListAdapter.getItem(pos);
                    nowPlayingList.addItem(dto);
                }

                nowPlayingList.setPosition(position);
                global.nowPlayingList = nowPlayingList;
            }
        });
        //registerForContextMenu(listView);

        Spinner spinner = (Spinner) v.findViewById(R.id.spin1);
        String[] platforms = getResources().
                getStringArray(R.array.mymusicfragment_sort);
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(getContext(),
                        android.R.layout.simple_spinner_dropdown_item,
                        platforms);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                if (position == 0)
                    list = mMusicManager.sort(list, global.SORT_NAME);
                else if (position == 1)
                    list = mMusicManager.sort(list, global.SORT_ALBUM);
                else if (position == 2)
                    list = mMusicManager.sort(list, global.SORT_ARTIST);
                else if (position == 3)
                    list = mMusicManager.sort(list, global.SORT_LENGTH);
                else if (position == 4)
                    list = mMusicManager.sort(list, global.SORT_LENGTH);

                listView.setAdapter(new MusicListAdapter(getContext(), list));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });


        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        listView.setMultiChoiceModeListener(modeChangeListener);

        // for develop

        PlayListManager plm = new PlayListManager(getContext());
        PlayList playList = new PlayList();

        playList.setName("testing!");
        playList.addItem("uid test test test trest");

        plm.savePlayList(playList);


        PlayList loaded = plm.getPlayList("testing!");
        Log.e(TAG, loaded.get(0));

        return v;
    }



    private AbsListView.MultiChoiceModeListener modeChangeListener = new AbsListView.MultiChoiceModeListener() {
        @Override
        public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked)
        {

        }

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu)
        {
            Log.e("log", "choicemode");
            listView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
            MusicListAdapter adapter = (MusicListAdapter) listView.getAdapter();
            adapter.setChoiceMode(true);
            adapter.notifyDataSetChanged();
            setActionBarState(true);

            return false;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu)
        {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item)
        {
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode)
        {

        }
    };

    public void setActionBarState(boolean state)
    {
        if (state)
        {
            ((MainActivity)getActivity()).getSupportActionBar().setTitle("선택");
        }
        else
        {
            ((MainActivity)getActivity()).getSupportActionBar().setTitle("취소");
        }
    }


}
