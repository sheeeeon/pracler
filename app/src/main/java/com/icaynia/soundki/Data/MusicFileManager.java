package com.icaynia.soundki.Data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;

import com.icaynia.soundki.Model.MusicDto;
import com.icaynia.soundki.Model.MusicList;
import com.icaynia.soundki.Model.PlayList;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by icaynia on 2017. 2. 8..
 */

public class MusicFileManager
{
    private Context context;

    private String TAG = "MusicFileManager";
    private String rootSD;

    private Bitmap tmpBitmap;


    public MusicFileManager(Context context)
    {
        this.context = context;
    }

    public MusicList getMusicList() {

        MusicList playList = new MusicList();
        //가져오고 싶은 컬럼 명을 나열합니다. 음악의 아이디, 앰블럼 아이디, 제목, 아티스트 정보를 가져옵니다.
        String[] projection = {
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.ALBUM_ID,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST
        };

        if (context == null) {
            return null;
        }

        Cursor cursor = context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                projection, null, null, null);

        while(cursor.moveToNext()){
            MusicDto musicDto = new MusicDto();
            musicDto.uid_local = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media._ID));
            musicDto.album = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
            musicDto.album_id = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));
            musicDto.title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
            musicDto.artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));

            playList.addItem(musicDto);
        }
        cursor.close();

        return playList;
    }

    public ArrayList<MusicDto> sort(ArrayList<MusicDto> mediaList, int SORT)
    {
        Comparator<MusicDto> compare;
        switch (SORT) {
            case 0: /** NAME */
            compare = new Comparator<MusicDto>()
            {
                @Override
                public int compare(MusicDto lhs, MusicDto rhs)
                {
                    char lhsVal = lhs.title.charAt(0); /** for example : 'f' */
                    char rhsVal = rhs.title.charAt(0); /** for example : 'a' */

                    if (lhsVal > rhsVal) /** true */
                    {
                        return 1;
                    }
                    else if (lhsVal == rhsVal) /** false */
                    {
                        return 0;
                    }
                    else /** false */
                    {
                        return -1;
                    }
                }
            };
            break;
            case 1: /** ALBUM */
                compare = new Comparator<MusicDto>()
                {
                    @Override
                    public int compare(MusicDto lhs, MusicDto rhs)
                    {
                        Log.e("compare", "t : "+lhs.album);

                        char lhsVal, rhsVal;
                        if (lhs.album == null)
                        {
                            lhsVal = '0';
                        }
                        else
                        {
                            lhsVal = lhs.album.charAt(0);
                        }
                        if (rhs.album == null)
                        {
                            rhsVal = '0';
                        }
                        else
                        {
                            rhsVal = rhs.album.charAt(0);
                        }


                        if (lhsVal > rhsVal) /** true */
                        {
                            return 1;
                        }
                        else if (lhsVal == rhsVal) /** false */
                        {
                            return 0;
                        }
                        else /** false */
                        {
                            return -1;
                        }
                    }
                };
                break;
            case 2: /** ARTIST */
                compare = new Comparator<MusicDto>()
                {
                    @Override
                    public int compare(MusicDto lhs, MusicDto rhs)
                    {
                        char lhsVal = lhs.artist.charAt(0); /** for example : 'f' */
                        char rhsVal = rhs.artist.charAt(0); /** for example : 'a' */

                        if (lhsVal > rhsVal) /** true */
                        {
                            return 1;
                        }
                        else if (lhsVal == rhsVal) /** false */
                        {
                            return 0;
                        }
                        else /** false */
                        {
                            return -1;
                        }
                    }
                };
                break;
            case 3: /** LENGTH */
                compare = new Comparator<MusicDto>()
                {
                    @Override
                    public int compare(MusicDto lhs, MusicDto rhs)
                    {
                        char lhsVal = lhs.artist.charAt(0); /** for example : 'f' */
                        char rhsVal = rhs.artist.charAt(0); /** for example : 'a' */

                        if (lhsVal > rhsVal) /** true */
                        {
                            return 1;
                        }
                        else if (lhsVal == rhsVal) /** false */
                        {
                            return 0;
                        }
                        else /** false */
                        {
                            return -1;
                        }
                    }
                };
                break;
            default:
                return null;
        }

        Collections.sort(mediaList, compare);
        return mediaList;
    }

    public String convertToTime(int millis)
    {
        long second = (millis / 1000) % 60;
        long minute = (millis / (1000 * 60)) % 60;
        long hour = (millis / (1000 * 60 * 60)) % 24;

        String time = String.format("%01d:%02d", minute, second);
        if (minute > 9) {
            time = String.format("%02d:%02d", minute, second);
        }
        if (hour > 0) {
            time = String.format("%01d:%02d:%02d", hour, minute, second);
        }
        if (hour > 9) {
            time = String.format("%02d:%02d:%02d", hour, minute, second);
        }

        return time;
    }


    public MusicDto getMusicDto(String id)
    {
        Log.e("first", id);
        String[] projection = {
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST
        };

        if (context == null)
        {
            return null;
        }
        Uri singleUri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,Integer.parseInt(id));
        Cursor cursor = context.getContentResolver().query(singleUri,
                projection, null, null, null);

        MusicDto musicDto = new MusicDto();
        while(cursor.moveToNext()){
            musicDto.uid_local = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media._ID));
            musicDto.album = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
            musicDto.title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
            musicDto.artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
            Log.e("tag", musicDto.uid_local + " , " + musicDto.title);
        }

        return musicDto;

    }

    public int getLength(String songid)
    {
        MediaPlayer mp = new MediaPlayer();
        Uri musicURI = Uri.withAppendedPath(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, songid);
        try
        {
            mp.setDataSource(context, musicURI);
            mp.prepare();
        }
        catch (Exception e)
        {

        }
        return mp.getDuration();
    }

    public Bitmap getAlbumImage(Context context, int album_id, int MAX_IMAGE_SIZE) {
        // NOTE: There is in fact a 1 pixel frame in the ImageView used to
        // display this drawable. Take it into account now, so we don't have to
        // scale later.
        ContentResolver res = context.getContentResolver();
        Uri uri = Uri.parse("content://media/external/audio/albumart/" + album_id);
        if (uri != null) {
            ParcelFileDescriptor fd = null;
            try {
                fd = res.openFileDescriptor(uri, "r");

                //크기를 얻어오기 위한옵션 ,
                //inJustDecodeBounds값이 true로 설정되면 decoder가 bitmap object에 대해 메모리를 할당하지 않고, 따라서 bitmap을 반환하지도 않는다.
                // 다만 options fields는 값이 채워지기 때문에 Load 하려는 이미지의 크기를 포함한 정보들을 얻어올 수 있다.
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFileDescriptor(
                        fd.getFileDescriptor(), null, options);
                int scale = 0;
                if (options.outHeight > MAX_IMAGE_SIZE || options.outWidth > MAX_IMAGE_SIZE) {
                    scale = (int) Math.pow(2, (int) Math.round(Math.log(MAX_IMAGE_SIZE / (double) Math.max(options.outHeight, options.outWidth)) / Math.log(0.5)));
                }
                options.inJustDecodeBounds = false;
                options.inSampleSize = scale;



                tmpBitmap = BitmapFactory.decodeFileDescriptor(
                        fd.getFileDescriptor(), null, options);

                if (tmpBitmap != null) {
                    // finally rescale to exactly the size we need
                    if (options.outWidth != MAX_IMAGE_SIZE || options.outHeight != MAX_IMAGE_SIZE) {
                        Bitmap tmp = Bitmap.createScaledBitmap(tmpBitmap, MAX_IMAGE_SIZE, MAX_IMAGE_SIZE, true);
                        tmpBitmap.recycle();
                        tmpBitmap = tmp;
                    }
                }

                return tmpBitmap;
            } catch (FileNotFoundException e) {
            } finally {
                try {
                    if (fd != null)
                        fd.close();
                } catch (IOException e) {
                }
            }
        }
        return null;
    }

    private static final BitmapFactory.Options options = new BitmapFactory.Options();
}
