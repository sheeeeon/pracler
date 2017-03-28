package com.icaynia.soundki.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseUser;
import com.icaynia.soundki.Activity.FindUserActivity;
import com.icaynia.soundki.Activity.ProfileActivity;
import com.icaynia.soundki.Data.UserManager;
import com.icaynia.soundki.Global;
import com.icaynia.soundki.Model.User;
import com.icaynia.soundki.R;
import com.icaynia.soundki.View.Card;
import com.icaynia.soundki.adapters.ProfileMenuAdapter;

import java.util.ArrayList;

/**
 * Created by icaynia on 2017. 2. 8..
 */

public class ProfileMenuFragment extends Fragment
{
    private Global global;
    private View v;
    private ListView listView;

    private Handler handler = new Handler();

    private String userPhotoUrl;

    private FirebaseUser firebaseUser;

    private ArrayList<String> list;

    private int i = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        v = inflater.inflate(R.layout.fragment_profile, container, false);
        setHasOptionsMenu(true);

        global = (Global) getContext().getApplicationContext();

        firebaseUser = global.loginUser;
        Log.e("looog", firebaseUser.getDisplayName());
        Log.e("looog", firebaseUser.getPhotoUrl().toString());
        Log.e("looog", firebaseUser.getProviders().toString());

        listView = (ListView) v.findViewById(R.id.listview);
        prepare();
        return v;
    }

    private void onProfileActivity(String uid)
    {
        Intent intent = new Intent(getContext(), ProfileActivity.class);
        intent.putExtra("targetUid", uid);
        startActivity(intent);
    }

    private void onFindUserActivity()
    {
        Intent intent = new Intent(getContext(), FindUserActivity.class);
        startActivity(intent);
    }

    private void prepare()
    {
        Card card = new Card(getContext());
        card.setTitleText("친구 찾기");
        card.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Log.e("onClick", "findFriends");
                onFindUserActivity();
            }
        });
        listView.addFooterView(card);
        global.userManager.getFollowingList(firebaseUser.getUid(), new UserManager.OnCompleteGetUserFollowingListener()
        {
            @Override
            public void onComplete(final ArrayList<String> followingList)
            {
                followingList.add(0, firebaseUser.getUid());
                final ArrayList<User> userlist = new ArrayList<User>();
                for (i = 0; i < followingList.size(); i++)
                {
                    global.userManager.getUser(followingList.get(i), new UserManager.OnCompleteGetUserListener()
                    {
                        @Override
                        public void onComplete(User user)
                        {
                            userlist.add(user);
                            if (userlist.size() == followingList.size())
                            {
                                ProfileMenuAdapter profileMenuAdapter = new ProfileMenuAdapter(getContext(), userlist);
                                listView.setAdapter(profileMenuAdapter);
                            }
                        }
                    });
                }
                list = followingList;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                if (i >= list.size())
                {
                    onFindUserActivity();
                }
                else
                {
                    onProfileActivity(list.get(i));
                }
            }
        });


    }
}
