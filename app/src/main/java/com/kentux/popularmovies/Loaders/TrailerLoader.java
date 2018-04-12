package com.kentux.popularmovies.Loaders;

import android.content.AsyncTaskLoader;
import android.content.Context;

import com.kentux.popularmovies.model.MovieTrailer;
import com.kentux.popularmovies.utils.QueryUtils;

import java.util.List;

/**
 * Created by Tiago Gomes on 11/04/2018.
 */

public class TrailerLoader extends AsyncTaskLoader<List<MovieTrailer>> {
    private String mUrl;

    public TrailerLoader(Context context, String url) {
        super(context);
        mUrl = url;

    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<MovieTrailer> loadInBackground() {
        if (mUrl == null) {
            return null;
        }

        return QueryUtils.fetchTrailerData(mUrl);
    }
}
