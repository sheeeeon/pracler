package com.icaynia.soundki.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.icaynia.soundki.Data.UserManager;
import com.icaynia.soundki.Global;
import com.icaynia.soundki.Model.User;
import com.icaynia.soundki.R;
import com.icaynia.soundki.View.FindUserAdapter;

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


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finduser);
        global = (Global) getApplication();
        viewInitialize();
        prepare();
    }

    private void viewInitialize()
    {
        editText = (EditText) findViewById(R.id.findText);
        listView = (ListView) findViewById(R.id.listview);
        button = (Button) findViewById(R.id.commit);
    }

    private void prepare()
    {
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                update(editText.getText().toString());
            }
        });
    }

    private void update(final String username)
    {
        if (username.equals("")) return ;

        global.userManager.getUserList(username, 10, new UserManager.OnCompleteGetUserListListener()
        {
            @Override
            public void onComplete(ArrayList<User> UserList)
            {
                Log.e("username", username);
                FindUserAdapter adapter = new FindUserAdapter(getBaseContext(), UserList);
                listView.setAdapter(adapter);
            }
        });

    }


}
