package com.example.androidfinalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static android.view.Gravity.CENTER;


/*
dynamic buttons with table layout - may help
https://www.youtube.com/watch?v=4MFzuP1F-xQ
 */

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_MESSAGE = "com.example.androidfinalproject.MESSAGE";
    private static final int COL_NUM = 3;


    private TableLayout tableLayout;
    private TableRow tableRow;
    private ImageButton add;
    private ArrayList<Button> items;
  //  private TableRow.LayoutParams lp;
    private Context context;

    private int itemsAdded;

    // for popup
    private Dialog myDialog;
    boolean isPopupWarningOn;

    private final int NUM_COL = 3;

    private DatabaseReference database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //currently holding only added items
        items = new ArrayList<Button>();
        tableLayout = findViewById(R.id.tableLayout);
       // tableRow = findViewById(R.id.row0);
       // lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, 1.0f);
        itemsAdded = 0;

        add = findViewById(R.id.addBtn);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 showPopup();
            }
        });

        myDialog = new Dialog(this);
        isPopupWarningOn = false;

        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        database = FirebaseDatabase.getInstance().getReference("panels"); //Dont pass any path if you want root of the tree

        context = this.getBaseContext();


    }

    @Override
    protected void onStart() {
        super.onStart();
        //attaching value event listener
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //clearing the previous artist list
                items.clear();
                tableLayout.removeAllViews();

                //iterating through all the nodes
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    //getting artist
                    Log.d("debug --------", "onDataChange: "+ postSnapshot.getValue());

                    Item item = postSnapshot.getValue(Item.class);

                    Button btn = new Button(context);
                    btn.setText(item.getName());
                    Log.d("debugA", "onDataChange: " +item.getLengths().get(0));
                    btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            goToItem(v);
                        }});
                    //adding artist to the list
                    items.add(btn);
                    //mayby revers items??


                }
                populateTable();
                //creating adapter

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
                //buttons[row][col] = btn;
            }
        }
    }

    private void showPopup() {
        TextView txtclose;

        Button saveBtn;
        myDialog.setContentView(R.layout.popup);
        txtclose = myDialog.findViewById(R.id.txtclose);
        txtclose.setText("X");
        saveBtn = myDialog.findViewById(R.id.btnfollow);

        txtclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });

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

    private void addNewItem(String inputVal) {

        Button button = new Button(this);



        button.setLayoutParams(new TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT));
        button.setText(inputVal);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToItem(v);
            }});

        String id = database.push().getKey();
        button.setTag(id);

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
        database.child(id).setValue(inputVal);

    }

    public void goToItem(View view)
    {
        Intent intent = new Intent(this, ItemActivity.class);

        String id = view.getTag().toString();
        Log.d("debug", "goToItem: " + id);


        intent.putExtra(EXTRA_MESSAGE, "panels/"+id);
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
        //alertDialog.setIcon(R.drawable.document);
        alertDialog.setTitle("About");
        alertDialog.setMessage("Developed by\n\n Eden Ohayon and Natalie *complete in main activity* (c)");
        alertDialog.show();
    }


}
