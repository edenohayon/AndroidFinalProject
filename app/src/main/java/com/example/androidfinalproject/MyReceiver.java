package com.example.androidfinalproject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;


public class MyReceiver extends BroadcastReceiver {

    public static String ACTION_DATABASE_CHANGED = "com.example.androidfinalproject.DATABASE_CHANGED";

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "עודכן בהצלחה" , Toast.LENGTH_SHORT).show();
        //setupNotificationChannel();

    }

}
