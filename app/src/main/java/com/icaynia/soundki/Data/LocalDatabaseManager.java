package com.icaynia.soundki.Data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.google.gson.Gson;
import com.icaynia.soundki.Model.PlayList;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import static android.R.attr.tag;

/**
 * Created by icaynia on 2017. 2. 8..
 */

public class LocalDatabaseManager
{
    private int dbVersion = 2;
    private MusicSQLiteOpenHelper helper;
    private Context context;

    private SQLiteDatabase db;

    public LocalDatabaseManager(Context context)
    {

        helper = new MusicSQLiteOpenHelper(
                context,
                "playlist",
                null,
                dbVersion
        );
        try
        {
            db = helper.getWritableDatabase(); // 읽고 쓸수 있는 DB
            //db = helper.getReadableDatabase(); // 읽기 전용 DB select문
        } catch (SQLiteException e) {
            e.printStackTrace();
            Log.e("LocalDatabaseManager", "데이터베이스를 얻어올 수 없음");
        }


    }

    /** NO USE !!! playlist
    public void addPlayList(PlayList playList)
    {
        Gson gson = new Gson();
        Log.e("SS", gson.toJson(playList));
        db.execSQL("insert into playlist values(null, '123', '123');");
    }

    public ArrayList<String> getPlayListArray()
    {
        Cursor cursor = db.rawQuery("SELECT * FROM playlist", null);
        while (cursor.moveToNext())
        {
            Log.e("getPlayList", cursor.getString(0)+", "+cursor.getString(1)+", "+cursor.getString(2));
        }
        return null;
    }
    */
}
