package com.icaynia.soundki.View;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.icaynia.soundki.R;

/**
 * Created by icaynia on 08/03/2017.
 */

public class ProfileRow extends LinearLayout
{
    private View mainView;

    private LayoutInflater inflater;
    private RelativeLayout container;

    public ImageView imageView;

    public ProfileRow(Context context)
    {
        super(context);
        this.viewInitialize();
    }

    public ProfileRow(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        this.viewInitialize();
    }

    public void viewInitialize()
    {
        inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mainView = inflater.inflate(R.layout.view_list_profilemenurows, null);
        container = (RelativeLayout) mainView.findViewById(R.id.container);
        imageView = (ImageView) mainView.findViewById(R.id.view_image);
        addView(mainView);
    }

    public void setImage(Bitmap bitmap)
    {
        imageView.setImageBitmap(bitmap);
    }

    public void setText(String str)
    {
        TextView textView = (TextView) mainView.findViewById(R.id.view_title);
        textView.setText(str);
    }

    public void setOnClickListener(OnClickListener listener)
    {
        container.setOnClickListener(listener);
    }

}
