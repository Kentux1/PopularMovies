package com.kentux.popularmovies;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kentux.popularmovies.model.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Tiago Gomes on 13/03/2018.
 */

class MovieAdapter extends RecyclerView.Adapter<ViewHolder>  {

    private static final String LOG_TAG = MovieAdapter.class.getSimpleName();

    private List<Movie> movieList;

    public MovieAdapter(List<Movie> movieList) {
        this.movieList = movieList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.grid_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Context context = holder.moviePoster.getContext();
        String POSTER_PATH = "http://image.tmdb.org/t/p/w185/";
        Movie movie = movieList.get(position);
        Picasso.with(context).load(POSTER_PATH + movie.getMoviePoster()).into(holder.moviePoster);
        holder.movieTitle.setText(movie.getMovieTitle());
        holder.voteAverage.setText(movie.getVoteAverage());

    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    void clear() {
        final int size = movieList.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                movieList.remove(0);
            }

            notifyItemRangeRemoved(0, size);
        }
    }
    void swap(List<Movie> list) {
        if (movieList != null) {
            movieList.clear();
            movieList.addAll(list);
        }
        else {
            movieList = list;
        }
        notifyDataSetChanged();
    }
}
