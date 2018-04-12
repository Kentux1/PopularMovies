package com.kentux.popularmovies.Loaders;

import android.content.AsyncTaskLoader;
import android.content.Context;

import com.kentux.popularmovies.model.Movie;
import com.kentux.popularmovies.utils.QueryUtils;

import java.util.List;

/**
 * Created by Tiago Gomes on 14/03/2018.
 */

public class MovieLoader extends AsyncTaskLoader<List<Movie>> {
    private String mUrl;

    public MovieLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Movie> loadInBackground() {
        if (mUrl == null) {
            return null;
        }

        return QueryUtils.fetchMovieData(mUrl);
    }
}
