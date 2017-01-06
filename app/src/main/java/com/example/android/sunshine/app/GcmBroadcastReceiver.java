package com.example.android.sunshine.app;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.google.android.gms.gcm.GoogleCloudMessaging;

public class GcmBroadcastReceiver extends BroadcastReceiver {

    private static final String WEATHER = "weather";
    private static final String LOCATION = "location";
    private static final String WEATHER_ALERT = "Weather Alert!";
    public static final int NOTIFICATION_ID = 1;

    private NotificationManager notiManager;

    public GcmBroadcastReceiver() {
        super();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(context);
        String messageType = gcm.getMessageType(intent);

        if (!extras.isEmpty()) {
            //VALIDATION
            if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {
                String weather = extras.getString(WEATHER);
                String location = extras.getString(LOCATION);
                String alert = "Heads up: " + weather + " in " + location + "!";
                sendNotification(context, alert);
            }
        }
    }

    // Put the message into a notification and post it.
    private void sendNotification(Context context, String msg) {
        notiManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent contentIntent =
                PendingIntent.getActivity(context, 0, new Intent(context, MainActivity.class), 0);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.art_storm)
                        .setContentTitle(WEATHER_ALERT)
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(msg))
                        .setContentText(msg)
                        .setPriority(NotificationCompat.PRIORITY_HIGH);

        mBuilder.setContentIntent(contentIntent);
        notiManager.notify(NOTIFICATION_ID, mBuilder.build());
    }
}
