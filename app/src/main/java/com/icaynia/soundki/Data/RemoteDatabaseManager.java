package com.icaynia.soundki.Data;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.icaynia.soundki.Model.PlayLog;
import com.icaynia.soundki.Model.User;

/**
 * Created by icaynia on 2017. 2. 8..
 *
 * 파이어베이스와 연동하는 부분
 * 파이어베이스에 추가시키거나 가져와서 리스너에 연결시키는 부분을 여기서 다 한다.
 */

public class RemoteDatabaseManager
{
    public RemoteDatabaseManager()
    {
    }

    public void addUser(User newUser)
    {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("User");

        myRef.child(newUser.user_id).setValue(newUser);
    }

    public void addPlayLog(String userId, PlayLog playLog)
    {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("User").child(userId).child("Log");

    }
}
