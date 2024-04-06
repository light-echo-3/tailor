package com.bytedance.demo;

import android.app.Activity;
import android.app.Application;

public class MyApplication extends Application {

    public static MyApplication application;
    public static Activity topActivity;

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
    }



}
