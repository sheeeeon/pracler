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
import com.icaynia.soundki.R;
import com.icaynia.soundki.View.MenuSelecter;
import com.icaynia.soundki.View.PlayListsAdapter;

import java.util.ArrayList;

/**
 * Created by icaynia on 2017. 2. 8..
 */

public class HomeFragment extends Fragment
{
    private View v;

    private ListView playListView;
    private ArrayList<String> arrayList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
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


