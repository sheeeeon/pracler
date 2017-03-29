package com.icaynia.pracler.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.icaynia.pracler.Data.UserManager;
import com.icaynia.pracler.Global;
import com.icaynia.pracler.models.User;
import com.icaynia.pracler.R;
import com.icaynia.pracler.adapters.FindUserAdapter;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;

/**
 * Created by icaynia on 25/03/2017.
 */

public class FindUserActivity extends AppCompatActivity
{
    private EditText editText;
    private ListView listView;
    private Button button;

    private Global global;

    private ArrayList<User> nowList;
    private ArrayList<String> followingList;

    private AVLoadingIndicatorView loadingbar;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finduser);
        global = (Global) getApplication();
        viewInitialize();
        prepare();
        loadingbar.hide();
    }

    private void viewInitialize()
    {
        editText = (EditText) findViewById(R.id.findText);
        listView = (ListView) findViewById(R.id.listview);
        button = (Button) findViewById(R.id.commit);
        loadingbar = (AVLoadingIndicatorView) findViewById(R.id.loadingBar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle(getString(R.string.search_user));

    }

    private void prepare()
    {
        global.userManager.getFollowingList(global.loginUid, new UserManager.OnCompleteGetUserFollowingListener()
        {
            @Override
            public void onComplete(ArrayList<String> list)
            {
                followingList = list;
            }
        });
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                loadingbar.show();
                update(editText.getText().toString());
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {

            }
        });
    }

    private void update(final String username)
    {
        if (username.equals(""))
        {
            loadingbar.hide();
            return ;
        }

        loadingbar.show();

        global.userManager.getUserList(username, 10, new UserManager.OnCompleteGetUserListListener()
        {
            @Override
            public void onComplete(ArrayList<User> UserList)
            {
                nowList = UserList;
                Log.e("username", username);
                FindUserAdapter adapter = new FindUserAdapter(getBaseContext(), UserList);
                listView.setAdapter(adapter);
                adapter.setFollowList(followingList);
                adapter.setOnClickListener(new FindUserAdapter.OnClickListener()
                {
                    @Override
                    public void onClick(String str, int position)
                    {
                        if (str.equals("profile"))
                        {
                            onProfileActivity(nowList.get(position).uid);
                        }
                        else if (str.equals("follow-enable"))
                        {
                            global.userManager.setFollowing(global.firebaseAuth.getCurrentUser().getUid(),
                                    nowList.get(position).uid, true);
                        }
                        else if (str.equals("follow-disable"))
                        {
                            global.userManager.setFollowing(global.firebaseAuth.getCurrentUser().getUid(),
                                    nowList.get(position).uid, false);
                        }
                    }
                });
                loadingbar.hide();
            }
        });
    }

    private void onProfileActivity(String uid)
    {
        Intent intent = new Intent(this, ProfileActivity.class);
        intent.putExtra("targetUid", uid);
        startActivity(intent);
    }




}
