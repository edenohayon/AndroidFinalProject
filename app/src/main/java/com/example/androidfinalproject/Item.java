package com.example.androidfinalproject;

public class Item {

    private int id;
    private String name;
    private double length;
    private int amount;



    public Item(int id, String name, double length, int amount) {
        this.id = id;
        this.name = name;
        this.length = length;
        this.amount = amount;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLength(float length) {
        this.length = length;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getLength() {
        return length;
    }

    public int getAmount() {
        return amount;
    }
}
