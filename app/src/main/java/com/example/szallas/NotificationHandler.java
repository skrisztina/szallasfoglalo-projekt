package com.example.szallas;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

public class NotificationHandler {
    private static final String CHANNEL_ID = "szallasfoglalo_channel";
    private final int NOTIF_ID = 0;
    private Context context;
    private NotificationManager manager;
    public NotificationHandler(Context context) {
        this.context = context;
        this.manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        createChannel();
    }

    private void createChannel(){
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.O){
            return;
        }else {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "SzallasFoglalo", NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription("Értesítés a SzállásFoglaló-tól.");
            manager.createNotificationChannel(channel);
        }
    }

    public void send(String message){
        Intent intent = new Intent(context, Szallasok.class);
        PendingIntent pendint = PendingIntent.getActivity(context, NOTIF_ID, intent, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentTitle("SzallasFoglalo").setContentText(message)
                .setSmallIcon(R.drawable.foglalasok_icon).setPriority(NotificationCompat.PRIORITY_HIGH).setContentIntent(pendint);

        manager.notify(NOTIF_ID, builder.build());
    }
}
