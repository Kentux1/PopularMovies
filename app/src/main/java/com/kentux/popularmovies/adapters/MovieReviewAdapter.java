package com.kentux.popularmovies.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kentux.popularmovies.R;
import com.kentux.popularmovies.model.MovieReview;

import java.util.List;

/**
 * Created by Tiago Gomes on 11/04/2018.
 */

public class MovieReviewAdapter extends RecyclerView.Adapter<ReviewViewHolder> {
    private Context context;
    private List<MovieReview> reviewList;

    public MovieReviewAdapter(List<MovieReview> reviewList) {
        this.reviewList = reviewList;
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.detail_review_item, parent, false);
        return new ReviewViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        final MovieReview movieReview = reviewList.get(position);
        holder.reviewAuthor.setText(movieReview.getReviewAuthor());
        holder.reviewContent.setText(movieReview.getReviewContent());
    }

    @Override
    public int getItemCount() {
        return reviewList.size();
    }

    public void clear() {
        final int size = reviewList.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                reviewList.remove(0);
            }

            notifyItemRangeRemoved(0, size);
        }
    }

    public void swap(List<MovieReview> list) {
        if (reviewList != null) {
            reviewList.clear();
            reviewList.addAll(list);
        } else {
            reviewList = list;
        }
        notifyDataSetChanged();
    }
}

class ReviewViewHolder extends RecyclerView.ViewHolder {
    TextView reviewAuthor, reviewContent;

    public ReviewViewHolder(View itemView) {
        super(itemView);
        reviewAuthor = itemView.findViewById(R.id.review_author);
        reviewContent = itemView.findViewById(R.id.review_content);
    }
}
