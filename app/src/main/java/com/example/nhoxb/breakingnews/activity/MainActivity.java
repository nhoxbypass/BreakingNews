package com.example.nhoxb.breakingnews.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.support.v4.view.MenuCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.nhoxb.breakingnews.R;
import com.example.nhoxb.breakingnews.adapter.ArticleListAdapter;
import com.example.nhoxb.breakingnews.api.AsyncHttp;
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

import org.parceler.Parcels;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Filter;
import java.util.zip.Inflater;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

    SearchView mSearchView;
    @BindView(R.id.rv_list_item) RecyclerView mRecyclerView;
    ArticleListAdapter mListAdapter;
    StaggeredGridLayoutManager staggeredGridLayoutManager;
    @BindView(R.id.pb_preload) ProgressBar mPreloadProgressBar;
    @BindView(R.id.rl_loadmore) RelativeLayout mLoadMoreProgressBar;
    //Use global query map for Subsequent searches will have any filters applied to the search results
    Map<String, String> queryMap;
    AsyncHttp asyncHttp;

    public static final String ARTICLE_KEY = "url";

    private interface ResponseInterface {
        void onResponse(List<Article> articleList);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //

        setContentView(R.layout.activity_main);

        if (!isNetworkAvailable()) {
            showSettingDialog();
        }

        staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);

        ButterKnife.bind(this);

        mRecyclerView.setLayoutManager(staggeredGridLayoutManager);
        mListAdapter = new ArticleListAdapter();
        mRecyclerView.setAdapter(mListAdapter);
        mListAdapter.setOnItemClickListener(article -> {
            Intent intent = new Intent(MainActivity.this, ArticleDetailActivity.class);

            intent.putExtra(ARTICLE_KEY, Parcels.wrap(article));
            startActivity(intent);
        });

        mRecyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(staggeredGridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                mLoadMoreProgressBar.setVisibility(View.VISIBLE);
                queryMap.put("page", String.valueOf(page));
                loadMoreArticle(queryMap);
            }
        });

        queryMap = new HashMap<>();
        loadMoreArticle(queryMap);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);
        MenuItem menuItem = menu.findItem(R.id.item_search);
        mSearchView = (SearchView) MenuItemCompat.getActionView(menuItem);

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Check if no view has focus:
                View view = getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }

                queryMap.put("q", query);
                queryMap.put("page", "0");
                loadArticle(queryMap);
                mSearchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        MenuItemCompat.setOnActionExpandListener(menuItem ,new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                queryMap = new HashMap<>();
                loadArticle(queryMap);
                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Handle item selection
        switch (item.getItemId()) {
            case R.id.item_sort:
                FilterDialog dialog = new FilterDialog(this) {
                    @Override
                    public void onResponse(Map<String, String> map) {
                        //Replace filter key
                        for (String key : map.keySet()) {
                            queryMap.put(key, map.get(key));
                        }
                        loadArticle(queryMap);
                    }
                };
                dialog.show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void fetchData(Map<String, String> queryMap, final ResponseInterface responseInterface) {
        asyncHttp = new AsyncHttp(queryMap);
        asyncHttp.get(new TextHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String res) {
                        // called when response HTTP status is "200 OK"
                        Gson gson = new GsonBuilder().create();
                        // Define Response class to correspond to the JSON response returned
                        JsonObject jsonResponse = gson.fromJson(res, JsonObject.class);
                        String status = jsonResponse.get("status").getAsString();

                        if (status.equals("OK")) {
                            ArticleResponse articleResponse = gson.fromJson(jsonResponse.getAsJsonObject("response"), ArticleResponse.class);
                            //mListAdapter.setArticles(articleResponse.getListArticle());
                            responseInterface.onResponse(articleResponse.getListArticle());

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

    private void loadArticle(Map<String, String> map) {
        mPreloadProgressBar.setVisibility(View.VISIBLE);
        fetchData(map, articleList -> {
            mListAdapter.setArticles(articleList);
            mRecyclerView.scrollToPosition(0);
        });
    }

    private void loadMoreArticle(final Map<String, String> map) {
        fetchData(map, articleList -> mListAdapter.addArticles(articleList));
    }

    private void handleLoadComplete() {
        mLoadMoreProgressBar.setVisibility(View.GONE);
        mPreloadProgressBar.setVisibility(View.GONE);
    }

    private Boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if (!(activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting()))
            return false;
        else
        {
            Runtime runtime = Runtime.getRuntime();
            try {
                Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
                int exitValue = ipProcess.waitFor();
                return (exitValue == 0);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return false;
        }
    }

    private void showSettingDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Connect to wifi or quit")
                .setCancelable(false)
                .setPositiveButton("Connect to WIFI", (dialog, id) -> {
                    startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                })
                .setNegativeButton("Quit", (dialog, id) -> {
                    finish();
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
}

