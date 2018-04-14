package com.kentux.popularmovies;

import android.app.LoaderManager;
import android.content.ContentValues;
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
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kentux.popularmovies.Loaders.ReviewLoader;
import com.kentux.popularmovies.Loaders.TrailerLoader;
import com.kentux.popularmovies.adapters.MovieReviewAdapter;
import com.kentux.popularmovies.adapters.MovieTrailerAdapter;
import com.kentux.popularmovies.data.MovieContract.MovieEntry;
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
    ImageView moviePosterIV;
    TextView mEmptyStateTrailersTextView, mEmptyStateReviewsTextView, mTrailerHeaderTextView,
            mReviewHeaderTextView, movieReleaseDateTV, movieSynopsisTV, movieVoteAverageTV;
    Movie movieData;
    String movieID, releaseDateText, voteAverageText, TMDB_TRAILER_URL, TMDB_REVIEW_URL;
    String movieName, moviePoster, movieReleaseDate, movieVoteAverage, movieSynopsis;
    RecyclerView trailerRecyclerView, reviewRecyclerView;
    Toolbar toolbar;
    private Uri mCurrentMovieUri;
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

            //mTrailerAdapter.setTrailers(movieTrailerList);

            if (movieTrailerList != null && !movieTrailerList.isEmpty()) {
                mEmptyStateTrailersTextView.setText("");
                mTrailerHeaderTextView.setText(getString(R.string.trailers_title));
                mTrailerAdapter.setTrailers(movieTrailerList);
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
            mEmptyStateReviewsTextView.setText("No reviews to display");

            mReviewAdapter.clear();

            if (movieReviews != null && !movieReviews.isEmpty()) {
                mEmptyStateReviewsTextView.setText("");
                mReviewHeaderTextView.setText(getString(R.string.reviews_title));
                mReviewAdapter.setReviews(movieReviews);
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
        toolbar = findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        Intent intent = getIntent();
        movieData = intent.getExtras().getParcelable("movieData");
        mCurrentMovieUri = intent.getData();
        this.setTitle(movieData.getMovieTitle());
        if (movieData != null) {
            movieID = movieData.getMovieID();
        }

        mTrailerHeaderTextView = findViewById(R.id.trailer_header);

        mReviewHeaderTextView = findViewById(R.id.review_header);

        mEmptyStateTrailersTextView = findViewById(R.id.no_trailers);

        mEmptyStateReviewsTextView = findViewById(R.id.no_reviews);

        releaseDateText = getString(R.string.release_date);

        voteAverageText = getString(R.string.vote_average);

        moviePosterIV = findViewById(R.id.movie_image);

        movieReleaseDateTV = findViewById(R.id.movie_release_date);

        movieSynopsisTV = findViewById(R.id.movie_overview);

        movieVoteAverageTV = findViewById(R.id.movie_vote_average);

        polishUI();

        trailerRecyclerView = findViewById(R.id.trailer_recycler_view);
        reviewRecyclerView = findViewById(R.id.review_recycler_view);

        mTrailerAdapter = new MovieTrailerAdapter(trailerList);
        mReviewAdapter = new MovieReviewAdapter(reviewList);
        RecyclerView.LayoutManager mTrailerLayoutManager = new LinearLayoutManager(this);
        RecyclerView.LayoutManager mReviewLayoutManager = new LinearLayoutManager(this);

        trailerRecyclerView.setLayoutManager(mTrailerLayoutManager);
        trailerRecyclerView.setItemAnimator(new DefaultItemAnimator());
        trailerRecyclerView.setNestedScrollingEnabled(false);
        trailerRecyclerView.setAdapter(mTrailerAdapter);

        reviewRecyclerView.setLayoutManager(mReviewLayoutManager);
        reviewRecyclerView.setItemAnimator(new DefaultItemAnimator());
        reviewRecyclerView.setNestedScrollingEnabled(false);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.add_to_favorites_menu) {
            saveMovieAsFavorite();
        }
        return super.onOptionsItemSelected(item);
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
            String TMDb_MOVIE_POSTER_PATH = builder.build().toString();
            Log.v(LOG_TAG, "Builder: " + TMDb_MOVIE_POSTER_PATH);
            Picasso.with(this).load(TMDb_MOVIE_POSTER_PATH).into(moviePosterIV);
        }
        /*if (movieData.getMovieTitle() != null) {
            setTitle(movieData.getMovieTitle());
            movieTitleTV.setText(movieData.getMovieTitle());
        }*/
        if (movieData.getReleaseDate() != null) {
            String displayReleaseDate = releaseDateText + " " + movieData.getReleaseDate();
            movieReleaseDateTV.setText(displayReleaseDate);
        }
        if (movieData.getPlotSynopsis() != null) {
            movieSynopsisTV.setText(movieData.getPlotSynopsis());
        }
        if (movieData.getVoteAverage() != null) {
            String displayVoteAverage = voteAverageText + " " + movieData.getVoteAverage();
            movieVoteAverageTV.setText(displayVoteAverage);
        }
    }

    private void saveMovieAsFavorite() {
        movieName = movieData.getMovieTitle();
        moviePoster = movieData.getMoviePoster();
        movieReleaseDate = movieData.getReleaseDate();
        movieVoteAverage = movieData.getVoteAverage();

        ContentValues values = new ContentValues();
        values.put(MovieEntry.COLUMN_MOVIE_ID, movieID);
        values.put(MovieEntry.COLUMN_MOVIE_NAME, movieName);
        values.put(MovieEntry.COLUMN_MOVIE_POSTER, moviePoster);
        values.put(MovieEntry.COLUMN_MOVIE_RELEASE_DATE, movieReleaseDate);
        values.put(MovieEntry.COLUMN_MOVIE_VOTE_AVERAGE, movieVoteAverage);

        if (mCurrentMovieUri == null) {
            Uri newUri = getContentResolver().insert(MovieEntry.CONTENT_URI, values);

            if (newUri == null) {
                Toast.makeText(this, "Error while adding to favorites", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Movie added to favorites", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
