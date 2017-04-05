package com.icaynia.pracler.remote;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.icaynia.pracler.data.RemoteDatabaseManager;
import com.icaynia.pracler.remote.listener.OnCompleteGetFirebaseAlbumListener;
import com.icaynia.pracler.remote.listener.OnCompleteGetFirebaseArtistListener;
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
        rdm.getSongsReference().child(song.artist).child(song.album).child(song.title).child("&info").addListenerForSingleValueEvent(new ValueEventListener()
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

    public static void getAlbum(PraclerSong song, final OnCompleteGetFirebaseAlbumListener listener)
    {
        RemoteDatabaseManager rdm = new RemoteDatabaseManager();
        rdm.getSongsReference().child(song.artist).child(song.album).child("&info").addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                AlbumRes albumRes = dataSnapshot.getValue(AlbumRes.class);
                listener.onComplete(albumRes);
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {
                Log.e(TAG, "getMusic method canceled.");
                return;
            }
        });
    }

    public static void getArtist(PraclerSong song, final OnCompleteGetFirebaseArtistListener listener)
    {
        RemoteDatabaseManager rdm = new RemoteDatabaseManager();
        rdm.getSongsReference().child(song.artist).child("&info").addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                ArtistRes artistRes = dataSnapshot.getValue(ArtistRes.class);
                listener.onComplete(artistRes);
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {

                Log.e(TAG, "getMusic method canceled.");
                return ;
            }
        });
    }

    public static void addNewSong(final MusicDto musicDto)
    {
        final PraclerSong praclerSong = new PraclerSong();
        praclerSong.album = musicDto.getAlbum();
        praclerSong.title = musicDto.getTitle();
        praclerSong.artist = musicDto.getArtist();

        getArtist(praclerSong, new OnCompleteGetFirebaseArtistListener()
        {
            @Override
            public void onComplete(ArtistRes artistRes)
            {
                // 새로운 아티스트
                // 이 경우 아무것도 없다는 것
                if (artistRes == null)
                {
                    addNewArtist(praclerSong);
                }
                else
                {
                    getAlbum(praclerSong, new OnCompleteGetFirebaseAlbumListener()
                    {
                        @Override
                        public void onComplete(AlbumRes albumRes)
                        {
                            if (albumRes == null)
                            {
                                addNewAlbum(praclerSong);
                            }
                            else
                            {
                                getMusic(praclerSong, new OnCompleteGetFirebaseMusicListener()
                                {
                                    @Override
                                    public void onComplete(MusicRes musicRes)
                                    {
                                        if (musicRes == null)
                                        {
                                            addNewMusic(praclerSong);

                                        }
                                    }
                                });
                            }
                        }
                    });
                }

            }
        });
    }

    private static void addNewArtist(PraclerSong praclerSong)
    {
        RemoteDatabaseManager rdm = new RemoteDatabaseManager();
        DatabaseReference ar = rdm.getSongsReference()
                .child(MusicDto.replaceForInput(praclerSong.artist));
        ar.child("&info").setValue(new ArtistRes());

        addNewAlbum(praclerSong);
    }

    private static void addNewAlbum(PraclerSong praclerSong)
    {
        RemoteDatabaseManager rdm = new RemoteDatabaseManager();
        DatabaseReference ar = rdm.getSongsReference()
                .child(MusicDto.replaceForInput(praclerSong.artist))
                .child(MusicDto.replaceForInput(praclerSong.album));
        ar.child("&info").setValue(new ArtistRes());
        addNewMusic(praclerSong);
    }

    private static void addNewMusic(PraclerSong praclerSong)
    {
        RemoteDatabaseManager rdm = new RemoteDatabaseManager();
        DatabaseReference ar = rdm.getSongsReference()
                .child(MusicDto.replaceForInput(praclerSong.artist))
                .child(MusicDto.replaceForInput(praclerSong.album))
                .child(MusicDto.replaceForInput(praclerSong.title));
        ar.child("&info").setValue(new MusicRes());
    }
}
