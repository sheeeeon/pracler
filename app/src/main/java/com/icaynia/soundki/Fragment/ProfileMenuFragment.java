package com.icaynia.soundki.Fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.google.firebase.auth.FirebaseUser;
import com.icaynia.soundki.Activity.ProfileActivity;
import com.icaynia.soundki.Global;
import com.icaynia.soundki.R;
import com.icaynia.soundki.View.ProfileMenuHeader;
import com.icaynia.soundki.View.ProfileRow;

import java.io.InputStream;
import java.net.URL;

/**
 * Created by icaynia on 2017. 2. 8..
 */

public class ProfileMenuFragment extends Fragment
{
    private Global global;
    private View v;
    private LinearLayout container1;

    private Handler handler = new Handler();

    private String userPhotoUrl;

    private FirebaseUser firebaseUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        v = inflater.inflate(R.layout.fragment_profile, container, false);
        setHasOptionsMenu(true);

        global = (Global) getContext().getApplicationContext();

        firebaseUser = global.loginUser;
        Log.e("looog", firebaseUser.getDisplayName());
        Log.e("looog", firebaseUser.getPhotoUrl().toString());
        Log.e("looog", firebaseUser.getEmail());
        Log.e("looog", firebaseUser.getProviders().toString());
        userPhotoUrl = firebaseUser.getPhotoUrl().toString();

        container1 = (LinearLayout) v.findViewById(R.id.container);
        listInitialize();

        return v;
    }

    private void onProfileActivity(String uid)
    {
        Intent intent = new Intent(getContext(), ProfileActivity.class);
        intent.putExtra("targetUid", uid);
        startActivity(intent);
    }

    private void listInitialize()
    {
        for (int i = 0; i < 1; i++)
        {
            ProfileRow profileRow = new ProfileRow(getContext());
            getImage(profileRow.imageView, userPhotoUrl);
            profileRow.setText(firebaseUser.getDisplayName());
            container1.addView(profileRow);
        }
    }

    private void getImage(final ImageView imageView, final String ul)
    {
        Thread t = new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    URL url = new URL(ul);
                    InputStream is = url.openStream();
                    final Bitmap bm = BitmapFactory.decodeStream(is);
                    handler.post(new Runnable()
                    {
                        @Override
                        public void run()
                        {  // 화면에 그려줄 작업
                            imageView.setImageBitmap(bm);
                        }
                    });
                    imageView.setImageBitmap(bm); //비트맵 객체로 보여주기
                }
                catch(Exception e)
                {

                }

            }
        });

        t.start();
    }


}
