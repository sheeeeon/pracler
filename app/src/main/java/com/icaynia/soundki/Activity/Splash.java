package com.icaynia.soundki.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;

import com.icaynia.soundki.R;

/**
 * Created by icaynia on 13/02/2017.
 */

public class Splash extends AppCompatActivity
{
    private LinearLayout facebookLoginButton;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        facebookLoginButton = (LinearLayout) findViewById(R.id.facebook_login_button);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run()
            {
                facebookLoginButton.setVisibility(LinearLayout.VISIBLE);
            }
        }, 1000);
    }

    public void onFacebookLoginButton()
    {

    }
}
