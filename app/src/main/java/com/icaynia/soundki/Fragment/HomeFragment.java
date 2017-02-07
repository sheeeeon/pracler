package com.icaynia.soundki.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.icaynia.soundki.R;

/**
 * Created by icaynia on 2017. 2. 8..
 */

public class HomeFragment extends Fragment
{
    private View v;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        v = inflater.inflate(R.layout.fragment_home, container, false);
        setHasOptionsMenu(true);

        return v;
    }
}
