package tce.education.koodalnraghavan.service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import tce.education.koodalnraghavan.MainActivity;
import tce.education.koodalnraghavan.R;

public class NotificationPush extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);


        if (remoteMessage.getData().size() >0)
        {
            showNotification(remoteMessage.getData().get("title"),remoteMessage.getData().get("content"));
        }
        if (remoteMessage.getNotification() != null){
            showNotification(remoteMessage.getNotification().getTitle(),remoteMessage.getNotification().getBody());
        }
    }

    public RemoteViews getCustomDesign(String title,String content)
    {
        RemoteViews remoteViews = new RemoteViews(getApplicationContext().getPackageName(), R.layout.notification_layout);

        remoteViews.setTextViewText(R.id.title,title);
        remoteViews.setTextViewText(R.id.content,content);

        return remoteViews;
    }

    public void showNotification(String title,String content)
    {
        Intent intent = new Intent(this, MainActivity.class);
        String channel = "Trial";

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_ONE_SHOT);
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(),channel)
                .setSmallIcon(R.drawable.ic_message_bl)
                .setSound(uri)
                .setAutoCancel(true)
                .setVibrate(new long[] {1000,1000,1000,1000,1000})
                .setOnlyAlertOnce(true)
                .setContentIntent(pendingIntent);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
        {
            builder = builder.setContent(getCustomDesign(title,content));
        }
        else {
            builder = builder.setContentTitle(title)
                    .setContentText(content)
                    .setSmallIcon(R.drawable.ic_message_bl);
            //builder = builder.setContent(getCustomDesign(title,content));
        }

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            NotificationChannel notificationChannel = new NotificationChannel(channel,"Name",NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.setSound(uri,null);

            manager.createNotificationChannel(notificationChannel);
        }

        manager.notify(0,builder.build());
    }
}
