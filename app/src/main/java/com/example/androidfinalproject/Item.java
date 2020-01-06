package com.example.androidfinalproject;

import java.util.HashMap;
import java.util.List;

public class Item {

    private String id;
    private String name;
    private HashMap<Lengths, Boolean> lengths;
    private int counter = 0;

    public Item() {
        //this constructor is required
        counter++;
    }

    public Item(String id, String name, HashMap<Lengths, Boolean> lengths) {
        this.id = id;
        this.name = name;
        this.lengths = lengths;
        counter++;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public HashMap<Lengths, Boolean> getLengths() {
        return lengths;
    }

    public void setLengths(HashMap<Lengths, Boolean> lengths) {
        this.lengths = lengths;
    }
    public int getCounter(){
        return this.counter;
    }


}


