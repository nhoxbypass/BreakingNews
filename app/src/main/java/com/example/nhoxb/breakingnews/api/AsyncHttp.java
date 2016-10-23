package com.example.nhoxb.breakingnews.api;

import com.example.nhoxb.breakingnews.utils.Constants;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.ResponseHandlerInterface;

import java.util.Map;

/**
 * Created by nhoxb on 10/23/2016.
 */
public class AsyncHttp {
    AsyncHttpClient client;
    Map<String,String> queryMap;

    RequestParams params;

    public AsyncHttp(Map<String,String> queryMap) {
        client = new AsyncHttpClient();
        queryMap.putAll(queryMap);
        params = new RequestParams();

        for (String key : queryMap.keySet()) {
            params.put(key, queryMap.get(key));
        }
        params.put("api-key", Constants.ARTICLE_SEARCH_API);
    }

    public AsyncHttp() {
        super();
    }

    public RequestParams getParams() {
        return params;
    }

    public void setQuery(Map<String,String> queryMap)
    {
        queryMap.clear();
        queryMap.putAll(queryMap);
        params = new RequestParams();

        for (String key : queryMap.keySet()) {
            params.put(key, queryMap.get(key));
        }
    }

    public void get(ResponseHandlerInterface responseHandlerInterface)
    {
        client.get(Constants.BASE_URL, params,responseHandlerInterface);
    }
}
