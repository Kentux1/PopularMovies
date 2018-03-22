package com.kentux.popularmovies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.kentux.popularmovies.model.Movie;
import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {
    ImageView moviePoster;
    TextView movieTitle, movieReleaseDate, movieSynopsis, movieVoteAverage;
    Movie movieData;
    String TMDb_MOVIE_POSTER_PATH, releaseDateText, voteAverageText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        TMDb_MOVIE_POSTER_PATH = getString(R.string.TMDb_poster_path);

        releaseDateText = getString(R.string.release_date);

        voteAverageText = getString(R.string.vote_average);

        Intent intent = getIntent();

        movieData = intent.getExtras().getParcelable("movieData");

        movieTitle = findViewById(R.id.movie_title);

        moviePoster = findViewById(R.id.movie_image);

        movieReleaseDate = findViewById(R.id.movie_release_date);

        movieSynopsis = findViewById(R.id.movie_overview);

        movieVoteAverage = findViewById(R.id.movie_vote_average);

        polishUI();
    }

    private void polishUI() {
        if (movieData.getMoviePoster() != null) {
            Picasso.with(this).load(TMDb_MOVIE_POSTER_PATH + movieData.getMoviePoster()).into(moviePoster);
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
