package com.icaynia.soundki.Data;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.icaynia.soundki.Model.User;

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

    public void setLike()
    {

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


    public interface OnCompleteGetUserListener
    {
        void onComplete(User user);
    }



}
