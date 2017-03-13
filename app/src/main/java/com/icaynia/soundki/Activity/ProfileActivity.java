package com.icaynia.soundki.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.icaynia.soundki.Data.UserManager;
import com.icaynia.soundki.Model.MusicDto;
import com.icaynia.soundki.Model.User;
import com.icaynia.soundki.R;
import com.icaynia.soundki.View.PlayLogListAdapter;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

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

    private RecyclerView recyclerView;

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

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

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

        ArrayList<MusicDto> musicDto = new ArrayList<MusicDto>();
        for (int i = 0; i < 35; i++)
        {
            MusicDto dto = new MusicDto();
            dto.setTitle("0000-00-00 - name "+i);
            musicDto.add(dto);
        }
        recyclerView.setAdapter(new PlayLogListAdapter(musicDto));
    }
}
