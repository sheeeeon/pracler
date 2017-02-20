package com.icaynia.soundki.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;

import com.icaynia.soundki.Global;
import com.icaynia.soundki.R;
import com.icaynia.soundki.View.PlayListAdapter;

import java.util.List;

public class PlayListActivity extends AppCompatActivity
{
    public Global global;
    public ListView mainListView;
    public PlayListAdapter adapter;

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
        String listuid = intent.getStringExtra("list");
        onList(listuid);
    }

    public void viewInitialize()
    {
        mainListView = (ListView) findViewById(R.id.listview);
    }

    public void onList(String listuid)
    {
        // when 'listuid' value is 0, it means show now playlist.
        if (listuid.equals("0"))
        {
            this.getSupportActionBar().setTitle("현재 재생중 ー " + global.nowPlayingList.size() + "곡");

            adapter = new PlayListAdapter(this, global.nowPlayingList);

            mainListView.setAdapter(adapter);


        }
    }
}
