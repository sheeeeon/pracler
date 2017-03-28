package com.icaynia.pracleme.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.LinearLayout;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.icaynia.pracleme.Data.UserManager;
import com.icaynia.pracleme.Global;
import com.icaynia.pracleme.Model.User;
import com.icaynia.pracleme.R;

/**
 * Created by icaynia on 13/02/2017.
 */

public class Splash extends AppCompatActivity
{
    private Global global;
    private LoginButton facebookLoginButton;
    private CallbackManager mCallbackManager;
    FirebaseAuth firebaseAuth;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        global = (Global) getApplication();
        mCallbackManager = CallbackManager.Factory.create();
        firebaseAuth = FirebaseAuth.getInstance();

        facebookLoginButton = (LoginButton) findViewById(R.id.login_button);
        facebookLoginButton.setReadPermissions("email", "public_profile");
        facebookLoginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult)
            {
                Log.d("facebook", "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel()
            {
                Log.d("facebook", "facebook:cancel:");

            }

            @Override
            public void onError(FacebookException error)
            {
                Log.d("facebook", "facebook:error" + error.toString());

            }
        });


        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run()
            {
                if (firebaseAuth.getCurrentUser() != null)
                {
                    onMainActivity();
                }
                else
                {
                    facebookLoginButton.setVisibility(LinearLayout.VISIBLE);
                }
            }
        }, 1500);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result back to the Facebook SDK
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d("Facebook", "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("Facebook", "signInWithCredential:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w("Facebook", "signInWithCredential", task.getException());
                        }

                        global.loginUser = firebaseAuth.getCurrentUser();

                        global.userManager.setLoginUser(global.loginUser);

                        global.userManager.getUser(global.loginUser.getUid(), new UserManager.OnCompleteGetUserListener()
                        {
                            @Override
                            public void onComplete(User user)
                            {
                                if (user == null)
                                {
                                    global.userManager.addNewUser();
                                }

                                onMainActivity();
                            }
                        });
                    }
                });

    }

    public void onMainActivity()
    {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
