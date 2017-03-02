package com.icaynia.soundki.View;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.icaynia.soundki.R;

/**
 * Created by icaynia on 02/03/2017.
 */

public class ProfileMenuHeader extends LinearLayout
{
    private Context context;
    public ProfileMenuHeader(Context context)
    {
        super(context);
        viewInitialize();
    }

    public ProfileMenuHeader(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        viewInitialize();
    }

    public void viewInitialize()
    {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rootView = inflater.inflate(R.layout.view_profilemenu_header, this, false);
        addView(rootView);
    }


}
