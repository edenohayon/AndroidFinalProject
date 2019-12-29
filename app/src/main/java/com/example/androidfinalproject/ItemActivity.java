package com.example.androidfinalproject;

import androidx.appcompat.app.AppCompatActivity;
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
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import static android.view.Gravity.CENTER;

public class ItemActivity extends AppCompatActivity {

    final int COL_NUM  = 4;
    //until we get server
    Item i = new Item(1, "panel", 1.2, 2);
    Item[] myItems = new Item[]{i,new Item(1, "panel", 1.3, 2)
            ,new Item(1, "panel", 1.4, 2),
            new Item(1, "panel", 1.5, 2)};

    private Dialog myDialog;
    private TableLayout table;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

        // Capture the layout's TextView and set the string as its text
        TextView textView = findViewById(R.id.textView);
        textView.setText(message);

        populateTable();

        myDialog = new Dialog(this);
    }

    private void populateTable() {
        table = findViewById(R.id.tableID);

        for (int row = 0; row != myItems.length; row++) {
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

        myDialog.setContentView(R.layout.itempopup);

        //close dialog
        TextView txtClose;
        txtClose = myDialog.findViewById(R.id.txtclose);
        txtClose.setText("X");
        txtClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });

        //save changes
        Button save;
        save = myDialog.findViewById(R.id.btnfollow);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView num = myDialog.findViewById(R.id.number);
                int newAmount = Integer.parseInt(num.getText().toString());
                //todo - save to server
                myItems[row].setAmount(newAmount);
                //todo - try find better solution
                table.removeAllViews();
                populateTable();

                myDialog.dismiss();
            }
        });

        //display number
        TextView num = myDialog.findViewById(R.id.number);
        String txt = myItems[row].getAmount()+ "";
        num.setText(txt);

        //add button
        Button add = myDialog.findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView num = myDialog.findViewById(R.id.number);

                int editedNum = Integer.parseInt(num.getText().toString())+1;
                num.setText(editedNum+"");
            }
        });

        //reduce button
        Button reduce = myDialog.findViewById(R.id.reduce);
        reduce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView num = myDialog.findViewById(R.id.number);

                int editedNum = Integer.parseInt(num.getText().toString())-1;
                if(editedNum != -1)
                    num.setText(editedNum+"");
            }
        });

        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();

    }

    private String getColInfo(int row, int col) {
        if(col == 0)
            return "" + myItems[row].getId();

        if(col == 1)
            return myItems[row].getName();

        if(col == 2)
            return "" + myItems[row].getLength();

        if(col == 3)
            return ""+ myItems[row].getAmount();
        return "";

    }
}
