package com.icaynia.pracler.notifications;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.NotificationCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.icaynia.pracler.Global;
import com.icaynia.pracler.R;
import com.icaynia.pracler.activities.PlayerActivity;
import com.icaynia.pracler.models.User;
import com.icaynia.pracler.remote.FirebaseAlertManager;
import com.icaynia.pracler.remote.FirebaseUserManager;
import com.icaynia.pracler.remote.listener.OnCompleteGetFirebaseUserListener;
import com.icaynia.pracler.remote.models.PraclerAlert;

/**
 * Created by icaynia on 30/03/2017.
 */

public class UserNotification
{
    public static void build(final Context context, final PraclerAlert alert)
    {
        Intent notificationIntent = new Intent(context, PlayerActivity.class);
        notificationIntent.putExtra("notificationId", 3333);
        final PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        final NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        //Bitmap albumArt = mMusicManager.getAlbumImage(getApplicationContext(), Integer.parseInt(musicDto.getAlbumId()), 100);
        FirebaseUserManager.getUser(alert.user_uid, new OnCompleteGetFirebaseUserListener()
        {
            @Override
            public void onComplete(User user)
            {
                builder.setContentTitle(alert.messages)
                        .setContentText(alert.messages)
                        .setTicker(alert.messages)
                        .setSmallIcon(R.drawable.ic_headset_white)
                        .setContentIntent(contentIntent)
                        .setAutoCancel(false)
                        .setWhen(System.currentTimeMillis());



                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    builder.setCategory(Notification.CATEGORY_MESSAGE)
                            .setPriority(Notification.PRIORITY_HIGH)
                            .setVisibility(Notification.VISIBILITY_PUBLIC);
                }

                NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                nm.notify(3333, builder.build());
            }
        });

        Global global = (Global) context.getApplicationContext();
        FirebaseAlertManager.deleteAlert(global.loginUser.getUid());
    }

}
