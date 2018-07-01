package com.example.nhoxb.breakingnews.data.remote;

import com.example.nhoxb.breakingnews.utils.Constants;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.ResponseHandlerInterface;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by nhoxb on 10/23/2016.
 */
public class AsyncHttp {
    AsyncHttpClient client;
    Map<String, String> queryMap;

    RequestParams params;

    public AsyncHttp() {
        super();
        client = new AsyncHttpClient();
        queryMap = new HashMap<>();
    }

    public RequestParams getParams() {
        return params;
    }

    public void setQuery(Map<String, String> map) {
        this.queryMap.clear();
        this.queryMap.putAll(map);
        params = new RequestParams();
        params.put("api-key", Constants.ARTICLE_SEARCH_API);

        for (String key : queryMap.keySet()) {
            params.put(key, queryMap.get(key));
        }
    }

    public void getArticles(ResponseHandlerInterface responseHandlerInterface) {
        client.get(ApiEndPoint.BASE_URL, params, responseHandlerInterface);
    }
}
