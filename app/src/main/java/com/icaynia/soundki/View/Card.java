package com.icaynia.soundki.View;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.icaynia.soundki.R;

/**
 * Created by icaynia on 20/03/2017.
 *
 * data는 다루지 않음.
 *
 */

public class Card extends RelativeLayout
{
    private View mainView;
    private TextView titleText;
    private TextView buttonTitleText;

    public Card(Context context)
    {
        super(context);
        onCreate();
    }

    public Card(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        onCreate();
    }

    public void onCreate()
    {
        initializeView();
    }

    public void initializeView()
    {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mainView = inflater.inflate(R.layout.view_card, this, false);
        addView(mainView);
        RelativeLayout contentBox = (RelativeLayout) mainView.findViewById(R.id.contentBox);
        titleText = (TextView) mainView.findViewById(R.id.titleText);
        buttonTitleText = (TextView) mainView.findViewById(R.id.buttonTitleText);

    }

    public void setTitleText(String titleString)
    {
        titleText.setText(titleString);
    }

    public void setButtonImageDrawable(Drawable drawable)
    {
        this.setButtonMode();
        ImageView buttonImage = (ImageView) mainView.findViewById(R.id.buttonImage);
        buttonImage.setImageDrawable(drawable);
    }

    public void setButtonTitleText(String titleString)
    {
        this.setButtonMode();
        buttonTitleText.setText(titleString);
    }

    private void setButtonMode()
    {
        RelativeLayout buttonBox = (RelativeLayout) mainView.findViewById(R.id.buttonBox);
        buttonBox.setVisibility(VISIBLE);
        titleText.setVisibility(GONE);
        buttonTitleText.setVisibility(VISIBLE);
    }



}
