package com.kentux.popularmovies.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Tiago Gomes on 13/04/2018.
 */

public class MovieContract {
    static final String CONTENT_AUTHORITY = "com.kentux.popularmovies";
    static final String PATH_MOVIES = "movies";
    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    private MovieContract() {

    }

    public static abstract class MovieEntry implements BaseColumns {
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_MOVIES);
        public static final String TABLE_NAME = "favoriteMovies";
        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_MOVIE_ID = "movieId";
        public static final String COLUMN_MOVIE_NAME = "movieName";
        public static final String COLUMN_MOVIE_POSTER = "moviePoster";
        public static final String COLUMN_MOVIE_RELEASE_DATE = "releaseDate";
        public static final String COLUMN_MOVIE_VOTE_AVERAGE = "voteAverage";
        public static final String COLUMN_MOVIE_SYNOPSIS = "movieSynopsis";
        /**
         * The MIME type of the {@Link #CONTENT_URI} for a list of movies.
         */
        static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIES;
        /**
         * The MIME type of the {@Link #CONTENT_URI} for a single movie.
         */
        static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIES;
    }
}
