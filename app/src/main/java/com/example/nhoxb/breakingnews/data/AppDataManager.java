package com.example.nhoxb.breakingnews.data;

import com.example.nhoxb.breakingnews.data.remote.ApiHelper;
import com.example.nhoxb.breakingnews.data.remote.AppApiHelper;

import java.util.Map;

/**
 * Created by tom on 7/1/18.
 */
public class AppDataManager implements DataManager {

    ApiHelper apiHelper;

    public AppDataManager() {
        super();
        apiHelper = new AppApiHelper();
    }

    @Override
    public void getArticles(Map<String, String> map, OnResponseListener responseListener) {
        apiHelper.getArticles(map, responseListener);
    }
}
