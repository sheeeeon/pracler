package com.icaynia.pracler.remote;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.icaynia.pracler.data.UserManager;
import com.icaynia.pracler.models.MusicDto;
import com.icaynia.pracler.models.User;
import com.icaynia.pracler.remote.listener.OnCompleteGetFirebaseUserListener;

/**
 * Created by icaynia on 29/03/2017.
 */

public class FirebaseUserManager
{
    public static void addNewUser(User newUser)
    {
        DatabaseReference database = getUsersReference();
        database.child(newUser.uid).child("profile").setValue(newUser);
    }



    public static void getNowListening()
    {

    }

    public static void setNowListening(MusicDto musicDto)
    {
        UserManager userManager = new UserManager();
        userManager.setNowlistening(musicDto.getArtist(), musicDto.getAlbum(), musicDto.getTitle());
    }

    public static void getUser(String uid, final OnCompleteGetFirebaseUserListener listener)
    {
        DatabaseReference database = getUsersReference();
        database.child(uid).child("profile").addListenerForSingleValueEvent(
                new ValueEventListener()
                {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot)
                    {
                        try
                        {
                            User user = dataSnapshot.getValue(User.class);
                            listener.onComplete(user);
                        }
                        catch ( Exception e )
                        {
                            Log.e("tag", "exception");
                            listener.onComplete(null);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError)
                    {

                    }
                });

    }

    private static DatabaseReference getUsersReference()
    {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference("Users");
        return database;
    }
}
