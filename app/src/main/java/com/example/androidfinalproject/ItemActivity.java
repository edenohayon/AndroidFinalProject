package com.example.androidfinalproject;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static android.view.Gravity.CENTER;

/*
 * https://www.simplifiedcoding.net/firebase-realtime-database-crud/#Firebase-Realtime-Database-Video-Tutorial
 * all server method explanation are from here*/

public class ItemActivity extends AppCompatActivity {

    private static final String PANEL_ID = "panelID";
    private static final String PANEL_NAME = "panelName";

    private static String CHANNEL_ID = "channel1";
    private static String CHANNEL_NAME = "Channel 1 Demo";
    private static int notificationId = 1;

    private final int COL_NUM = 2;


    private List<Lengths> myItems;

    //popup
    private Dialog popupDialog, addItemDialog;
    private boolean isPopupWarningOn;

    //layout virables
    private TableLayout table;
    private ImageButton addItem;

    //item info
    private double newLen;
    private int newAmount;
    private String panelId;
    private String itemName;

    private NotificationManager notificationManager;

    private DatabaseReference database;

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        panelId = intent.getStringExtra(PANEL_ID);
        itemName = intent.getStringExtra(PANEL_NAME);

        database = FirebaseDatabase.getInstance().getReference("lengths").child(panelId);


        // Capture the layout's TextView and set the string as its text
        TextView textView = findViewById(R.id.textView);
        textView.setText(itemName);

        myItems = new ArrayList<>();

        //for popup
        popupDialog = new Dialog(this);
        addItemDialog = new Dialog(this);
        isPopupWarningOn = false;


