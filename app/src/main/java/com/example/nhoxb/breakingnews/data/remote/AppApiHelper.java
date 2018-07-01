package com.example.nhoxb.breakingnews.data.remote;

import com.example.nhoxb.breakingnews.data.remote.model.ArticleResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.loopj.android.http.TextHttpResponseHandler;

import java.util.Map;

import cz.msebera.android.httpclient.Header;

/**
 * Created by tom on 7/1/18.
 */
public class AppApiHelper implements ApiHelper {
    private AsyncHttp asyncHttp;

    public AppApiHelper() {
        asyncHttp = new AsyncHttp();
    }

    @Override
    public void getArticles(Map<String, String> map, OnResponseListener responseListener) {
        asyncHttp.setQuery(map);
        asyncHttp.getArticles(new TextHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String res) {
                // called when response HTTP status is "200 OK"
                Gson gson = new GsonBuilder().create();
                // Define Response class to correspond to the JSON response returned
                JsonObject jsonResponse = gson.fromJson(res, JsonObject.class);
                String status = jsonResponse.get("status").getAsString();

                if (status.equals("OK")) {
                    ArticleResponse articleResponse = gson.fromJson(jsonResponse.getAsJsonObject("response"), ArticleResponse.class);
                    responseListener.onResponse(articleResponse.getListArticle());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
            }
        });
    }
}
