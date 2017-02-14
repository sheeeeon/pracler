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
import android.widget.Toast;

import com.icaynia.soundki.Activity.MainActivity;
import com.icaynia.soundki.Data.MusicFileManager;
import com.icaynia.soundki.Global;
import com.icaynia.soundki.Model.MusicDto;
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
        registerForContextMenu(listView);

        return v;
    }

    //Context Menu-------------------------------------
    private ContextMenu cmenu;
    private View cv;
    private ContextMenu.ContextMenuInfo cmenuInfo;
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        // TODO Auto-generated method stub

        menu.setHeaderTitle("메뉴");
        menu.add(0, v.getId(), 0, "재생목록에 추가");

        this.cmenu = menu;
        this.cv = v;
        this.cmenuInfo = menuInfo;

        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        if (item.getTitle() == "재생목록에 추가") {
            onPlayListChooser(global.playListManager.getPlayListList());

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
