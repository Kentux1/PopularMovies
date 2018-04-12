package com.kentux.popularmovies.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Tiago Gomes on 05/04/2018.
 */

public class MovieTrailer implements Parcelable {

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<MovieTrailer> CREATOR =
            new Parcelable.Creator<MovieTrailer>() {
                @Override
                public MovieTrailer createFromParcel(Parcel in) {
                    return new MovieTrailer(in);
                }

                @Override
                public MovieTrailer[] newArray(int i) {
                    return new MovieTrailer[i];
                }
            };
    private String trailerKey;
    private String trailerName;

    public MovieTrailer(String trailerKey, String trailerName) {
        this.trailerKey = trailerKey;
        this.trailerName = trailerName;
    }

    protected MovieTrailer(Parcel in) {
        trailerKey = in.readString();
        trailerName = in.readString();
    }

    public String getTrailerKey() {
        return trailerKey;
    }

    public void setMovieKey(String trailerKey) {
        this.trailerKey = trailerKey;
    }

    public String getTrailerName() {
        return trailerName;
    }

    public void setTrailerName(String trailerName) {
        this.trailerName = trailerName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(trailerKey);
        parcel.writeString(trailerName);
    }
}
