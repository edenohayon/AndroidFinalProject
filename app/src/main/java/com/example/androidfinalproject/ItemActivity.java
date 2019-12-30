package com.example.androidfinalproject;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;

import static android.view.Gravity.CENTER;

public class ItemActivity extends AppCompatActivity {

    final int COL_NUM  = 4;

    //until we get server
    Item i = new Item(1, "panel", 1.2, 2);
    private ArrayList<Item> myItems;

    private Dialog popupDialog, addItemDialog;
    private TableLayout table;
    private boolean isPopupWarningOn;

    private double newLen;
    private int newAmount;



    private ImageButton addItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        String id = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

        // Capture the layout's TextView and set the string as its text
        TextView textView = findViewById(R.id.textView);
        textView.setText(id);

        myItems = new ArrayList<>();
        myItems = getItemsByID(id);

        populateTable();

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


    }

    private ArrayList<Item> getItemsByID(String id) {
        //todo - get data from server
        ArrayList<Item> ret = new ArrayList<>();
        ret.add(new Item(1, "panel", 1.3, 2));
        ret.add(new Item(1, "panel", 1.4, 2));
        ret.add(new Item(1, "panel", 1.5, 2));
        ret.add(new Item(1, "panel", 1.6, 2));
        ret.add(new Item(1, "panel", 1.7, 2));
        ret.add(new Item(1, "panel", 1.8, 2));
        ret.add(new Item(1, "panel", 1.9, 2));
        ret.add(new Item(1, "panel", 1.0, 2));
        ret.add(new Item(1, "panel", 1.1, 2));
        ret.add(new Item(1, "panel", 1.2, 2));
        ret.add(new Item(1, "panel", 1.3, 2));

        return ret;

    }

    private void populateTable() {
        table = findViewById(R.id.tableID);

        for (int row = 0; row != myItems.size(); row++) {
            TableRow tableRow = new TableRow(this);
            tableRow.setLayoutParams(new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.MATCH_PARENT,
                    1.0f));
            table.addView(tableRow);

            for (int col = 0; col != COL_NUM; col++){
                final int FINAL_COL = col;
                final int FINAL_ROW = row;

                final TextView itemInfo = new TextView(this);

                itemInfo.setLayoutParams(new TableRow.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.MATCH_PARENT,
                        1.0f));

                String info = getColInfo(row, col);
                itemInfo.setText(info);
                itemInfo.setBackgroundResource(R.drawable.cell_shape);
                itemInfo.setTextColor(Color.BLACK);
                itemInfo.setGravity(CENTER);

                // Make text not clip on small buttons
                itemInfo.setPadding(0, 0, 0, 0);

             //   itemInfo.setBackgroundColor(getColBGColor(col));

                itemInfo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //todo - edit text
                        Log.d("debug", "onClick: click");
                    }
                });

                //todo - change to final
                if(col == 3) {
                    final int finalRow = row;
                    itemInfo.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showPopup(finalRow);
                        }
                    });
                }

                tableRow.addView(itemInfo);
                //buttons[row][col] = itemInfo;
            }
        }
    }

    private int getColBGColor(int col) {
        if(col == 0)
            return ContextCompat.getColor(this,R.color.col0);
        if(col == 1)
            return ContextCompat.getColor(this,R.color.col1);
        if(col == 2)
            return ContextCompat.getColor(this,R.color.col2);
        if(col == 3)
            return ContextCompat.getColor(this,R.color.col3);
        return 0;
    }

    private void showPopup(final int row) {

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
                //todo - save to server
                myItems.get(row).setAmount(newAmount);
                //todo - try find better solution
                table.removeAllViews();
                populateTable();

                popupDialog.dismiss();
            }
        });

        //display number
        TextView num = popupDialog.findViewById(R.id.length);
        String txt = myItems.get(row).getAmount()+ "";
        num.setText(txt);

        //add button
        Button add = popupDialog.findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView num = popupDialog.findViewById(R.id.length);

                int editedNum = Integer.parseInt(num.getText().toString())+1;
                num.setText(editedNum+"");
            }
        });

        //reduce button
        Button reduce = popupDialog.findViewById(R.id.reduce);
        reduce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView num = popupDialog.findViewById(R.id.length);

                int editedNum = Integer.parseInt(num.getText().toString())-1;
                if(editedNum != -1)
                    num.setText(editedNum+"");
            }
        });

        popupDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popupDialog.show();

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

                if(length.isEmpty() || amount.isEmpty())
                {

                    if(!isPopupWarningOn) {
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
                myItems.add( new Item(1,"panel", newLen, newAmount));
                //todo - find better solution
                table.removeAllViews();
                populateTable();


                addItemDialog.dismiss();
            }
        });
        addItemDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        addItemDialog.show();
    }

    private String getColInfo(int row, int col) {
        if(col == 0)
            return "" + myItems.get(row).getId();

        if(col == 1)
            return myItems.get(row).getName();

        if(col == 2)
            return "" + myItems.get(row).getLength();

        if(col == 3)
            return ""+ myItems.get(row).getAmount();
        return "";

    }
}
