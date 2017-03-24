package com.icaynia.soundki.Fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.icaynia.soundki.Activity.PlayListActivity;
import com.icaynia.soundki.CardLayout.ButtonView;
import com.icaynia.soundki.Data.MusicFileManager;
import com.icaynia.soundki.Data.PlayListManager;
import com.icaynia.soundki.Global;
import com.icaynia.soundki.CardLayout.FriendStateView;
import com.icaynia.soundki.CardLayout.MyStateView;
import com.icaynia.soundki.CardLayout.RecommandSongView;
import com.icaynia.soundki.Model.MusicDto;
import com.icaynia.soundki.R;
import com.icaynia.soundki.View.Card;
import com.icaynia.soundki.View.CardView;
import com.icaynia.soundki.View.MenuSelecter;
import com.icaynia.soundki.View.PlayListsAdapter;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by icaynia on 2017. 2. 8..
 */

public class HomeFragment extends Fragment
{
    private Global global;
    private View v;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        global = (Global) getContext().getApplicationContext();
        v = inflater.inflate(R.layout.fragment_home, container, false);
        setHasOptionsMenu(true);
        viewInitialize();

        prepare();

        return v;
    }

    private void viewInitialize()
    {
        swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swiperefresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                prepare();
            }
        });
    }

    private void prepare()
    {
        swipeRefreshLayout.setRefreshing(true);

        UpdateTask updateTask = new UpdateTask();
        updateTask.execute();
    }

    public class UpdateTask extends AsyncTask<String, Void, MusicDto>
    {
        private static final String TAG = "AlbumImageTask";
        private int randint;

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            Log.i(TAG, "PreExecute");
        }

        @Override
        protected MusicDto doInBackground(String... id)
        {
            Random rand = new Random();
            int size = global.mMusicManager.getMusicList().size();
            randint = rand.nextInt(size);
            MusicDto musicDto = global.mMusicManager.getMusicList().getItem(randint);

            for (String i : id)
            {

            }

            return musicDto;
        }

        @Override
        protected void onPostExecute(MusicDto result)
        {
            super.onPostExecute(result);

            RecommandSongView rsv = new RecommandSongView(getContext());
            rsv.setRecommandSong(result);
            rsv.setImage(global.mMusicManager.getAlbumImage(getContext(), Integer.parseInt(result.getAlbumId()), 100));
            Card card = (Card) v.findViewById(R.id.card_recommand);
            card.setTitleText("이 곡도 들어 보세요");
            card.deleteContent();
            card.addContent(rsv);
            card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    global.playMusic(Integer.parseInt(global.mMusicManager.getMusicList().getItem(randint).getUid_local()));
                }
            });

            Card cv = (Card) v.findViewById(R.id.card_yourstate);
            cv.setTitleText("내 기록");
            cv.deleteContent();
            MyStateView msv = new MyStateView(getContext());
            msv.setPlayCount(global.localHistoryManager.getHistoryCount());
            msv.setMylikecount(global.localLikeManager.getSongLikeCount());
            cv.addContent(msv);

            swipeRefreshLayout.setRefreshing(false);
        }
    }
}


