package com.icaynia.soundki.Fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.icaynia.soundki.Activity.MainActivity;
import com.icaynia.soundki.Data.MusicFileManager;
import com.icaynia.soundki.Global;
import com.icaynia.soundki.Model.MusicDto;
import com.icaynia.soundki.Model.MusicList;
import com.icaynia.soundki.Model.PlayList;
import com.icaynia.soundki.R;
import com.icaynia.soundki.View.MusicListAdapter;

import java.util.ArrayList;

import static com.facebook.FacebookSdk.getApplicationContext;


/**
 * Created by icaynia on 2017. 2. 8..
 */

public class MyMusicListFragment extends Fragment
{
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
        registerForContextMenu(listView);

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

        return v;
    }

    //Context Menu-------------------------------------

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        // TODO Auto-generated method stub

        menu.setHeaderTitle("메뉴");
        menu.add(0, v.getId(), 0, "다음 재생");
        menu.add(0, v.getId(), 0, "재생목록에 추가");

        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        if (item.getTitle() == "재생목록에 추가") {
            onPlayListChooser(global.playListManager.getPlayListList());

        }
        else if (item.getTitle() == "다음 재생")
        {


        }
        return false;
    }

    public void onPlayListChooser(ArrayList<String> listItems) {
        final CharSequence[] items = listItems.toArray(new CharSequence[listItems.size()]);
        listItems.toArray(new CharSequence[listItems.size()]);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {
                // TODO Auto-generated method stub
                if (item == 0) { // 연결할 장치를 선택하지 않고 '취소' 를 누른 경우.
                    Toast.makeText(getApplicationContext(), "연결할 장치를 선택하지 않았습니다.", Toast.LENGTH_LONG).show();
                } else { // 연결할 장치를 선택한 경우, 선택한 장치와 연결을 시도함.
                    items[item].toString();
                }
            }

        });

        //buider.setCancelable(false);  // 뒤로 가기 버튼 사용 금지.
        AlertDialog alert = builder.create();
        alert.show();
    }



}
