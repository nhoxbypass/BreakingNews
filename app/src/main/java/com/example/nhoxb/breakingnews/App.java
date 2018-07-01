package com.example.nhoxb.breakingnews;

import android.app.Application;

import com.example.nhoxb.breakingnews.data.AppDataManager;
import com.example.nhoxb.breakingnews.data.DataManager;

/**
 * Created by tom on 7/1/18.
 */
public class App extends Application {
    private static DataManager dataManager;

    public static DataManager getDataManager() {
        return dataManager;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        dataManager = new AppDataManager();
    }
}
