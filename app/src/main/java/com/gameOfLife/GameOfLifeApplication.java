package com.gameOfLife;

import android.app.Application;
import android.content.Context;

public class GameOfLifeApplication extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        GameOfLifeApplication.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return context;
    }
}