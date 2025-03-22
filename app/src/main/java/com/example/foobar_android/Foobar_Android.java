package com.example.foobar_android;

import android.app.Application;
import android.content.Context;

public class Foobar_Android extends Application {
    public static Context context;

    @Override
    public void onCreate(){
        super.onCreate();
        context = getApplicationContext();
    }
}
