package com.icaynia.soundki.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.icaynia.soundki.Global;
import com.icaynia.soundki.R;

/**
 * Created by icaynia on 2017. 2. 8..
 */

public class ProfileFragment extends Fragment
{
    private Global global;
    private View v;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        v = inflater.inflate(R.layout.fragment_profile, container, false);
        setHasOptionsMenu(true);

        global = (Global) getContext().getApplicationContext();

        FirebaseUser firebaseUser = global.loginUser;
        Log.e("looog", firebaseUser.getDisplayName());
        Log.e("looog", firebaseUser.getPhotoUrl().toString());
        Log.e("looog", firebaseUser.getEmail());
        Log.e("looog", firebaseUser.getProviders().toString());


        return v;
    }
}
