package com.example.mc_sign_app;

import android.app.Application;

public class MyApplication extends Application {

    private int counter=1;

    public int getCounter() {
        return counter;
    }

    public void setCounter(int val) {
        this.counter = val;
    }
}