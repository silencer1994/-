package com.navifunctiontest;

import android.app.Application;

/**
 * Created by Administrator on 2017/4/21 0021.
 */

public class APP extends Application {
    private static APP myApplication = null;
    public static boolean enablaWifi;


    @Override
    public void onCreate() {
        super.onCreate();
        myApplication = this;
    }

    public static  APP getInstance(){
        if(myApplication==null){
            myApplication = new APP();
        }
        return myApplication;
    }
    public void setEnablaWifi(boolean isEnableWifi){
        enablaWifi = isEnableWifi;
    }
}
