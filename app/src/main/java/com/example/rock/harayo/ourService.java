package com.example.rock.harayo;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by rock on 7/21/2019.
 */


public class ourService extends Service{
    Context context = this;
    SendJSON sendJSON;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        Toast.makeText(context, "Entered", Toast.LENGTH_LONG).show();
        Notify(context);
        sendJSON = new SendJSON(context);
        SendJSON.VolleyCallback callback = new SendJSON.VolleyCallback() {
            @Override
            public void onSuccess(Boolean success) {
                if(success){
                    ArrayList<HashMap> lists = sendJSON.getList();


                }
            }
        };
        sendJSON.getJSON(this, callback);
        super.onCreate();
    }

    private void Notify(Context context){
       // NotificationManager notificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        Intent notificationIntent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);

        /*Notification notification = new Notification();
        NotificationCompat.Builder builder;
        builder = new NotificationCompat.Builder(context);
        notification = builder.setContentIntent(pendingIntent)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setTicker("ticker").setWhen(System.currentTimeMillis())
                .setAutoCancel(true).setContentTitle("Good News")
                .setContentText("May be found").build();

        notificationManager.notify(1010, notification);*/

        Notification n = new Notification.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Found")
                .setContentText("Item")
                .setContentIntent(pendingIntent).setAutoCancel(true)
                .build();

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
// Hide the notification after it's selected

        notificationManager.notify(0, n);
    }
}
