package com.icaynia.soundki.View;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.icaynia.soundki.Data.UserManager;
import com.icaynia.soundki.Global;
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

        addView(mainView);
    }

    public void setUid(String uid)
    {
        this.uid = uid;
    }

    public void update()
    {
        global.userManager.getNowListening(uid, new UserManager.OnCompleteGetNowListening() {
            @Override
            public void onComplete(String str) {
                TextView title = (TextView) mainView.findViewById(R.id.view_title);
                title.setText("얍" + uid);
            }
        });
    }
}
