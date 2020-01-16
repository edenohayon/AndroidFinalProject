package com.example.androidfinalproject;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MyService extends Service {
    DatabaseReference database;

    fireBaseHandler fb = new fireBaseHandler();

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId)
    {
        Log.d("debug ----", "onStartCommand: ");
        new Thread(new Runnable() {
            @Override
            public void run()
            {

               //update fire base
                String name =  intent.getStringExtra("name");
                String id = intent.getStringExtra("id");
                Log.d("debug ---- ", "run: " + name+ " " +id);
                Panel p = new Panel(id,name);

                fb.updatePanel(id,p);



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