        // my_child_toolbar is defined in the layout file
        Toolbar myChildToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myChildToolbar);

        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);

        addItem = findViewById(R.id.addItem);
        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddItemPopup(v);
            }
        });

        this.context = this.getBaseContext();
        table = findViewById(R.id.tableID);

        // ScrollView scrollview = (ScrollView) findViewById(R.id.ScrollView03);

        setupNotificationChannel();
        // populateTable();
    }

    @Override
    protected void onStart() {
        super.onStart();

        database.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                myItems.clear();
                table.removeAllViews();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Lengths len = postSnapshot.getValue(Lengths.class);
                    myItems.add(len);
                }
                if(!myItems.isEmpty()) {
                    myItems.sort(new Comparator<Lengths>() {
                        @Override
                        public int compare(Lengths o1, Lengths o2) {
                            return (int)(o1.getLength() - o2.getLength());
                        }
                    } );

                    populateTable();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }



    private void populateTable() {

        for (int row = 0; row != myItems.size(); row++) {
            TableRow tableRow = new TableRow(this);

            TableLayout.LayoutParams lp =
                    new TableLayout.LayoutParams(0,
                            TableLayout.LayoutParams.MATCH_PARENT, 1);

            lp.setMargins(10, 5, 10, 5);
            tableRow.setLayoutParams(lp);

            table.addView(tableRow, lp);

            for (int col = 0; col != COL_NUM; col++) {
                final int FINAL_COL = col;
                final int FINAL_ROW = row;

                final TextView itemInfo = new TextView(this);

                TableRow.LayoutParams lpr =
                        new TableRow.LayoutParams(1,
                                TableRow.LayoutParams.MATCH_PARENT, 1);

                lpr.setMargins(5, 5, 5, 5);
                itemInfo.setLayoutParams(lpr);

                String info = getColInfo(row, col);
                itemInfo.setText(info);
                itemInfo.setTextSize(20f);
                itemInfo.setBackgroundResource(R.drawable.costume_item_list);
                itemInfo.setTextColor(getResources().getColor(R.color.primary));
                itemInfo.setGravity(CENTER);

                // Make text not clip on small buttons
                itemInfo.setPadding(0, 0, 0, 0);

                final int finalRow = row;
                itemInfo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showUpdatePopup(finalRow, myItems.get(FINAL_ROW).getId(), myItems.get(FINAL_ROW).getLength());
                    }
                });


                tableRow.addView(itemInfo, lpr);
            }
        }
    }

    private void showUpdatePopup(final int row, final String id, final double length) {

        popupDialog.setContentView(R.layout.itempopup);

        //close dialog
        TextView txtClose;
        txtClose = popupDialog.findViewById(R.id.txtclose);
        txtClose.setText("X");
        txtClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupDialog.dismiss();
            }
        });

        //save changes
        Button save;
        save = popupDialog.findViewById(R.id.btnfollow);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView num = popupDialog.findViewById(R.id.length);
                int newAmount = Integer.parseInt(num.getText().toString());

                if (newAmount == 0) {
                    Log.d("debug", "onClick: amount = 0 ");

                    //todo - build method that returns the messege to be sent by item
                    // HashMap<Lengths, Boolean> h = myItems.
                    showNotification("ניהול מלאי", itemName + " " + myItems.get(row).getLength() + " אזל במלאי");

                }

                database.child(id).setValue(new Lengths(length, newAmount, id));
                popupDialog.dismiss();
            }
        });

        //display number
        TextView num = popupDialog.findViewById(R.id.length);
        String txt = "" + myItems.get(row).getAmount();
        num.setText(txt);

        //add button
        Button add = popupDialog.findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView num = popupDialog.findViewById(R.id.length);

                int editedNum = Integer.parseInt(num.getText().toString()) + 1;
                num.setText(editedNum + "");
            }
        });

        //reduce button
        Button reduce = popupDialog.findViewById(R.id.reduce);
        reduce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView num = popupDialog.findViewById(R.id.length);

                int editedNum = Integer.parseInt(num.getText().toString()) - 1;
                if (editedNum != -1)
                    num.setText(editedNum + "");
            }
        });

        Button delete = popupDialog.findViewById(R.id.btnDelete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                database.child(id).removeValue();
                popupDialog.dismiss();
            }
        });

        popupDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popupDialog.show();

    }

    private void setupNotificationChannel() {
        // 1. Get reference to Notification Manager
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        // 2. Create Notification Channel ONLY ONEs.
        //    Need for Android 8.0 (API level 26) and higher.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //Toast.makeText(this, "Notification Channel created!", Toast.LENGTH_LONG).show();
            //Create channel only if it is not already created
            if (notificationManager.getNotificationChannel(CHANNEL_ID) == null) {
                NotificationChannel notificationChannel = new NotificationChannel(
                        CHANNEL_ID,
                        CHANNEL_NAME,
                        NotificationManager.IMPORTANCE_DEFAULT); // NotificationManager.IMPORTANCE_HIGH

                notificationManager.createNotificationChannel(notificationChannel);
            }
        }
    }

    public void showNotification(String notificationTitle, String notificationText) {
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        // Build Notification with NotificationCompat.Builder
        // on Build.VERSION < Oreo the notification avoid the CHANEL_ID
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notifications)  //Set the icon
                .setContentTitle(notificationTitle)         //Set the title of notification
                .setContentText(notificationText)           //Set the text for notification
                .setContentIntent(pendingIntent)            // Starts Intent when notification clicked
                //.setOngoing(true)                         // stick notification
                .setAutoCancel(true)                        // close notification when clicked
                .build();

        // Send the notification to the device Status bar.
        notificationManager.notify(notificationId, notification);

        notificationId++;  // for multiple(grouping) notifications on the same chanel
    }


    private void showAddItemPopup(View view) {

        addItemDialog.setContentView(R.layout.add_item);

        TextView txtclose;
        txtclose = addItemDialog.findViewById(R.id.txtclose);
        txtclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addItemDialog.dismiss();
            }
        });

        Button save;
        save = addItemDialog.findViewById(R.id.btnfollow);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Inflate using dialog themed context. - mabe not neseccery
                final Context context = addItemDialog.getContext();
                final LayoutInflater inflater = LayoutInflater.from(context);
                final View view = inflater.inflate(R.layout.add_item, null, false);

                final EditText lengthInput = addItemDialog.findViewById(R.id.lengthID);
                String length = lengthInput.getText().toString();

                final EditText amountInput = addItemDialog.findViewById(R.id.amountID);
                String amount = amountInput.getText().toString();

                if (length.isEmpty() || amount.isEmpty()) {

                    if (!isPopupWarningOn) {
                        Log.d("debug", "onClick: input emty");
                        TextView warning = new TextView(addItemDialog.getContext());
                        warning.setTextColor(Color.RED);
                        warning.setText("הכנס אורך וכמות");
                        warning.setGravity(CENTER);
                        warning.setLayoutParams(new LinearLayout.LayoutParams(
                                TableLayout.LayoutParams.MATCH_PARENT,
                                TableLayout.LayoutParams.MATCH_PARENT));
                        LinearLayout layout = addItemDialog.findViewById(R.id.addWarningLayout);
                        layout.addView(warning);
                    }
                    isPopupWarningOn = true;

                    return;
                }

                newAmount = Integer.parseInt(amount);
                newLen = Double.parseDouble(length);
                String newID = database.push().getKey();
                Lengths l = new Lengths(newLen, newAmount, newID);
                database.child(newID).setValue(l);

                addItemDialog.dismiss();
                // table.addView();
            }
        });
        addItemDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        addItemDialog.show();

        //populateTable();
    }

    private String getColInfo(int row, int col) {
        if (col == 0)
            return "" + myItems.get(row).getLength();

        if (col == 1)
            return "" + myItems.get(row).getAmount();

        return "";

    }


//    @RequiresApi(api = Build.VERSION_CODES.N)
//    public boolean containsName(final List<Lengths> list, final Double len){
//        return list.stream().filter(num-> (num.getLength()) == (len)).findFirst().isPresent();
//    }
}
