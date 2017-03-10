package com.icaynia.soundki.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.icaynia.soundki.Data.UserManager;
import com.icaynia.soundki.Model.User;
import com.icaynia.soundki.R;

import org.w3c.dom.Text;

/**
 * Created by icaynia on 06/03/2017.
 */

public class ProfileActivity extends AppCompatActivity
{
    private String targetUid;

    private FirebaseUser firebaseUser;

    /** VIEW */
    private TextView text_name;
    private TextView text_description;
    private ImageView image_profile;

    private UserManager userManager;
    private User data_user;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        targetUid = getIntent().getStringExtra("targetUid");

        initializeView();
        setData(targetUid);
    }


    private void initializeView()
    {
        getSupportActionBar().setTitle("");
        text_name = (TextView) findViewById(R.id.text_name);
        text_description = (TextView) findViewById(R.id.text_description);
        image_profile = (ImageView) findViewById(R.id.image_profile);

    }

    private void setData(String uid)
    {
        userManager = new UserManager();
        userManager.getUser(uid, new UserManager.OnCompleteGetUserListener() {
            @Override
            public void onComplete(User user)
            {
                data_user = user;
                getSupportActionBar().setTitle(data_user.name);
                text_name.setText(data_user.name);
                text_description.setText(data_user.user_description);

                userManager.getImage(data_user.picture, new UserManager.OnCompleteGetUserImageListener() {
                    @Override
                    public void onComplete(Bitmap UserImage)
                    {
                        image_profile.setImageBitmap(UserImage);
                    }
                });
            }
        });


    }


}
