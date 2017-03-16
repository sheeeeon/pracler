package com.icaynia.soundki.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.icaynia.soundki.Activity.PlayListActivity;
import com.icaynia.soundki.Data.PlayListManager;
import com.icaynia.soundki.Global;
import com.icaynia.soundki.Layout.RecommandSongView;
import com.icaynia.soundki.R;
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
    }

    private void prepare()
    {
        PlayListManager plm = new PlayListManager(getContext());

        arrayList = plm.getPlayListList();
        PlayListsAdapter playListsAdapter = new PlayListsAdapter(getContext(), arrayList);
        playListView.setAdapter(playListsAdapter);

        playListView.setOnItemClickListener(itemclick);
        playListView.setOnItemLongClickListener(itemLongClick);

        // TODO Recommend for you block.
        Random rand = new Random();
        int randint = rand.nextInt(global.mMusicManager.getMusicList().size());

        CardView cv = (CardView) v.findViewById(R.id.card_recommand);
        cv.setTitleText("이 곡도 들어 보세요");
        cv.setTheme(CardView.THEME_5);
        RecommandSongView rsv = new RecommandSongView(getContext());
        rsv.setRecommandSong(global.mMusicManager.getMusicList().getItem(randint));

        cv.addContent(rsv);
        cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        cv = (CardView) v.findViewById(R.id.card_yourfriends);
        cv.setTitleText("친구가 듣고 있는 것");
        cv.setTheme(CardView.THEME_3);

        cv = (CardView) v.findViewById(R.id.card_yourstate);
        cv.setTitleText("나의 상태");
        cv.setTheme(CardView.THEME_4);

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
}


