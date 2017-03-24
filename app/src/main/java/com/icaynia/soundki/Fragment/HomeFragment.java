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

    private ListView playListView;
    private ArrayList<String> arrayList;
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
        playListView = (ListView) v.findViewById(R.id.playlistview);
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
        //PlayListManager plm = new PlayListManager(getContext());
        UpdateTask updateTask = new UpdateTask();
        updateTask.execute();
        //arrayList = plm.getPlayListList();
        //PlayListsAdapter playListsAdapter = new PlayListsAdapter(getContext(), arrayList);
        //playListView.setAdapter(playListsAdapter);

        //playListView.setOnItemClickListener(itemclick);
        //playListView.setOnItemLongClickListener(itemLongClick);




        //cv = (CardView) v.findViewById(R.id.card_yourfriends);
        //cv.setTitleText("친구가 듣고 있는 것");
        //cv.setTheme(CardView.THEME_3);
        //FriendStateView fsv = new FriendStateView(getContext());
        //cv.addContent(fsv);

//        CardView cvv = (CardView) v.findViewById(R.id.card_playlist);
//        cvv.deleteContent();
//        cvv.setTitleText("");
//        cvv.setTheme(CardView.THEME_1);
//        ButtonView bv = new ButtonView(getContext());
//        cvv.addContent(bv);

        swipeRefreshLayout.setRefreshing(false);
    }

    private AdapterView.OnItemClickListener itemclick = new AdapterView.OnItemClickListener()
    {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id)
        {
            onPlayListActivity(arrayList.get(position));
        }
    };

    private AdapterView.OnItemLongClickListener itemLongClick = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id)
        {
            ArrayList<String> menu = new ArrayList<>();
            // TODO Menu list
            menu.add("이 재생목록을 삭제");
            Bundle bundle = new Bundle();
            bundle.putStringArrayList("menu", menu);
            MenuSelecter menuSelecter = new MenuSelecter(getContext());
            menuSelecter.setBundle(bundle);
            menuSelecter.show();
            menuSelecter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                {
                    if (position == 0)
                    {

                    }
                }
            });
            return false;
        }
    };

    private void onPlayListActivity(String extra)
    {
        Intent intent = new Intent(this.getContext(), PlayListActivity.class);
        intent.putExtra("list", extra);
        startActivity(intent);
    }

    public class UpdateTask extends AsyncTask<String, Void, Void>
    {
        private static final String TAG = "AlbumImageTask";
        private int randint;
        private ImageView imgView;

        public void setImgView(ImageView _imgView)
        {
            this.imgView = _imgView;
        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            Log.i(TAG, "PreExecute");
        }

        @Override
        protected Void doInBackground(String... id)
        {
            Random rand = new Random();
            int size = global.mMusicManager.getMusicList().size();
            randint = rand.nextInt(size);

            for (String i : id)
            {

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result)
        {
            super.onPostExecute(result);

            MusicDto musicDto = global.mMusicManager.getMusicList().getItem(randint);
            RecommandSongView rsv = new RecommandSongView(getContext());
            rsv.setRecommandSong(musicDto);
            rsv.setImage(global.mMusicManager.getAlbumImage(getContext(), Integer.parseInt(musicDto.getAlbumId()), 100));
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
        }

    }
}


