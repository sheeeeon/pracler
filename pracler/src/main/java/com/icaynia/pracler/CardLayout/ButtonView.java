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

/**
 * Created by icaynia on 16/03/2017.
 *
 * 메인 화면에서 사용하는 레이아웃 뷰.
 *
 * 데이터는 global에서 가져옵니다.
 */

public class ButtonView extends LinearLayout
{
    private View mainView;
    private LinearLayout contentBox;
    private Global global;

    private RelativeLayout listRow;
    private LayoutInflater inflater;
    private TextView Artist;
    private TextView Title;

    public ButtonView(Context context)
    {
        super(context);
        viewInitialize();
    }

    public ButtonView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        viewInitialize();
    }

    private void viewInitialize()
    {
        inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mainView = inflater.inflate(R.layout.view_main_button, null);

        global = (Global) getContext().getApplicationContext();
        contentBox = (LinearLayout) mainView.findViewById(R.id.contentBox);

        addView(mainView);

    }


}
