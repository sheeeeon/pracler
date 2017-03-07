package com.icaynia.soundki.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.icaynia.soundki.Activity.ProfileActivity;
import com.icaynia.soundki.Data.RemoteDatabaseManager;
import com.icaynia.soundki.Global;
import com.icaynia.soundki.R;
import com.icaynia.soundki.View.ProfileMenuAdapter;
import com.icaynia.soundki.View.ProfileMenuHeader;

import java.util.ArrayList;

/**
 * Created by icaynia on 2017. 2. 8..
 */

public class ProfileMenuFragment extends Fragment
{
    private Global global;
    private View v;

    private ListView listView;
    private ProfileMenuHeader profileMenuHeader;

    private ProfileMenuAdapter profileMenuAdapter;

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

        profileMenuHeader = new ProfileMenuHeader(getContext());
        listView = (ListView) v.findViewById(R.id.profilemenu_list);
        listView.addHeaderView(profileMenuHeader);

        ArrayList<String> menuString = new ArrayList<>();
        menuString.add(firebaseUser.getDisplayName());
        menuString.add("menu 2");
        menuString.add("menu 3");

        ProfileMenuAdapter profileMenuAdapter = new ProfileMenuAdapter(getContext(), menuString);

        listView.setAdapter(profileMenuAdapter);
        final String uid = firebaseUser.getUid();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                if (position == 1) // profile
                {
                    onProfileActivity(uid);
                }
            }
        });

        return v;
    }

    private void onProfileActivity(String uid)
    {
        Intent intent = new Intent(getContext(), ProfileActivity.class);
        intent.putExtra("targetUid", uid);
        startActivity(intent);
    }

}
