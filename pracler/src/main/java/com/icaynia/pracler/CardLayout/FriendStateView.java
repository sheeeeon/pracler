package com.icaynia.pracler.CardLayout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.icaynia.pracler.Global;
import com.icaynia.pracler.R;
import com.icaynia.pracler.View.FriendStateRow;

/**
 * Created by icaynia on 16/03/2017.
 *
 * 메인 화면에서 사용하는 레이아웃 뷰.
 *
 * 데이터는 global에서 가져옵니다.
 */

public class FriendStateView extends LinearLayout
{
    private View mainView;
    private LinearLayout contentBox;
    private Global global;

    private RelativeLayout listRow;
    private LayoutInflater inflater;
    private TextView Artist;
    private TextView Title;

    public FriendStateView(Context context)
    {
        super(context);
        viewInitialize();
    }

    public FriendStateView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        viewInitialize();
    }

    private void viewInitialize()
    {
        inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mainView = inflater.inflate(R.layout.view_main_friend_state, null);

        global = (Global) getContext().getApplicationContext();
        contentBox = (LinearLayout) mainView.findViewById(R.id.contentBox);

        listRow = (RelativeLayout) inflater.inflate(R.layout.view_list_friend_state, null);

        addView(mainView);

        addUserList("bodOteabRPU9K01qTTgRr75LlHw2");
        addUserList("9MR5nzIWaKQMRPYri8KvJBCF9wB2");
        addUserList(global.firebaseAuth.getCurrentUser().getUid());
    }

    private void addUserList(final String uid)
    {
        FriendStateRow fsr = new FriendStateRow(getContext());
        fsr.setUid(uid);
        contentBox.addView(fsr);
    }




}
