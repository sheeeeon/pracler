package com.icaynia.soundki.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.ListView;

import com.icaynia.soundki.R;

/**
 * Created by icaynia on 25/03/2017.
 */

public class FindUserActivity extends AppCompatActivity
{
    private EditText editText;
    private ListView listView;


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finduser);

        viewInitialize();
    }

    private void viewInitialize()
    {
        editText = (EditText) findViewById(R.id.findText);
        listView = (ListView) findViewById(R.id.listview);
    }

    private void update(String userid)
    {
        if (userid.equals("")) return ;


    }


}
