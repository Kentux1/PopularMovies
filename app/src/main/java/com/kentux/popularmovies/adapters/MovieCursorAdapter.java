package com.kentux.popularmovies.adapters;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kentux.popularmovies.R;
import com.kentux.popularmovies.data.MovieContract.MovieEntry;
import com.squareup.picasso.Picasso;

/**
 * Created by Tiago Gomes on 13/04/2018.
 */

public class MovieCursorAdapter extends CursorAdapter {

    public MovieCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.favorite_list_item, viewGroup, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        Context mContext = context.getApplicationContext();
        ImageView movieImageView;
        TextView movieNameTV, movieReleaseDateTV, movieVoteAverageTV, movieSynopsisTV;

        movieImageView = view.findViewById(R.id.favorite_movie_image);
        movieNameTV = view.findViewById(R.id.favorite_movie_title);
        movieReleaseDateTV = view.findViewById(R.id.favorite_movie_release_date);
        movieVoteAverageTV = view.findViewById(R.id.favorite_movie_vote_average);

        int imageColumnIndex = cursor.getColumnIndex(MovieEntry.COLUMN_MOVIE_POSTER);
        int nameColumnIndex = cursor.getColumnIndex(MovieEntry.COLUMN_MOVIE_NAME);
        int releaseDateColumnIndex = cursor.getColumnIndex(MovieEntry.COLUMN_MOVIE_RELEASE_DATE);
        int voteAverageColumnIndex = cursor.getColumnIndex(MovieEntry.COLUMN_MOVIE_VOTE_AVERAGE);

        final String moviePosterPath = cursor.getString(imageColumnIndex);
        Uri.Builder builder = new Uri.Builder();

        builder.scheme(mContext.getString(R.string.scheme))
                .authority(mContext.getString(R.string.TMDB_poster_authority))
                .appendPath(mContext.getString(R.string.TMDB_poster_t))
                .appendPath(mContext.getString(R.string.TMDB_poster_p))
                .appendPath(mContext.getString(R.string.TMDB_poster_w185))
                .appendEncodedPath(moviePosterPath);
        final String TMDb_MOVIE_POSTER_PATH = builder.build().toString();
        final String movieName = cursor.getString(nameColumnIndex);
        final String movieReleaseDate = "Release date: " + cursor.getString(releaseDateColumnIndex);
        final String movieVoteAverage = "Average votes: " + cursor.getString(voteAverageColumnIndex);

        Picasso.with(mContext).load(TMDb_MOVIE_POSTER_PATH).into(movieImageView);
        movieNameTV.setText(movieName);
        movieReleaseDateTV.setText(movieReleaseDate);
        movieVoteAverageTV.setText(movieVoteAverage);
    }
}
