package com.icaynia.pracler.View;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.icaynia.pracler.R;

/**
 * Created by icaynia on 16/03/2017.
 */

public class CardView extends LinearLayout
{
    private View view;
    private LayoutInflater inflater;

    private LinearLayout layout;
    private LinearLayout contentBox;
    private TextView titleText;

    public static String THEME_1 = "THEME_1";
    public static String THEME_2 = "THEME_2";
    public static String THEME_3 = "THEME_3";
    public static String THEME_4 = "THEME_4";
    public static String THEME_5 = "THEME_5";

    public CardView(Context context)
    {
        super(context);
        initialize();
    }

    public CardView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        initialize();
    }


    private void initialize()
    {
        inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.view_cardv, null);
        this.addView(view);

        layout = (LinearLayout) view.findViewById(R.id.layout);
        titleText = (TextView) view.findViewById(R.id.titleText);
        contentBox = (LinearLayout) view.findViewById(R.id.contentBox);
    }

    public void setTitleText(String str)
    {
        titleText.setText(str);
    }

    public void setOnClickListener(OnClickListener listener)
    {
        view.setOnClickListener(listener);
    }

    public void setTheme(String THEME)
    {
        Resources res = getResources();
        if (THEME == THEME_1) layout.setBackground(res.getDrawable(R.drawable.view_card_theme_1));
        if (THEME == THEME_2) layout.setBackground(res.getDrawable(R.drawable.view_card_theme_2));
        if (THEME == THEME_3) layout.setBackground(res.getDrawable(R.drawable.view_card_theme_3));
        if (THEME == THEME_4) layout.setBackground(res.getDrawable(R.drawable.view_card_theme_4));
        if (THEME == THEME_5) layout.setBackground(res.getDrawable(R.drawable.view_card_theme_5));
    }

    public void addContent(View v)
    {
        contentBox.addView(v);
    }

}
