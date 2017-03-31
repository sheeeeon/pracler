package com.icaynia.pracler.remote;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.icaynia.pracler.remote.models.PraclerAlert;
import com.icaynia.pracler.utils.PraclerDate;

/**
 * Created by icaynia on 29/03/2017.
 */

public class FirebaseAlertManager
{
    public static void sendAlert(String fromUid, String toUid, String str, String imageUrl)
    {
        PraclerAlert alert = new PraclerAlert();
        alert.user_uid = fromUid;
        alert.messages = str;
        alert.regdate = PraclerDate.getNowDate();
        alert.image_url = imageUrl;

        DatabaseReference database = getUsersReference();
        database.child(toUid).child("alert").push().setValue(alert);
    }

    public static void deleteAlert(String uid)
    {
        DatabaseReference database = getUsersReference();
        database.child(uid).child("alert").setValue(null);
    }

    private static DatabaseReference getUsersReference()
    {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference("Users");
        return database;
    }
}
