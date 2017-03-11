package com.icaynia.soundki.Data;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.icaynia.soundki.Model.MusicDto;
import com.icaynia.soundki.Model.User;

import java.io.InputStream;
import java.net.URL;

/**
 * Created by icaynia on 07/03/2017.
 */

public class UserManager
{
    private Context context;

    private String TAG = "UserManager";
    private FirebaseAuth firebaseAuth;
    private FirebaseUser loginUser;
    private RemoteDatabaseManager rdm;

    public UserManager()
    {
        firebaseAuth = FirebaseAuth.getInstance();
        loginUser = firebaseAuth.getCurrentUser();
        rdm = new RemoteDatabaseManager();
    }

    public void getUser(String userId, final OnCompleteGetUserListener listener)
    {
        rdm.getUsersReference().child(userId).child("profile").addListenerForSingleValueEvent(
            new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // Get user value
                    User user = dataSnapshot.getValue(User.class);
                    Log.e(TAG, "getUser:complete, " + user.email);
                    listener.onComplete(user);
                    // ...
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.e(TAG, "getUser:onCancelled", databaseError.toException());
                }
            });
    }

    public void setLike(String artist, String album, String title)
    {
        RemoteDatabaseManager rdm = new RemoteDatabaseManager();
        DatabaseReference dr = rdm.getUsersReference();
        DatabaseReference songRef = rdm.getSongsReference();

        artist = MusicDto.replaceForInput(artist);
        album = MusicDto.replaceForInput(album);
        title = MusicDto.replaceForInput(title);

        boolean state = true;

        dr.child(loginUser.getUid()).child("like").child(artist)
                .child(album).child(title).setValue(state);
    }

    public void addNewUser()
    {
        RemoteDatabaseManager rdm = new RemoteDatabaseManager();
        DatabaseReference dr = rdm.getUsersReference();
        User user = new User();
        user.name = loginUser.getDisplayName();
        user.picture = loginUser.getPhotoUrl().toString();
        user.email = loginUser.getEmail();

        dr.child(loginUser.getUid()).child("profile").setValue(user);
    }

    public void setNowlistening(String artist, String album, String title)
    {
        RemoteDatabaseManager rdm = new RemoteDatabaseManager();
        DatabaseReference dr = rdm.getUsersReference();

        artist = MusicDto.replaceForInput(artist);
        album = MusicDto.replaceForInput(album);
        title = MusicDto.replaceForInput(title);

        dr.child(loginUser.getUid()).child("now").setValue(artist+"/"+album+"/"+title);
    }

    public void isLove(String userId, String artist, String album, String title, final OnCompleteGetLikeState listener)
    {
        RemoteDatabaseManager rdm = new RemoteDatabaseManager();

        artist = MusicDto.replaceForInput(artist);
        album = MusicDto.replaceForInput(album);
        title = MusicDto.replaceForInput(title);

        rdm.getUsersReference().child(userId).child("like").child(artist).child(album).child(title).addListenerForSingleValueEvent(
                new ValueEventListener()
                {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot)
                    {
                        // Get user value
                        boolean state = dataSnapshot.getValue(boolean.class);
                        Log.e(TAG, "getLikeState:complete, " + state);
                        listener.onComplete(state);
                        // ...
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError)
                    {
                        Log.e(TAG, "getUser:onCancelled", databaseError.toException());
                    }
                });
    }


    public interface OnCompleteGetUserListener
    {
        void onComplete(User user);
    }

    public interface OnCompleteGetUserImageListener
    {
        void onComplete(Bitmap UserImage);
    }

    public void getImage(final String Url, final OnCompleteGetUserImageListener listener)
    {
        Thread t = new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    URL url = new URL(Url);
                    InputStream is = url.openStream();
                    Bitmap bm = BitmapFactory.decodeStream(is);
                    listener.onComplete(bm);

                }
                catch(Exception e)
                {

                }

            }
        });

        t.start();
    }

    public interface OnCompleteGetLikeState
    {
        void onComplete(boolean likeState);
    }


}
