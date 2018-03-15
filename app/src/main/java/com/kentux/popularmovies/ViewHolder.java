package com.kentux.popularmovies;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Tiago Gomes on 13/03/2018.
 */

public class ViewHolder extends RecyclerView.ViewHolder {
    public ImageView moviePoster;
    public TextView movieTitle, voteAverage;
    public ViewHolder(View view) {
        super(view);
        moviePoster = (ImageView) view.findViewById(R.id.movie_card);
        movieTitle = (TextView) view.findViewById(R.id.movie_card_title);
        voteAverage = (TextView) view.findViewById(R.id.movie_card_vote_average);
    }
}
