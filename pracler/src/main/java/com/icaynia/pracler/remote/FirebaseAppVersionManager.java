package com.icaynia.pracler.remote;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.icaynia.pracler.models.User;
import com.icaynia.pracler.remote.listener.OnCompleteGetFirebaseAppVersionListener;
import com.icaynia.pracler.remote.models.AppVersion;

/**
 * Created by icaynia on 02/04/2017.
 */

public class FirebaseAppVersionManager
{
    // TODO Do not call this method at publish app
    public static void InitAppVersion()
    {
        DatabaseReference database = getPreferenceReference();
        AppVersion appVersion = new AppVersion();
        appVersion.VERSION = 1;
        appVersion.CHANGE_NOTE = "First publish";
        database.child("APP_VERSION").setValue(appVersion);
    }

    public static void getAppVersion(final OnCompleteGetFirebaseAppVersionListener listener)
    {
        DatabaseReference database = getPreferenceReference();
        database.child("APP_VERSION").addListenerForSingleValueEvent(
                new ValueEventListener()
                {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot)
                    {
                        try
                        {
                            AppVersion ver = dataSnapshot.getValue(AppVersion.class);
                            listener.onComplete(ver);
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

    private static DatabaseReference getPreferenceReference()
    {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference("Preference");
        return database;
    }
}
