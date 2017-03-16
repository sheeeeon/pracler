package com.icaynia.soundki.Data;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.icaynia.soundki.Model.MusicDto;
import com.icaynia.soundki.Model.PlayHistory;
import com.icaynia.soundki.Model.State;
import com.icaynia.soundki.Model.User;

import java.io.InputStream;
import java.net.URL;

/**
 * Created by icaynia on 07/03/2017.
 */

public class UserManager
{
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

    public void addHistory(PlayHistory playHistory)
    {
        RemoteDatabaseManager rdm = new RemoteDatabaseManager();
        DatabaseReference dr = rdm.getUsersReference();

        playHistory.Regdate = MusicDto.replaceForInput("20170313101010");
        playHistory.artist = MusicDto.replaceForInput(playHistory.artist);
        playHistory.album = MusicDto.replaceForInput(playHistory.album);
        playHistory.title = MusicDto.replaceForInput(playHistory.title);

        dr.child(loginUser.getUid()).child("log").child(playHistory.Regdate).setValue(playHistory);
    }

    public void setLike(String artist, String album, String title, boolean state, final OnCompleteGetLikeState listener)
    {
        RemoteDatabaseManager rdm = new RemoteDatabaseManager();
        DatabaseReference dr = rdm.getUsersReference();

        artist = MusicDto.replaceForInput(artist);
        album = MusicDto.replaceForInput(album);
        title = MusicDto.replaceForInput(title);

        State mstate = new State();
        mstate.setState(state);

        dr.child(loginUser.getUid()).child("like").child(artist+"/"+album+"/"+title).setValue(mstate);
        listener.onComplete(state);
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

        dr.child(loginUser.getUid()).child("now").setValue(artist+"&DL"+album+"&DL"+title);
    }

    public void getNowListening(String uid, final OnCompleteGetNowListening listener)
    {
        rdm.getUsersReference().child(uid).child("now").addListenerForSingleValueEvent(
                new ValueEventListener()
                {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot)
                    {
                        String state = dataSnapshot.getValue(String.class);
                        if (state == null) return;
                        Log.e(TAG, "getNowListening:complete, " + state);
                        listener.onComplete(state);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError)
                    {
                        Log.e(TAG, "getNowListening:onCancelled", databaseError.toException());
                    }
                });
    }

    public void isLove(String userId, String artist, String album, String title, final OnCompleteGetLikeState listener)
    {
        RemoteDatabaseManager rdm = new RemoteDatabaseManager();

        artist = MusicDto.replaceForInput(artist);
        album = MusicDto.replaceForInput(album);
        title = MusicDto.replaceForInput(title);

        rdm.getUsersReference().child(userId).child("like").child(artist+"/"+album+"/"+title).addListenerForSingleValueEvent(
                new ValueEventListener()
                {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot)
                    {
                        State state = dataSnapshot.getValue(State.class);
                        if (state == null) return;
                        Log.e(TAG, "getLikeState:complete, " + state.getState());
                        listener.onComplete(state.getState());
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError)
                    {
                        Log.e(TAG, "getLikeState:onCancelled", databaseError.toException());
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

    public interface OnCompleteGetNowListening
    {
        void onComplete(String str);
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
