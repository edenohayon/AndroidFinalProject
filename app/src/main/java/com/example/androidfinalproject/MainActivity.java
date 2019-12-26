package com.example.androidfinalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Icon;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private TableLayout tableLayout;
    private TableRow tableRow;
    private ImageButton add;
    private ArrayList<Button> items;
    private TableRow.LayoutParams lp;

    private int numInRow;

    private final int NUM_COL = 3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        items = new ArrayList<Button>();
        tableLayout = findViewById(R.id.tableLayout);
        tableRow = findViewById(R.id.row0);
        lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, 1.0f);
        numInRow = 0;

        add = findViewById(R.id.addBtn);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 addNewItem();
            }
        });

        //this is my edit

    }

    private void addNewItem() {

        Button tmp = new Button(this);

        //image
        tmp.setBackgroundResource(R.drawable.download);

        //set image size and style
        tmp.setLayoutParams(new TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT,
                1.0f));

        lp.height=150;
        lp.width = 150;
        tmp.setPadding(100,110,1,1);

        tmp.setLayoutParams(lp);
        items.add(tmp);

        //max items per row
        if(numInRow == NUM_COL)
        {
            TableRow tableRowTmp = new TableRow(this);
            tableRow.setLayoutParams(new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.MATCH_PARENT,
                    1.0f));
            tableLayout.addView(tableRowTmp);
            tableRow = tableRowTmp;
            numInRow = 0;
        }

        tableRow.addView(tmp);
        numInRow++;

    }



}
