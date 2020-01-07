package com.example.androidfinalproject;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.Toast;


import androidx.core.app.NotificationCompat;

import static androidx.core.content.ContextCompat.getSystemService;

public class MyReceiver extends BroadcastReceiver {

    private NotificationManager notificationManager;
    private static String CHANNEL_ID = "channel1";
    private static String CHANNEL_NAME = "Channel 1 Demo";
    private static int notificationId = 1;

    public static String ACTION_DATABASE_CHANGED = "com.example.androidfinalproject.DATABASE_CHANGED";

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "עודכן בהצלחה" , Toast.LENGTH_SHORT).show();
        //setupNotificationChannel();

    }


//    private void setupNotificationChannel()
//    {
//        // 1. Get reference to Notification Manager
//        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//
//        // 2. Create Notification Channel ONLY ONEs.
//        //    Need for Android 8.0 (API level 26) and higher.
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
//        {
//            //Toast.makeText(this, "Notification Channel created!", Toast.LENGTH_LONG).show();
//            //Create channel only if it is not already created
//            if (notificationManager.getNotificationChannel(CHANNEL_ID) == null)
//            {
//                NotificationChannel notificationChannel = new NotificationChannel(
//                        CHANNEL_ID,
//                        CHANNEL_NAME,
//                        NotificationManager.IMPORTANCE_DEFAULT); // NotificationManager.IMPORTANCE_HIGH
//
//                notificationManager.createNotificationChannel(notificationChannel);
//            }
//        }
//    }
//
//    // 3. create the Notification & send it to the device status bar
//    public void showNotification(String notificationTitle, String notificationText)
//    {
//        Intent intent = new Intent(this, MainActivity.class);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this.,0,intent,0);
//
//        // Build Notification with NotificationCompat.Builder
//        // on Build.VERSION < Oreo the notification avoid the CHANEL_ID
//        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
//                .setSmallIcon(R.drawable.ic_notifications)  //Set the icon
//                .setContentTitle(notificationTitle)         //Set the title of notification
//                .setContentText(notificationText)           //Set the text for notification
//                .setContentIntent(pendingIntent)            // Starts Intent when notification clicked
//                //.setOngoing(true)                         // stick notification
//                .setAutoCancel(true)                        // close notification when clicked
//                .build();
//
//        // Send the notification to the device Status bar.
//        notificationManager.notify(notificationId, notification);
//
//        notificationId++;  // for multiple(grouping) notifications on the same chanel
//    }
//
//    //public
}
