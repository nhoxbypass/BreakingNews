package com.example.nhoxb.breakingnews.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.nhoxb.breakingnews.R;
import com.example.nhoxb.breakingnews.adapter.ArticleListAdapter;
import com.example.nhoxb.breakingnews.model.Article;
import com.example.nhoxb.breakingnews.model.ArticleResponse;
import com.example.nhoxb.breakingnews.utils.Constants;
import com.example.nhoxb.breakingnews.utils.EndlessRecyclerViewScrollListener;
import com.example.nhoxb.breakingnews.utils.FilterDialog;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Filter;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

    SearchView mSearchView;
    RecyclerView mRecyclerView;
    ArticleListAdapter mListAdapter;
    StaggeredGridLayoutManager staggeredGridLayoutManager;
    ProgressBar mPreloadProgressBar;
    RelativeLayout mLoadMoreProgressBar;

    public static final String URL_KEY = "url";

    private interface ResponseInterface
    {
        void onResponse(List<Article> articleList);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_list_item);
        mPreloadProgressBar = (ProgressBar) findViewById(R.id.pb_preload);
        mLoadMoreProgressBar = (RelativeLayout) findViewById(R.id.pb_loadmore);
        mRecyclerView.setLayoutManager(staggeredGridLayoutManager);
        mListAdapter = new ArticleListAdapter();
        mRecyclerView.setAdapter(mListAdapter);
        mListAdapter.setOnItemClickListener(new ArticleListAdapter.OnArticleClickListener() {
            @Override
            public void onArticleClick(Article article) {
                Intent intent = new Intent(MainActivity.this, ArticleDetailActivity.class);
                intent.putExtra(URL_KEY,article.getWebUrl());
                startActivity(intent);
            }
        });

        mRecyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(staggeredGridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                mLoadMoreProgressBar.setVisibility(View.VISIBLE);
                Map map = new HashMap();
                map.put("page",String.valueOf(page));
                loadMoreArticle(map);
            }
        });

        Map map = new HashMap();
        loadMoreArticle(map);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        MenuItem menuItem = menu.findItem(R.id.item_search);
        mSearchView = (SearchView) MenuItemCompat.getActionView(menuItem);

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mPreloadProgressBar.setVisibility(View.VISIBLE);
                // Check if no view has focus:
                View view = getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }

                Map map = new HashMap();
                map.put("q",query);
                loadArticle(map);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Handle item selection
        switch (item.getItemId())
        {
            case R.id.item_sort:
                FilterDialog dialog = new FilterDialog(this) {
                    @Override
                    public void onResponse(Map<String, String> map) {
                        loadArticle(map);
                    }
                };
                dialog.show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void fetchData(Map<String,String> queryMap, final ResponseInterface responseInterface)
    {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        for (String key : queryMap.keySet())
        {
            params.put(key, queryMap.get(key));
        }

        params.put("api-key", Constants.ARTICLE_SEARCH_API);
        client.get(Constants.BASE_URL, params, new TextHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String res) {
                        // called when response HTTP status is "200 OK"
                        Gson gson = new GsonBuilder().create();
                        // Define Response class to correspond to the JSON response returned
                        JsonObject jsonResponse = gson.fromJson(res, JsonObject.class);
                        String status = jsonResponse.get("status").getAsString();

                        if (status.equals("OK"))
                        {
                            ArticleResponse articleResponse = gson.fromJson(jsonResponse.getAsJsonObject("response"),ArticleResponse.class);
                            //mListAdapter.setArticles(articleResponse.getListArticle());
                            responseInterface.onResponse(articleResponse.getListArticle());
                            Toast.makeText(MainActivity.this, mListAdapter.getItemCount() + "", Toast.LENGTH_SHORT).show();
                            handleLoadComplete();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                        // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                    }
                }
        );
    }

    private void loadArticle(Map<String,String> map)
    {
        fetchData(map, new ResponseInterface() {
            @Override
            public void onResponse(List<Article> articleList) {
                mListAdapter.setArticles(articleList);
            }
        });
    }

    private void loadMoreArticle(final Map<String,String> map)
    {
        fetchData(map, new ResponseInterface() {
            @Override
            public void onResponse(List<Article> articleList) {
                mListAdapter.addArticles(articleList);
            }
        });
    }

    private void handleLoadComplete()
    {
        mLoadMoreProgressBar.setVisibility(View.GONE);
        mPreloadProgressBar.setVisibility(View.GONE);
    }
}

