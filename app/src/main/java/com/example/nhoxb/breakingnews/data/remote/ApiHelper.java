package com.example.nhoxb.breakingnews.data.remote;

import com.example.nhoxb.breakingnews.data.remote.model.Article;

import java.util.List;
import java.util.Map;

/**
 * Created by tom on 7/1/18.
 */
public interface ApiHelper {
    void getArticles(Map<String, String> map, OnResponseListener responseListener);

    public interface OnResponseListener {
        void onResponse(List<Article> articleList);
    }
}
