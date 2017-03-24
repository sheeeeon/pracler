package com.icaynia.soundki.View;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
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

public class CardHeader extends RelativeLayout
{
    private View mainView;
    private TextView titleText;
    private TextView buttonTitleText;
    private RelativeLayout contentBox;
    private LinearLayout content;

    public CardHeader(Context context)
    {
        super(context);
        onCreate();
    }

    public CardHeader(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        onCreate();
    }

    @TargetApi(21)
    public CardHeader(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        onCreate();
    }

    public void onCreate()
    {
        initializeView();
    }

    public void initializeView()
    {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mainView = inflater.inflate(R.layout.view_card_header, this, false);
        addView(mainView);
        contentBox = (RelativeLayout) mainView.findViewById(R.id.contentBox);
        titleText = (TextView) mainView.findViewById(R.id.titleText);
        buttonTitleText = (TextView) mainView.findViewById(R.id.buttonTitleText);
        content = (LinearLayout) mainView.findViewById(R.id.content);
        contentBox.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    public void setOnClickListener(OnClickListener listener)
    {
        contentBox.setOnClickListener(listener);
    }

    public void setIcon(Bitmap bitmap)
    {
        //추가 필요
    }

    public void setIcon(Drawable drawable)
    {
        //추가 필요
    }

    public void addContent(View v)
    {
        content.addView(v);
    }

    public void deleteContent()
    {
        content.removeAllViewsInLayout();
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
