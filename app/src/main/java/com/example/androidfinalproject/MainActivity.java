package com.example.androidfinalproject;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;
import static android.view.Gravity.CENTER;


/*
dynamic buttons with table layout - may help
https://www.youtube.com/watch?v=4MFzuP1F-xQ
 */

public class MainActivity extends AppCompatActivity {

    //public static final String EXTRA_MESSAGE = "com.example.androidfinalproject.MESSAGE";
    private final int NUM_COL = 3;
    private static final String PANEL_ID = "panelID";
    private static final String PANEL_NAME = "panelName";


    //layout virables
    private TableLayout tableLayout;
    private TableRow tableRow;
    private ImageButton add;
    private List<Button> items;
    private List<Panel> panel;
    private Context context;
    private int itemsAdded;

    // for popup
    private Dialog myDialog, editDialog;
    boolean isPopupWarningOn;

    private DatabaseReference database;

    //for broadcast receiver
    private MyReceiver mReceiver;
    private IntentFilter intentFilter;

    //private int listIndex = 0;
    //private ListView listViewPanels;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        items = new ArrayList<>();
        tableLayout = findViewById(R.id.tableLayout);

        itemsAdded = 0;

        add = findViewById(R.id.addBtn);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 showPopup();
            }
        });

        myDialog = new Dialog(this);
        editDialog = new Dialog(this);
        isPopupWarningOn = false;

        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        database = FirebaseDatabase.getInstance().getReference("panel"); //Dont pass any path if you want root of the tree

        context = this.getBaseContext();

        // create the Broadcast Receiver
        mReceiver = new MyReceiver();

        // Define the IntentFilter for battery change.
        intentFilter = new IntentFilter(MyReceiver.ACTION_DATABASE_CHANGED);

        // register the receiver with the filter
        registerReceiver(mReceiver, intentFilter);

    }

    @Override
    protected void onStart() {
        super.onStart();
        //attaching value event listener
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //clearing the previous items list and table
                items.clear();
                tableLayout.removeAllViews();

                //iterating through all the nodes
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                    final Panel item = postSnapshot.getValue(Panel.class);
                    final String itemName = item.getName();

                    Button btn = new Button(context);
                   // btn.setBackgroundColor(Color.rgb(141,216,141));
                    btn.setBackgroundResource(R.drawable.costum_button);


                    btn.setText(itemName);
                    btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showSelectPopup(v,item.getId());
                          // goToItem(v,item.getId());

                        }});

                    items.add(btn);
                    //todo - maybe revers items??
                    btn.setTag(item.getId());

                }
                populateTable();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });
    }

    private void populateTable() {
        Log.d("debug --------", "populateTable: in populat");
        tableLayout = findViewById(R.id.tableLayout);
        int row = 0, col = 0;
        while( row < items.size() ) {
            TableRow tableRow = new TableRow(this);
            tableRow.setLayoutParams(new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.MATCH_PARENT,
                    1.0f));
            tableLayout.addView(tableRow);
            col = 0;

            while(col < NUM_COL && row < items.size() )
            {

                items.get(row).setLayoutParams(new TableRow.LayoutParams(
                        TableRow.LayoutParams.WRAP_CONTENT,
                        TableRow.LayoutParams.WRAP_CONTENT,
                        1.0f));


                tableRow.addView(items.get(row));
                row++;
                col++;
            }
        }
    }

    private void showPopup() {
        TextView txtClose;

        myDialog.setContentView(R.layout.popup);
        txtClose = myDialog.findViewById(R.id.txtclose);
        txtClose.setText("X");
        txtClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });

        Button saveBtn;
        saveBtn = myDialog.findViewById(R.id.btnfollow);
     //   saveBtn.setBackgroundColor(Color.rgb(141,216,141));
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Inflate using dialog themed context. - mabe not neseccery
                final Context context = myDialog.getContext();
                final LayoutInflater inflater = LayoutInflater.from(context);
                final View view = inflater.inflate(R.layout.popup, null, false);

                final  EditText input = myDialog.findViewById(R.id.input);
                String inputVal = input.getText().toString();

                if(inputVal.isEmpty())
                {

                    if(!isPopupWarningOn) {
                        Log.d("debug", "onClick: input emty");
                        TextView warning = new TextView(myDialog.getContext());
                        warning.setTextColor(Color.RED);
                        warning.setText("הכנס שם מוצר");
                        warning.setGravity(CENTER);
                        warning.setLayoutParams(new LinearLayout.LayoutParams(
                                TableLayout.LayoutParams.MATCH_PARENT,
                                TableLayout.LayoutParams.MATCH_PARENT));
                        LinearLayout layout = myDialog.findViewById(R.id.linearLayout);
                        layout.addView(warning);
                    }
                    isPopupWarningOn = true;

                    return;
                }

                addNewItem(inputVal);
                myDialog.dismiss();
            }
        });
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();


    }

    private void addNewItem(final String inputVal) {

        Button button = new Button(this);

        button.setLayoutParams(new TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT));


        button.setText(inputVal);

        final String id = database.push().getKey();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToItem(v,id);
            }});

        Panel panel = new Panel(id, inputVal);
        button.setTag(id);

        //makes sure only NUM_COL items per row
        if(itemsAdded == 0)
        {
            TableRow tableRowTmp = new TableRow(this);
            tableRowTmp.setLayoutParams(new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.MATCH_PARENT));
            tableLayout.addView(tableRowTmp);
            tableRow = tableRowTmp;

        }
        tableRow.addView(button);
        itemsAdded++;
        items.add(button);

        if(itemsAdded == NUM_COL)
            itemsAdded = 0;

        //addItem to server
        database.child(id).setValue(panel);
        //send broadcast that item added seccefully
        context.sendBroadcast(new Intent(MyReceiver.ACTION_DATABASE_CHANGED));

    }

    public void goToItem(View view, String id)
    {

       // showSelectPopup();

        String panelID = view.getTag().toString();
        String panelName = ((Button)view).getText().toString();
        Log.d("debug", "goToItem: " + panelName);

        Intent intent = new Intent(getApplicationContext(), ItemActivity.class);

        intent.putExtra(PANEL_ID, panelID);
        intent.putExtra(PANEL_NAME, panelName);

        startActivity(intent);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuItem menuAbout = menu.add("About");
        MenuItem menuExit = menu.add("Exit");

        menuAbout.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Log.d("debug", "onMenuItemClick: " + item);
                showAboutDialog();
                return true;
            }
        });

        menuExit.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Log.d("debug", "onMenuItemClick: " + item);
                System.exit(0);
                return true;
            }
        });
        return true;
    }

    private void showAboutDialog()
    {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("About");
        alertDialog.setMessage("Developed by\n\n Eden Ohayon and Natalie Eisenstadt (c)");
        alertDialog.show();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();

        // unregister the receiver
        unregisterReceiver(mReceiver);
    }

    private void showSelectPopup(final View view, final String id) {

        editDialog.setContentView(R.layout.selectpopup);
        TextView txtClose;
        txtClose = editDialog.findViewById(R.id.txtclose1);
        txtClose.setText("X");
        txtClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editDialog.dismiss();
            }
        });

        //edit button
        Button editItem = editDialog.findViewById(R.id.edit);
        editItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToItem(view,id);

            }
        });
        editDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        editDialog.show();
//        //reduce button
//        Button reduce = popupDialog.findViewById(R.id.reduce);
//        reduce.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                TextView num = popupDialog.findViewById(R.id.length);
//
//                int editedNum = Integer.parseInt(num.getText().toString())-1;
//                if(editedNum != -1)
//                    num.setText(editedNum+"");
//            }
//        });



    }

}
