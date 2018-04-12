package com.kentux.popularmovies.Loaders;

import android.content.AsyncTaskLoader;
import android.content.Context;

import com.kentux.popularmovies.model.MovieReview;
import com.kentux.popularmovies.utils.QueryUtils;

import java.util.List;

/**
 * Created by Tiago Gomes on 11/04/2018.
 */

public class ReviewLoader extends AsyncTaskLoader<List<MovieReview>> {
    private String mUrl;

    public ReviewLoader(Context context, String url) {
        super(context);
        mUrl = url;

    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<MovieReview> loadInBackground() {
        if (mUrl == null) {
            return null;
        }

        return QueryUtils.fetchReviewData(mUrl);
    }
}
