package com.kentux.popularmovies;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.kentux.popularmovies.model.Movie;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Movie>> {

    private Context context;

    private List<Movie> movieList = new ArrayList<>();

    private RecyclerView recyclerView;

    private TextView mEmptyStateTextView;

    private MovieAdapter mAdapter;

    private static LoaderManager loaderManager;

    ProgressBar loadingIndicator;

    SwipeRefreshLayout swipeRefreshLayout;

    String THE_MOVIE_DATABASE_URL;

    String myApiKey;

    @Override
    public Loader<List<Movie>> onCreateLoader(int i, Bundle bundle) {
        myApiKey = getString(R.string.my_api_key);
        String uri = "http://api.themoviedb.org/3/movie/popular" + "?api_key=" + myApiKey;
        Log.v("Main Activity", "Uri: " + uri);

        return new MovieLoader(this, uri);
    }

    @Override
    public void onLoadFinished(Loader<List<Movie>> loader, List<Movie> movies) {
        loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);

        mEmptyStateTextView.setText(R.string.no_movies);

        mAdapter.clear();

        if (movies != null && !movies.isEmpty()) {
            mEmptyStateTextView.setText("");
            mAdapter.swap(movies);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Movie>> loader) {
        mAdapter.clear();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycler_view);
        mEmptyStateTextView = findViewById(R.id.empty_view);


        mAdapter = new MovieAdapter(movieList);
        if (mAdapter.getItemCount() == 0) {
            mEmptyStateTextView.setVisibility(View.VISIBLE);
        }
        swipeRefreshLayout = findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshRecyclerView();
            }
        });
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this,
                2,
                GridLayoutManager.VERTICAL,
                false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connMgr != null) {
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {
                LoaderManager loaderManager = getLoaderManager();

                loaderManager.initLoader(1, null, this);
            } else {
                View loadingIndicator = findViewById(R.id.loading_indicator);
                loadingIndicator.setVisibility(View.GONE);
                mEmptyStateTextView.setText(R.string.no_internet);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        String uri;
        switch (id) {
            case R.id.refresh: refreshRecyclerView();
                break;
            case R.id.popular_menu: uri = "http://api.themoviedb.org/3/movie/popular" + "?api_key=" + myApiKey;
                Log.v("Main Activity", "Uri: " + uri);

                new MovieLoader(this, uri);
                refreshRecyclerView();
                break;
            case R.id.top_rated_menu: uri = "http://api.themoviedb.org/3/movie/top_rated" + "?api_key=" + myApiKey;
                Log.v("Main Activity", "Uri: " + uri);

                new MovieLoader(this, uri);
                refreshRecyclerView();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void refreshRecyclerView() {
        mEmptyStateTextView.setText("");
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connMgr != null) {
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {
                if (loadingIndicator != null) {
                    loadingIndicator.setVisibility(View.VISIBLE);
                }
                mEmptyStateTextView.setText("");

                if (mAdapter != null) {
                    mAdapter.clear();
                }
                if (loaderManager != null) {
                    loaderManager.restartLoader(1, null, this);
                    swipeRefreshLayout.setRefreshing(false);
                } else {
                    mEmptyStateTextView.setVisibility(View.GONE);
                    loaderManager = getLoaderManager();
                    loaderManager.initLoader(1, null, this);
                    recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

                    mAdapter = new MovieAdapter(movieList);
                    RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this,
                            2,
                            GridLayoutManager.VERTICAL,
                            false);
                    recyclerView.setLayoutManager(mLayoutManager);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.setAdapter(mAdapter);
                    swipeRefreshLayout.setRefreshing(false);
                }
            } else {
                // Hide progressBar
                if (loadingIndicator != null) {
                    loadingIndicator.setVisibility(View.GONE);
                }
                // Check if mAdapter is not null (which will happen if on launch there was no
                // connection)
                if (mAdapter != null) {
                    // Clear the adapter
                    mAdapter.clear();
                }
                mEmptyStateTextView.setText(R.string.no_internet);
                swipeRefreshLayout.setRefreshing(false);
            }
        }
    }
}
