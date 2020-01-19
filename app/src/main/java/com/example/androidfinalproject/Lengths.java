package com.example.androidfinalproject;

public class Lengths {
    private double length;
    private int amount;
    private String id;

    public Lengths(){

    }

    public Lengths(double length, int amount, String id) {
        this.length = length;
        this.amount = amount;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

//    public boolean equals(Double len)
//    {
//        return this.length == len;
//    }
}
