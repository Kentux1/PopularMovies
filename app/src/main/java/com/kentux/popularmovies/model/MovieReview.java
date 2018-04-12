package com.kentux.popularmovies.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Tiago Gomes on 05/04/2018.
 */

public class MovieReview implements Parcelable {

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<MovieReview> CREATOR = new Parcelable.Creator<MovieReview>() {
        @Override
        public MovieReview createFromParcel(Parcel in) {
            return new MovieReview(in);
        }

        @Override
        public MovieReview[] newArray(int size) {
            return new MovieReview[size];
        }
    };
    private String reviewAuthor;
    private String reviewURL;
    private String reviewContent;

    public MovieReview(String reviewAuthor, String reviewURL, String reviewContent) {
        this.reviewAuthor = reviewAuthor;
        this.reviewURL = reviewURL;
        this.reviewContent = reviewContent;
    }

    protected MovieReview(Parcel in) {
        reviewAuthor = in.readString();
        reviewURL = in.readString();
        reviewContent = in.readString();
    }

    public String getReviewAuthor() {
        return reviewAuthor;
    }

    public void setReviewAuthor(String reviewAuthor) {
        this.reviewAuthor = reviewAuthor;
    }

    public String getReviewURL() {
        return reviewURL;
    }

    public void setReviewURL(String reviewURL) {
        this.reviewURL = reviewURL;
    }

    public String getReviewContent() {
        return reviewContent;
    }

    public void setReviewContent(String reviewContent) {
        this.reviewContent = reviewContent;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(reviewAuthor);
        dest.writeString(reviewURL);
        dest.writeString(reviewContent);
    }
}
