package com.icaynia.soundki;

import android.app.Application;
import android.content.res.Configuration;

/**
 * Created by icaynia on 2017. 2. 8..
 */

public class Global extends Application
{
    @Override
    public void onCreate() {
        super.onCreate();
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}
