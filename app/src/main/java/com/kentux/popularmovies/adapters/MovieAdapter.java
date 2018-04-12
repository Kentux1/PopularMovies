package com.kentux.popularmovies.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kentux.popularmovies.DetailActivity;
import com.kentux.popularmovies.R;
import com.kentux.popularmovies.interfaces.ItemClickListener;
import com.kentux.popularmovies.model.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

//import com.kentux.popularmovies.DetailActivity;

/**
 * Created by Tiago Gomes on 13/03/2018.
 */


public class MovieAdapter extends RecyclerView.Adapter<RecyclerViewHolder> {

    private static final String LOG_TAG = MovieAdapter.class.getSimpleName();
    private Context context;
    private List<Movie> movieList;



    public MovieAdapter(List<Movie> movieList) {
        this.movieList = movieList;
    }


    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.grid_item, parent, false);
        return new RecyclerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        context = holder.moviePoster.getContext();
        String MOVIE_POSTER_PATH = context.getResources().getString(R.string.TMDb_poster_path);
        final Movie movie = movieList.get(position);
        Picasso.with(context).load(MOVIE_POSTER_PATH + movie.getMoviePoster()).fit().centerCrop().into(holder.moviePoster);
        holder.movieTitle.setText(movie.getMovieTitle());
        holder.voteAverage.setText(movie.getVoteAverage());
        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                Log.v(LOG_TAG, "Item clicked: " + movieList.get(position));
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("movieData", movie);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public void clear() {
        final int size = movieList.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                movieList.remove(0);
            }

            notifyItemRangeRemoved(0, size);
        }
    }

    public void swap(List<Movie> list) {
        if (movieList != null) {
            movieList.clear();
            movieList.addAll(list);
        } else {
            movieList = list;
        }
        notifyDataSetChanged();
    }
}

class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    ImageView moviePoster;
    TextView movieTitle, voteAverage;
    private CardView movieCardView;
    private Context context;

    private ItemClickListener itemClickListener;

    public RecyclerViewHolder(View itemView) {
        super(itemView);
        movieCardView = itemView.findViewById(R.id.movie_card_view);
        moviePoster = itemView.findViewById(R.id.movie_card);
        movieTitle = itemView.findViewById(R.id.movie_card_title);
        voteAverage = itemView.findViewById(R.id.movie_card_vote_average);
        itemView.setOnClickListener(this);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view, getAdapterPosition());
    }
}