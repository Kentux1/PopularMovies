package com.kentux.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.kentux.popularmovies.data.MovieContract.MovieEntry;

/**
 * Created by Tiago Gomes on 13/04/2018.
 */

public class MovieDbHelper extends SQLiteOpenHelper {

    private static final String SQL_CREATE_MOVIES_TABLE = "CREATE TABLE "
            + MovieEntry.TABLE_NAME + " ("
            + MovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + MovieEntry.COLUMN_MOVIE_ID + " TEXT NOT NULL UNIQUE, "
            + MovieEntry.COLUMN_MOVIE_NAME + " TEXT NOT NULL, "
            + MovieEntry.COLUMN_MOVIE_POSTER + " TEXT NOT NULL, "
            + MovieEntry.COLUMN_MOVIE_RELEASE_DATE + " TEXT NOT NULL, "
            + MovieEntry.COLUMN_MOVIE_VOTE_AVERAGE + " TEXT NOT NULL);";

    private static final String DATABASE_NAME = "favorites.db";

    private static final int DATABASE_VERSION = 1;

    MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_MOVIES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
