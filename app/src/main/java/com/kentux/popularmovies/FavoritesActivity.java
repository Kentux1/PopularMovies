package com.kentux.popularmovies;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.kentux.popularmovies.adapters.MovieCursorAdapter;
import com.kentux.popularmovies.data.MovieContract;
import com.kentux.popularmovies.data.MovieContract.MovieEntry;

public class FavoritesActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final String LOG_TAG = FavoritesActivity.class.getSimpleName();
    private static final int FAVORITES_LOADER = 0;
    MovieCursorAdapter mCursorAdapter;
    Uri mCurrentFavoriteUri;
    Toolbar toolbar;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.favorites_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.delete_all_favorites) {
            showFavoritesDeleteConfirmationDialog();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);
        toolbar = findViewById(R.id.favorite_toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        this.setTitle(getString(R.string.favorites_activity));

        ListView favoritesListView = findViewById(R.id.list);

        View emptyView = findViewById(R.id.empty_text_view);
        favoritesListView.setEmptyView(emptyView);

        mCursorAdapter = new MovieCursorAdapter(this, null);
        favoritesListView.setAdapter(mCursorAdapter);


        favoritesListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long id) {
                mCurrentFavoriteUri = ContentUris.withAppendedId(MovieContract.MovieEntry.CONTENT_URI, id);
                showFavoriteDeleteConfirmationDialog();
                return true;
            }
        });

        getLoaderManager().initLoader(FAVORITES_LOADER, null, this);
    }

    private void deleteFavorite() {
        if (mCurrentFavoriteUri != null) {
            int rowsDeleted = getContentResolver().delete(mCurrentFavoriteUri, null, null);
            if (rowsDeleted == 0) {
                Toast.makeText(this, "Couldn't delete favorite movie.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Favorite movie deleted.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void deleteAllFavorites() {
        int rowsDeleted = getContentResolver().delete(MovieEntry.CONTENT_URI, null, null);
        Log.v(LOG_TAG, rowsDeleted + " rows deleted from favorites database");
    }

    private void showFavoriteDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you want to remove this movie as a favorite?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteFavorite();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void showFavoritesDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you want to remove all favorite movies?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteAllFavorites();
                Toast.makeText(FavoritesActivity.this, "All movies have been removed.", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
        switch (id) {
            case FAVORITES_LOADER:
                String[] projection = {
                        MovieEntry._ID,
                        MovieEntry.COLUMN_MOVIE_ID,
                        MovieEntry.COLUMN_MOVIE_NAME,
                        MovieEntry.COLUMN_MOVIE_POSTER,
                        MovieEntry.COLUMN_MOVIE_RELEASE_DATE,
                        MovieEntry.COLUMN_MOVIE_VOTE_AVERAGE};
                String sortOrder =
                        MovieEntry._ID + " DESC ";
                return new CursorLoader(this,
                        MovieEntry.CONTENT_URI,
                        projection,
                        null,
                        null,
                        sortOrder);
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        try {
            mCursorAdapter.swapCursor(cursor);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null);
    }


}
