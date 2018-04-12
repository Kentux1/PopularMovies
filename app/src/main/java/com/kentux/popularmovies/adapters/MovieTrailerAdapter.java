package com.kentux.popularmovies.adapters;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kentux.popularmovies.R;
import com.kentux.popularmovies.interfaces.ItemClickListener;
import com.kentux.popularmovies.model.MovieTrailer;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Tiago Gomes on 11/04/2018.
 */

public class MovieTrailerAdapter extends RecyclerView.Adapter<TrailerViewHolder> {
    private static final String LOG_TAG = MovieTrailerAdapter.class.getSimpleName();
    private Context context;
    private List<MovieTrailer> trailerList;

    public MovieTrailerAdapter(List<MovieTrailer> trailerList) {
        this.trailerList = trailerList;
    }

    @NonNull
    @Override
    public TrailerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.detail_trailer_item, parent, false);
        return new TrailerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TrailerViewHolder holder, int position) {
        context = holder.trailerThumbnail.getContext();
        final MovieTrailer movieTrailer = trailerList.get(position);
        Uri.Builder builder = new Uri.Builder();
        builder.scheme(context.getString(R.string.scheme))
                .authority(context.getString(R.string.yt_thumb_authority))
                .appendPath(context.getString(R.string.vi_path))
                .appendPath(movieTrailer.getTrailerKey())
                .appendPath(context.getString(R.string.thumb_path));
        final String TRAILER_THUMBNAIL_PATH = builder.build().toString();

        Picasso.with(context).load(TRAILER_THUMBNAIL_PATH).fit().centerCrop().into(holder.trailerThumbnail);
        holder.trailerName.setText(movieTrailer.getTrailerName());
        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                Log.v(LOG_TAG, "Trailer clicked: " + TRAILER_THUMBNAIL_PATH + " / " + movieTrailer.getTrailerName());
                Uri.Builder builder = new Uri.Builder();
                builder.scheme(context.getString(R.string.scheme))
                        .authority(context.getString(R.string.yt_video_authority))
                        .appendPath(context.getString(R.string.watch_path))
                        .appendQueryParameter(context.getString(R.string.id_query), movieTrailer.getTrailerKey());
                final String TRAILER_VIDEO_PATH = builder.build().toString();

                Intent youtubeAppIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + movieTrailer.getTrailerKey()));
                Intent youtubeWebIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(TRAILER_VIDEO_PATH));
                try {
                    context.startActivity(youtubeAppIntent);
                } catch (ActivityNotFoundException ex) {
                    context.startActivity(youtubeWebIntent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return trailerList.size();
    }

    public void clear() {
        final int size = trailerList.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                trailerList.remove(0);
            }

            notifyItemRangeRemoved(0, size);
        }
    }

    public void swap(List<MovieTrailer> list) {
        if (trailerList != null) {
            trailerList.clear();
            trailerList.addAll(list);
        } else {
            trailerList = list;
        }
        notifyDataSetChanged();
    }
}

class TrailerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    ImageView trailerThumbnail;
    TextView trailerName;

    private ItemClickListener itemClickListener;

    public TrailerViewHolder(View itemView) {
        super(itemView);
        trailerThumbnail = itemView.findViewById(R.id.trailer_thumbnail);
        trailerName = itemView.findViewById(R.id.trailer_name);
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