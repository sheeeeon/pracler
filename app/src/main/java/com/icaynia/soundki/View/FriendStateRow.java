package com.icaynia.soundki.View;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.icaynia.soundki.Data.UserManager;
import com.icaynia.soundki.Global;
import com.icaynia.soundki.Model.User;
import com.icaynia.soundki.R;

/**
 * Created by icaynia on 17/03/2017.
 */

public class FriendStateRow extends RelativeLayout
{
    private View mainView;
    private LayoutInflater inflater;
    private Global global;

    private String uid;
    private User thisUser;

    private ImageView userPic;
    private TextView userName;
    private TextView title;

    public FriendStateRow(Context context)
    {
        super(context);
        initialize();
    }

    public FriendStateRow(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        initialize();
    }

    private void initialize()
    {
        global = (Global) getContext().getApplicationContext();
        inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mainView = inflater.inflate(R.layout.view_list_friend_state, null);

        userPic = (ImageView) mainView.findViewById(R.id.view_userPic);
        userName = (TextView) mainView.findViewById(R.id.view_userName);
        title = (TextView) mainView.findViewById(R.id.view_title);

        addView(mainView);
    }

    public void setUid(String uid)
    {
        this.uid = uid;
        global.userManager.getUser(uid, new UserManager.OnCompleteGetUserListener() {
            @Override
            public void onComplete(User user) {
                thisUser = user;

                userName.setText(thisUser.name + "님이 듣는 중");
                global.userManager.getImage(thisUser.picture, new UserManager.OnCompleteGetUserImageListener() {
                    @Override
                    public void onComplete(Bitmap UserImage) {
                        userPic.setImageBitmap(UserImage);
                    }
                });
            }
        });

    }

    public void update()
    {

        global.userManager.getNowListening(uid, new UserManager.OnCompleteGetNowListening() {
            @Override
            public void onComplete(String str) {
                String[] r = str.split("&DL");
                String Artist = r[0];
                String Album = r[1];
                String Name = r[2];

                title.setText(Artist + " - " + Name);
            }
        });


    }
}
