package com.example.androidfinalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import gr.net.maroulis.library.EasySplashScreen;

import static android.os.SystemClock.sleep;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashscreen);


        //EasySplashScreen config;
        Thread thread = new Thread() {

            public void run() {


                        try {
                            sleep(2000);
                            finish();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        finally {
//                            if (!checkConnectivity()) {
//                                Toast.makeText(getBaseContext(), "שגיאה. אנא וודא כי קיים חיבור לרשת", Toast.LENGTH_LONG).show();
//                                Log.d("debug -------- ", "splash: ");
//                                finish();
//                                finishAffinity();
//                            } else
                                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                        }
                    }
                };

        thread.start();

    }

    @Override
    protected void onStart() {
        super.onStart();


    }


    private boolean checkConnectivity() {
        boolean hasWifi = false;
        boolean hasMobileData = false;
        ConnectivityManager connectivity = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info != null) {
                if (info.getType() == ConnectivityManager.TYPE_WIFI) {
                    if (info.isConnected())
                        hasWifi = true;
                }
                if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
                    if (info.isConnected())
                        hasMobileData = true;
                }
            }


        }

        return hasMobileData || hasWifi;
    }


}