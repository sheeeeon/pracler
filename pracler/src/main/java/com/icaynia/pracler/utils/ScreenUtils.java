package com.icaynia.pracler.utils;

import android.content.Context;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;

/**
 * Created by icaynia on 01/04/2017.
 */

public class ScreenUtils
{
    public static Point getScreenSize(Context context)
    {

        DisplayMetrics dm = context.getApplicationContext().getResources().getDisplayMetrics();
        int width = dm.widthPixels;
        int height = dm.heightPixels;

        Log.e("ScreenUtils:ScreenSize", width + ", " + height);
        Point size = new Point();
        size.x = width;
        size.y = height;
        return size;
    }
}
