package com.icaynia.soundki.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.icaynia.soundki.R;

/**
 * Created by icaynia on 06/03/2017.
 */

public class ProfileActivity extends AppCompatActivity
{
    private String targetUid;

    private FirebaseUser firebaseUser;

    /** VIEW */
    private TextView text_name;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        targetUid = getIntent().getStringExtra("targetUid");

        initializeView();
        setData(targetUid);
    }


    private void initializeView()
    {
        text_name = (TextView) findViewById(R.id.text_name);

    }

    private void setData(String uid)
    {

    }


}
