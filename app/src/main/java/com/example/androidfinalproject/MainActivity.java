package com.example.androidfinalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
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

public class MainActivity extends AppCompatActivity {

    private TableLayout tableLayout;
    private TableRow tableRow;
    private ImageButton add;
    private ArrayList<Button> items;
    private TableRow.LayoutParams lp;

    private int itemsAdded;

    // for popup
    Dialog myDialog;
    boolean isPopupWarningOn;

    private final int NUM_COL = 3;
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

    }

    private void showPopup() {
        TextView txtclose;
        Button btnFollow;
        myDialog.setContentView(R.layout.popup);
        txtclose =(TextView) myDialog.findViewById(R.id.txtclose);
        txtclose.setText("M");
        btnFollow = (Button) myDialog.findViewById(R.id.btnfollow);

        txtclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });

        btnFollow.setOnClickListener(new View.OnClickListener() {
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

        items.add(button);

        // need to create new row
        if(itemsAdded == 0)
        {
            TableRow tableRowTmp = new TableRow(this);
            tableRowTmp.setLayoutParams(new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.MATCH_PARENT));
            tableLayout.addView(tableRowTmp);
            tableRow = tableRowTmp;

        }

        button.setLayoutParams(new TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT));
        button.setText(inputVal);

        tableRow.addView(button);
        itemsAdded++;
        if(itemsAdded == NUM_COL)
            itemsAdded = 0;

    }



}
