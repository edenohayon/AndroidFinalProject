package com.example.androidfinalproject;

import android.app.Service;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class fireBaseHandler extends Service {

    private DatabaseReference itemRef;
    private DatabaseReference lenRef;

    private List<Panel> panels;
    //   private List<ClipData.Item>

    fireBaseHandler() {
        itemRef = FirebaseDatabase.getInstance().getReference("panel");
        lenRef = FirebaseDatabase.getInstance().getReference("lengths");
        panels = new ArrayList<>();

        panels.clear();
        //  tableLayout.removeAllViews();

        //iterating through all the nodes

        itemRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                new Thread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        Log.d("debug ----- ", "FBFBFBFBFBFBFBFBFBFB " + panels);

                        //clearing the previous items list and table
                        panels.clear();
                        //  tableLayout.removeAllViews();

                        //iterating through all the nodes
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                            final Panel panel = postSnapshot.getValue(Panel.class);
                            final String itemName = panel.getName();
                            panels.add(panel);

                        }
                        Log.d("debug --------------", "onDataChange: " + panels);
                    }


                    @Nullable
                    public IBinder onBind(Intent intent) {
                        return null;
                    }


                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    public void onCreate() {
        super.onCreate();
        itemRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //clearing the previous items list and table
                panels.clear();
                //  tableLayout.removeAllViews();

                //iterating through all the nodes
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                    final Panel panel = postSnapshot.getValue(Panel.class);
                    final String itemName = panel.getName();
                    panels.add(panel);

                }
                Log.d("debug --------------", "onDataChange: " + panels);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }

            @Nullable
            public IBinder onBind(Intent intent) {
                return null;
            }


        });
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    public List<Panel> getPanels() {
        return panels;
    }

    public void updatePanel(String id, Panel panel) {
        itemRef.child(id).setValue(panel);

    }
}

