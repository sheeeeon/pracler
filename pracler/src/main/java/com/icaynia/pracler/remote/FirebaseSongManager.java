package com.icaynia.pracler.remote;

import com.google.firebase.database.DatabaseReference;
import com.icaynia.pracler.data.RemoteDatabaseManager;
import com.icaynia.pracler.models.AlbumRes;
import com.icaynia.pracler.models.ArtistRes;
import com.icaynia.pracler.models.MusicDto;
import com.icaynia.pracler.models.MusicRes;

/**
 * Created by icaynia on 29/03/2017.
 */

public class FirebaseSongManager
{
    public static void addNewSong(MusicDto musicDto)
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
