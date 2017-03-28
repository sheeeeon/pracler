package com.icaynia.pracler.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.icaynia.pracler.R;

/**
 * Created by icaynia on 16/03/2017.
 */

public class RootFragmentPos1 extends Fragment
{
    private View v;
    private FrameLayout framelayout;
    // TODO VIEW
    private ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        v = inflater.inflate(R.layout.fragment_root_pos_1, container, false);
        setHasOptionsMenu(true);
        viewInitialize();
        prepare();
        return v;
    }

    public void viewInitialize()
    {
        framelayout = (FrameLayout) v.findViewById(R.id.framelayout);
    }

    public void prepare()
    {
        getFragmentManager().beginTransaction()
                .add(R.id.framelayout, new MyMusicListFragment())
                .commit();

    }


}
