package com.icaynia.soundki.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.icaynia.soundki.Global;
import com.icaynia.soundki.R;
import com.icaynia.soundki.adapters.PlayListsAdapter;

import java.util.ArrayList;

/**
 * Created by icaynia on 16/03/2017.
 */

public class PlayListsFragment extends Fragment
{
    private Global global;
    private View v;

    private ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        v = inflater.inflate(R.layout.fragment_myplaylists, container, false);
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
        final ArrayList<String> playLists = global.playListManager.getPlayListList();

        PlayListsAdapter playListsAdapter = new PlayListsAdapter(getContext(), playLists);
        listView.setAdapter(playListsAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                PlayListFragment plf = new PlayListFragment();
                plf.setPlayList(playLists.get(i));
                getFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
                        .add(R.id.framelayout, plf)
                        .addToBackStack(null)
                        .setTransition(FragmentTransaction.TRANSIT_ENTER_MASK)
                        .commit();
            }
        });
    }



}
