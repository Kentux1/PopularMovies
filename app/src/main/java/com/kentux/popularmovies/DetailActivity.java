package com.kentux.popularmovies;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.kentux.popularmovies.Loaders.ReviewLoader;
import com.kentux.popularmovies.Loaders.TrailerLoader;
import com.kentux.popularmovies.adapters.MovieReviewAdapter;
import com.kentux.popularmovies.adapters.MovieTrailerAdapter;
import com.kentux.popularmovies.model.Movie;
import com.kentux.popularmovies.model.MovieReview;
import com.kentux.popularmovies.model.MovieTrailer;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity {

    public static final String LOG_TAG = "DetailActivity";
    private static final int TRAILER_RESULT_LOADER_ID = 1;
    private static final int REVIEW_RESULT_LOADER_ID = 2;
    Context context;
    ImageView moviePoster;
    TextView mEmptyStateTrailersTextView, mEmptyStateReviewsTextView, movieTitle, movieReleaseDate, movieSynopsis, movieVoteAverage;
    Movie movieData;
    String movieID, releaseDateText, voteAverageText, TMDB_TRAILER_URL, TMDB_REVIEW_URL;
    RecyclerView trailerRecyclerView, reviewRecyclerView;
    private List<MovieTrailer> trailerList = new ArrayList<>();
    private List<MovieReview> reviewList = new ArrayList<>();
    private MovieTrailerAdapter mTrailerAdapter;
    private MovieReviewAdapter mReviewAdapter;
    private LoaderManager.LoaderCallbacks<List<MovieTrailer>> trailerResultLoaderListener
            = new LoaderManager.LoaderCallbacks<List<MovieTrailer>>() {
        @Override
        public Loader<List<MovieTrailer>> onCreateLoader(int i, Bundle bundle) {
            Uri.Builder builder = new Uri.Builder();
            builder.scheme(getString(R.string.scheme))
                    .authority(getString(R.string.TMDb_base_authority))
                    .appendPath(getString(R.string.TMDb_base_path_3))
                    .appendPath(getString(R.string.TMDb_base_path_movie))
                    .appendPath(movieData.getMovieID())
                    .appendPath(getString(R.string.TMDb_videos_path))
                    .appendQueryParameter(getString(R.string.TMDb_base_api_key_query),
                            getString(R.string.my_api_key));
            TMDB_TRAILER_URL = builder.build().toString();

            Log.v(LOG_TAG, "Uri: " + TMDB_TRAILER_URL);

            return new TrailerLoader(getApplicationContext(), TMDB_TRAILER_URL);
        }

        @Override
        public void onLoadFinished(Loader<List<MovieTrailer>> loader, List<MovieTrailer> movieTrailerList) {
            mEmptyStateTrailersTextView.setText("No trailers to display");

            mTrailerAdapter.clear();

            if (trailerList != null && !trailerList.isEmpty()) {
                mEmptyStateTrailersTextView.setText("");
                mTrailerAdapter.swap(trailerList);
            }
        }

        @Override
        public void onLoaderReset(Loader<List<MovieTrailer>> loader) {
            mTrailerAdapter.clear();
        }
    };
    private LoaderManager.LoaderCallbacks<List<MovieReview>> reviewResultLoaderListener
            = new LoaderManager.LoaderCallbacks<List<MovieReview>>() {
        @Override
        public Loader<List<MovieReview>> onCreateLoader(int i, Bundle bundle) {
            Uri.Builder builder = new Uri.Builder();
            builder.scheme(getString(R.string.scheme))
                    .authority(getString(R.string.TMDb_base_authority))
                    .appendPath(getString(R.string.TMDb_base_path_3))
                    .appendPath(getString(R.string.TMDb_base_path_movie))
                    .appendPath(movieData.getMovieID())
                    .appendPath(getString(R.string.TMDb_reviews_path))
                    .appendQueryParameter(getString(R.string.TMDb_base_api_key_query),
                            getString(R.string.my_api_key));
            TMDB_REVIEW_URL = builder.build().toString();

            Log.v(LOG_TAG, "Uri: " + TMDB_REVIEW_URL);

            return new ReviewLoader(getApplicationContext(), TMDB_REVIEW_URL);
        }

        @Override
        public void onLoadFinished(Loader<List<MovieReview>> loader, List<MovieReview> movieReviews) {
            mReviewAdapter.clear();
            if (reviewList != null && !reviewList.isEmpty()) {
                mReviewAdapter.swap(reviewList);
            }
        }

        @Override
        public void onLoaderReset(Loader<List<MovieReview>> loader) {
            mReviewAdapter.clear();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        movieData = intent.getExtras().getParcelable("movieData");

        if (movieData != null) {
            movieID = movieData.getMovieID();
        }

        mEmptyStateTrailersTextView = findViewById(R.id.no_trailers);

        mEmptyStateReviewsTextView = findViewById(R.id.no_reviews);

        releaseDateText = getString(R.string.release_date);

        voteAverageText = getString(R.string.vote_average);

        movieTitle = findViewById(R.id.movie_title);

        moviePoster = findViewById(R.id.movie_image);

        movieReleaseDate = findViewById(R.id.movie_release_date);

        movieSynopsis = findViewById(R.id.movie_overview);

        movieVoteAverage = findViewById(R.id.movie_vote_average);

        polishUI();

        trailerRecyclerView = findViewById(R.id.trailer_recycler_view);
        reviewRecyclerView = findViewById(R.id.review_recycler_view);

        mTrailerAdapter = new MovieTrailerAdapter(trailerList);
        mReviewAdapter = new MovieReviewAdapter(reviewList);
        RecyclerView.LayoutManager mTrailerLayoutManager = new LinearLayoutManager(this, 1, false);
        RecyclerView.LayoutManager mReviewLayoutManager = new LinearLayoutManager(this, 1, false);
        trailerRecyclerView.setLayoutManager(mTrailerLayoutManager);
        trailerRecyclerView.setItemAnimator(new DefaultItemAnimator());
        trailerRecyclerView.setAdapter(mTrailerAdapter);

        reviewRecyclerView.setLayoutManager(mReviewLayoutManager);
        reviewRecyclerView.setItemAnimator(new DefaultItemAnimator());
        reviewRecyclerView.setAdapter(mReviewAdapter);

        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connMgr != null) {
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {
                LoaderManager loaderManager = getLoaderManager();

                loaderManager.initLoader(TRAILER_RESULT_LOADER_ID, null, trailerResultLoaderListener);
                loaderManager.initLoader(REVIEW_RESULT_LOADER_ID, null, reviewResultLoaderListener);
            }
        }
    }

    private void polishUI() {
        String moviePosterPath = movieData.getMoviePoster();
        if (movieData.getMoviePoster() != null) {
            Uri.Builder builder = new Uri.Builder();
            builder.scheme(getString(R.string.scheme))
                    .authority(getString(R.string.TMDB_poster_authority))
                    .appendPath(getString(R.string.TMDB_poster_t))
                    .appendPath(getString(R.string.TMDB_poster_p))
                    .appendPath(getString(R.string.TMDB_poster_w185))
                    .appendEncodedPath(moviePosterPath);
            String TMDB_MOVIE_POSTER_PATH = builder.build().toString();
            Log.v(LOG_TAG, "Builder: " + TMDB_MOVIE_POSTER_PATH);
            String TMDb_MOVIE_POSTER_PATH = getString(R.string.TMDb_poster_path) + movieData.getMoviePoster();
            Log.v(LOG_TAG, "string: " + TMDb_MOVIE_POSTER_PATH);
            Picasso.with(this).load(TMDb_MOVIE_POSTER_PATH).into(moviePoster);
        }
        if (movieData.getMovieTitle() != null) {
            setTitle(movieData.getMovieTitle());
            movieTitle.setText(movieData.getMovieTitle());
        }
        if (movieData.getReleaseDate() != null) {
            String displayReleaseDate = releaseDateText + " " + movieData.getReleaseDate();
            movieReleaseDate.setText(displayReleaseDate);
        }
        if (movieData.getPlotSynopsis() != null) {
            movieSynopsis.setText(movieData.getPlotSynopsis());
        }
        if (movieData.getVoteAverage() != null) {
            String displayVoteAverage = voteAverageText + " " + movieData.getVoteAverage();
            movieVoteAverage.setText(displayVoteAverage);
        }
    }

}
