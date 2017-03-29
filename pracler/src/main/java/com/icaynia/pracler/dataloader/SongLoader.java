package com.icaynia.pracler.dataloader;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import com.icaynia.pracler.models.MusicDto;
import com.icaynia.pracler.models.MusicList;
import com.icaynia.pracler.models.Song;
import com.icaynia.pracler.models.SongList;

import java.util.ArrayList;

/**
 * Created by icaynia on 29/03/2017.
 */

public class SongLoader
{
    public static String[] projection = {
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.ALBUM_ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.DURATION

    };

    public static SongList getSongs(Context context)
    {
        SongList list = new SongList();

        Cursor cursor = context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                projection, null, null, null);
        while(cursor.moveToNext()){
            Song song = new Song();
            song.uid = (cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media._ID)));
            song.album = (cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM)));
            song.albumUid = (cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID)));
            song.title = (cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)));
            song.artist = (cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)));
            song.length = (Integer.parseInt(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION))));
            list.add(song);
        }
        cursor.close();

        return list;
    }
}
