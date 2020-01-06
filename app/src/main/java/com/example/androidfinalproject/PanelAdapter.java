package com.example.androidfinalproject;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class PanelAdapter  extends ArrayAdapter<Panel> {

    private Activity context;
    List<Panel> panel;

    public PanelAdapter(Activity context, List<Panel> panels) {
        super(context, R.layout.activity_main, panels);
        this.context = context;
        this.panel = panels;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.activity_main, null, true);

//        TextView textViewName = (TextView) listViewItem.findViewById(R.id.textViewName);
//        TextView textViewGenre = (TextView) listViewItem.findViewById(R.id.textViewGenre);

        Panel p = panel.get(position);


        return listViewItem;
    }
}
