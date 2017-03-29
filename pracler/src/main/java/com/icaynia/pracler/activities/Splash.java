package com.icaynia.pracler.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.Toast;

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
import com.icaynia.pracler.Data.UserManager;
import com.icaynia.pracler.Global;
import com.icaynia.pracler.models.User;
import com.icaynia.pracler.R;
import com.icaynia.pracler.remote.FirebaseUserManager;
import com.icaynia.pracler.remote.listener.OnCompleteGetFirebaseUserListener;

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
                Toast.makeText(getBaseContext(), "서버에 연결할 수 없습니다. 인터넷 연결을 확인해주세요.", Toast.LENGTH_SHORT).show();
            }
        });


        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run()
            {
                if (firebaseAuth.getCurrentUser() != null)
                {
                    prepare();
                }
                else
                {
                    facebookLoginButton.setVisibility(LinearLayout.VISIBLE);
                }
            }
        }, 1500);
    }

    private void checkPermission() {
        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED
                || checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                // Explain to the user why we need to write the permission.
                Toast.makeText(this, "Read/Write external storage", Toast.LENGTH_SHORT).show();
            }

            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    1);
        } else {
            onMainActivity();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {


                    prepare();
                    onMainActivity();
                    // permission was granted, yay! do the
                    // calendar task you need to do.

                } else {

                    Log.d("splash", "Permission always deny");
                    Toast.makeText(this, getString(R.string.permission_must_to_grant_for_use_pracler), Toast.LENGTH_SHORT).show();
                    finish();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                break;
        }
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
                        prepare();
                    }
                });

    }

    public void prepare()
    {
        global.loginUser = firebaseAuth.getCurrentUser();

        global.userManager.setLoginUser(global.loginUser);

        /* 신규인지 확인 */
        FirebaseUserManager.getUser(global.loginUser.getUid(), new OnCompleteGetFirebaseUserListener()
        {
            @Override
            public void onComplete(User user)
            {
                if (user != null)
                {
                    Log.e("tag", "user "+user.uid+" already created.");
                }
                else if (user == null)
                {
                    Log.e("tag", "user wasn't created! now create.");
                    User newUser = new User();
                    newUser.uid = global.loginUser.getUid();
                    newUser.email = global.loginUser.getEmail();
                    newUser.name = global.loginUser.getDisplayName();
                    newUser.picture = global.loginUser.getPhotoUrl().toString();
                    FirebaseUserManager.addNewUser(newUser);
                }
                checkPermission();
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
