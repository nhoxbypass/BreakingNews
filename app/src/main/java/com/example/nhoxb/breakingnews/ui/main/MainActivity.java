package com.example.nhoxb.breakingnews.ui.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.example.nhoxb.breakingnews.App;
import com.example.nhoxb.breakingnews.R;
import com.example.nhoxb.breakingnews.data.DataManager;
import com.example.nhoxb.breakingnews.data.remote.ApiHelper;
import com.example.nhoxb.breakingnews.ui.detail.ArticleDetailActivity;
import com.example.nhoxb.breakingnews.utils.Constants;
import com.example.nhoxb.breakingnews.utils.NetworkUtils;
import com.example.nhoxb.breakingnews.utils.UiUtils;

import org.parceler.Parcels;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    public static final String ARTICLE_KEY = "url";
    SearchView mSearchView;
    @BindView(R.id.rv_list_item)
    RecyclerView mRecyclerView;
    @BindView(R.id.pb_preload)
    ProgressBar mPreloadProgressBar;
    @BindView(R.id.rl_loadmore)
    RelativeLayout mLoadMoreProgressBar;
    StaggeredGridLayoutManager staggeredGridLayoutManager;
    ArticleListAdapter mListAdapter;
    Map<String, String> queryMap;  //Use global query map for Subsequent searches will have any filters applied to the search results
    DataManager dataManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!NetworkUtils.isNetworkAvailable(this)) {
            showSettingDialog();
        }

        setupUi();

        dataManager = App.getDataManager();

        // Setup listeners
        mListAdapter.setOnItemClickListener(article -> {
            Intent intent = new Intent(MainActivity.this, ArticleDetailActivity.class);
            intent.putExtra(ARTICLE_KEY, Parcels.wrap(article));
            startActivity(intent);
        });

        mRecyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(staggeredGridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                mLoadMoreProgressBar.setVisibility(View.VISIBLE);
                queryMap.put(Constants.QUERY_KEY_PAGE, String.valueOf(page));
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

                queryMap.put(Constants.QUERY_KEY, query);
                queryMap.put(Constants.QUERY_KEY_PAGE, "0");
                loadArticle(queryMap);
                mSearchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        MenuItemCompat.setOnActionExpandListener(menuItem, new MenuItemCompat.OnActionExpandListener() {
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
                FilterDialog dialog = new FilterDialog(this, map -> {
                    //Replace filter key
                    for (String key : map.keySet()) {
                        queryMap.put(key, map.get(key));
                    }
                    loadArticle(queryMap);
                });
                dialog.show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupUi() {
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);

        ButterKnife.bind(this);

        mRecyclerView.setLayoutManager(staggeredGridLayoutManager);
        mListAdapter = new ArticleListAdapter();
        mRecyclerView.setAdapter(mListAdapter);
    }

    private void fetchData(Map<String, String> queryMap, final ApiHelper.OnResponseListener listener) {
        dataManager.getArticles(queryMap, listener);
    }

    private void loadArticle(Map<String, String> map) {
        mPreloadProgressBar.setVisibility(View.VISIBLE);
        fetchData(map, articleList -> {
            mListAdapter.setArticles(articleList);
            mRecyclerView.scrollToPosition(0);
            handleLoadComplete();
        });
    }

    private void loadMoreArticle(final Map<String, String> map) {
        fetchData(map, articleList -> {
            if (!articleList.isEmpty()) {
                //If there are more article -> load
                mListAdapter.addArticles(articleList);
            }
            handleLoadComplete();
        });
    }

    private void handleLoadComplete() {
        mLoadMoreProgressBar.setVisibility(View.GONE);
        mPreloadProgressBar.setVisibility(View.GONE);
    }

    private void showSettingDialog() {
        AlertDialog alertDialog = UiUtils.createAlertDialog(this);
        alertDialog.show();
    }


}

