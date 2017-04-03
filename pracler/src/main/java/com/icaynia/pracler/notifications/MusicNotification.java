package com.icaynia.pracler.notifications;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.NotificationCompat;

import com.icaynia.pracler.data.MusicFileManager;
import com.icaynia.pracler.R;
import com.icaynia.pracler.activities.PlayerActivity;
import com.icaynia.pracler.models.MusicDto;

/**
 * Created by icaynia on 30/03/2017.
 */

public class MusicNotification
{
    public static void update(Context context, int nowPlayingSongId)
    {
        Intent notificationIntent = new Intent(context, PlayerActivity.class);
        notificationIntent.putExtra("notificationId", 9999); //전달할 값
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        int songId = nowPlayingSongId;

        MusicFileManager mMusicManager = new MusicFileManager(context);
        MusicDto musicDto = mMusicManager.getMusicDto(songId+"");
        Bitmap albumArt = mMusicManager.getAlbumImage(context, Integer.parseInt(musicDto.getAlbumId()), 100);

        builder.setContentTitle("현재 기록 중")
                .setContentText(musicDto.getArtist() + " - " + musicDto.getTitle() + " ")
                .setTicker(musicDto.getArtist() + " - " + musicDto.getTitle())
                .setSmallIcon(R.drawable.ic_headset_white)
                .setLargeIcon(albumArt)
                .setContentIntent(contentIntent)
                .setAutoCancel(false)
                .setWhen(System.currentTimeMillis())
                .setDefaults(Notification.DEFAULT_LIGHTS);



        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            builder.setCategory(Notification.CATEGORY_MESSAGE)
                    .setPriority(Notification.PRIORITY_HIGH)
                    .setVisibility(Notification.VISIBILITY_PUBLIC);
        }

        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify(1234, builder.build());
    }
}
