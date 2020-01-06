package com.example.androidfinalproject;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

public class MyService extends Service {
    @Override


    public int onStartCommand(Intent intent, int flags, int startId)
    {

        new Thread(new Runnable() {
            @Override
            public void run()
            {

                while(true)
                {

                }
            }
        }).start();
        Log.d("debug","MyService onStartCommand()");

        //we have some options for service
        //start sticky means service will be explicity started and stopped
        return START_NOT_STICKY;
    }


    @Override
    public void onDestroy()
    {
        super.onDestroy();
        Log.d("debug","MyService onDestroy()");

    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
