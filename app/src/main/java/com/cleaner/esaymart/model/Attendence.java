package com.cleaner.esaymart.model;

/**
 * Created by LifePlayTrip on 2/20/2018.
 */

public class Attendence {
    private String date;
    private int present;
    private int absent;


    public Attendence(String news_name, int present, int absent) {
        this.date = news_name;
        this.present = present;
        this.absent = absent;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getPresent() {
        return present;
    }

    public void setPresent(int present) {
        this.present = present;
    }

    public int getAbsent() {
        return absent;
    }

    public void setAbsent(int absent) {
        this.absent = absent;
    }
}
