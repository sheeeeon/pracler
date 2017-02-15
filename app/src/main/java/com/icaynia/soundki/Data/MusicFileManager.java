package com.icaynia.soundki.Data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.util.Log;

import com.icaynia.soundki.Model.MusicDto;

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


    public MusicFileManager(Context context)
    {
        this.context = context;
    }

    public ArrayList<MusicDto> getMusicList() {

        ArrayList<MusicDto> mediaList = new ArrayList<>();
        //가져오고 싶은 컬럼 명을 나열합니다. 음악의 아이디, 앰블럼 아이디, 제목, 아티스트 정보를 가져옵니다.
        String[] projection = {
                MediaStore.Audio.Media._ID,
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
            musicDto.id = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media._ID));
            musicDto.albumid = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));
            musicDto.title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
            musicDto.artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
            mediaList.add(musicDto);
        }
        cursor.close();

        Comparator<MusicDto> compare = new Comparator<MusicDto>()
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

        Collections.sort(mediaList, compare);

        return mediaList;
    }

    public int SORT_NAME;
    public int SORT_ALBUM;
    public int SORT_ARTIST;
    public int SORT_LENGTH;
    public int SORT_PLAYCOUNT;
    public ArrayList<MusicDto> sort(int SORT)
    {
        return null;
    }


    public MusicDto getMusicDto(String id)
    {
        Log.e("first", id);
        String[] projection = {
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.ALBUM_ID,
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
            musicDto.id = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media._ID));
            musicDto.album = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
            musicDto.albumid = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));
            musicDto.title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
            musicDto.artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
            Log.e("tag", musicDto.id + " , " + musicDto.title);
        }

        return musicDto;

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



                Bitmap b = BitmapFactory.decodeFileDescriptor(
                        fd.getFileDescriptor(), null, options);

                if (b != null) {
                    // finally rescale to exactly the size we need
                    if (options.outWidth != MAX_IMAGE_SIZE || options.outHeight != MAX_IMAGE_SIZE) {
                        Bitmap tmp = Bitmap.createScaledBitmap(b, MAX_IMAGE_SIZE, MAX_IMAGE_SIZE, true);
                        b.recycle();
                        b = tmp;
                    }
                }

                return b;
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
