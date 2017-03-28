package com.icaynia.pracleme.Data;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by icaynia on 2017. 2. 8..
 *
 * 파이어베이스와 연동하는 부분
 */

public class RemoteDatabaseManager
{

    private FirebaseDatabase database;
    private DatabaseReference myRef;

    public RemoteDatabaseManager()
    {
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
    }

    public DatabaseReference getUsersReference()
    {
        DatabaseReference userRef = database.getReference("Users");
        return userRef;
    }

    public DatabaseReference getSongsReference()
    {
        DatabaseReference userRef = database.getReference("Songs");
        return userRef;
    }
}
