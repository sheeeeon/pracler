package com.icaynia.pracler.remote;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.icaynia.pracler.data.RemoteDatabaseManager;
import com.icaynia.pracler.remote.listener.OnCompleteGetFirebaseMusicListener;
import com.icaynia.pracler.remote.models.AlbumRes;
import com.icaynia.pracler.models.ArtistRes;
import com.icaynia.pracler.models.MusicDto;
import com.icaynia.pracler.remote.models.MusicRes;
import com.icaynia.pracler.remote.models.PraclerSong;

/**
 * Created by icaynia on 29/03/2017.
 */

public class FirebaseSongManager
{
    public static String TAG = "FirebaseSongManager";
    public static void getMusic(PraclerSong song, final OnCompleteGetFirebaseMusicListener listener)
    {
        RemoteDatabaseManager rdm = new RemoteDatabaseManager();
        rdm.getSongsReference().child(song.artist).child(song.album).child(song.title).addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                MusicRes musicRes = dataSnapshot.getValue(MusicRes.class);
                listener.onComplete(musicRes);
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {
                Log.e(TAG, "getMusic method canceled.");
                return;
            }
        });
    }

    public static void addNewSong(final MusicDto musicDto)
    {
        PraclerSong praclerSong = new PraclerSong();
        praclerSong.album = musicDto.getAlbum();
        praclerSong.title = musicDto.getTitle();
        praclerSong.artist = musicDto.getArtist();

        getMusic(praclerSong, new OnCompleteGetFirebaseMusicListener()
        {
            @Override
            public void onComplete(MusicRes musicRes)
            {
                if (musicRes == null)
                {
                    MusicRes info = new MusicRes();
                    ArtistRes arres = new ArtistRes();
                    AlbumRes albumRes = new AlbumRes();

                    RemoteDatabaseManager rdm = new RemoteDatabaseManager();

                    DatabaseReference ar = rdm.getSongsReference()
                            .child(MusicDto.replaceForInput(musicDto.getArtist()));

                    DatabaseReference br = ar
                            .child(MusicDto.replaceForInput(musicDto.getAlbum()));

                    DatabaseReference dr = br
                            .child(MusicDto.replaceForInput(musicDto.getTitle()));

                    dr.child("&info").setValue(info);
                    ar.child("&info").setValue(arres);
                    br.child("&info").setValue(albumRes);
                }

            }
        });
    }
}
