package com.icaynia.soundki.Activity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.icaynia.soundki.Data.PlayListManager;
import com.icaynia.soundki.Global;
import com.icaynia.soundki.Model.PlayList;
import com.icaynia.soundki.R;
import com.icaynia.soundki.View.MusicListAdapter;
import com.icaynia.soundki.View.PlayListAdapter;

import java.util.List;

public class PlayListActivity extends AppCompatActivity
{
    public Global global;
    public ListView mainListView;
    public PlayListAdapter adapter;
    private NestedScrollView scrollview;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });

        viewInitialize();
        global = (Global) this.getApplicationContext();

        Intent intent = getIntent();
        String listname = intent.getStringExtra("list");
        onList(listname);
    }

    public void viewInitialize()
    {
        mainListView = (ListView) findViewById(R.id.listview);
        scrollview = (NestedScrollView) findViewById(R.id.scrollview);
        mainListView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                scrollview.requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

    }

    public void onList(String listname)
    {
        // when 'listname' value is 0, it means show now playlist.
        if (listname.equals("0"))
        {
            this.getSupportActionBar().setTitle("현재 재생중 ー " + global.nowPlayingList.size() + "곡");

            adapter = new PlayListAdapter(this, global.nowPlayingList);

            mainListView.setAdapter(adapter);

        }
        else
        {
            PlayListManager plm = new PlayListManager(this);
            PlayList playList = plm.getPlayList(listname);

            PlayListAdapter pla = new PlayListAdapter(this, playList);
            mainListView.setAdapter(pla);

            if (playList.size() == 0)
            {
                Toast.makeText(this, "플레이리스트가 비어있습니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }


}


