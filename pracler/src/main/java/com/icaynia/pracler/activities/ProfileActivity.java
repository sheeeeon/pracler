package com.icaynia.pracler.activities;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseUser;
import com.icaynia.pracler.data.UserManager;
import com.icaynia.pracler.models.MusicDto;
import com.icaynia.pracler.models.User;
import com.icaynia.pracler.R;
import com.icaynia.pracler.adapters.PlayLogListAdapter;

import java.util.ArrayList;

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
    private TextView text_nowlistening;
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
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        targetUid = getIntent().getStringExtra("targetUid");

        initializeView();
        updateData(targetUid);
    }

    private void initializeView()
    {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        getSupportActionBar().setTitle("");
        text_name = (TextView) findViewById(R.id.text_name);
        text_description = (TextView) findViewById(R.id.text_description);
        image_profile = (ImageView) findViewById(R.id.image_profile);
        text_nowlistening = (TextView) findViewById(R.id.content_now_listening_text);
    }

    private void updateData(final String uid)
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

                userManager.getNowListening(uid, new UserManager.OnCompleteGetNowListening() {
                    @Override
                    public void onComplete(String str) {
                        text_nowlistening.setText("현재 재생중 : " + str.replace("&DL", " - "));
                    }
                });
            }
        });


        // history
        ArrayList<MusicDto> musicDto = new ArrayList<MusicDto>();

        for (int i = 0; i < 35; i++)
        {
            MusicDto dto = new MusicDto();
            dto.setTitle("0000-00-00 - name "+i);
            //musicDto.add(dto)=
        }
        recyclerView.setAdapter(new PlayLogListAdapter(musicDto));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
