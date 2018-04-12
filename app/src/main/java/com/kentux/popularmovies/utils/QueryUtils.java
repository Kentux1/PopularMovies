package com.kentux.popularmovies.utils;

import android.text.TextUtils;
import android.util.Log;

import com.kentux.popularmovies.model.Movie;
import com.kentux.popularmovies.model.MovieReview;
import com.kentux.popularmovies.model.MovieTrailer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tiago Gomes on 07/03/2018.
 */

public class QueryUtils {

    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    private static final String ARRAY_RESULTS = "results";

    //Movies JSON keys
    private static final String KEY_ID = "id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_RELEASE_DATE = "release_date";
    private static final String KEY_MOVIE_POSTER = "poster_path";
    private static final String KEY_VOTE_AVERAGE = "vote_average";
    private static final String KEY_SYNOPSIS = "overview";

    //Trailers JSON keys
    private static final String KEY_YOUTUBE_KEY = "key";
    private static final String KEY_NAME = "name";
    private static final String KEY_SITE = "site";

    //Reviews JSON keys
    private static final String KEY_AUTHOR = "author";
    private static final String KEY_URL = "url";
    private static final String KEY_CONTENT = "content";

    public QueryUtils() {
    }

    //This method fetches movie data
    public static List<Movie> fetchMovieData(String requestURL) {
        URL url = createURL(requestURL);
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error closing movie data input stream", e);
        }
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return extractMovieFeaturesFromJson(jsonResponse);
    }

    //This method fetches trailers data from the selected movie using its ID
    public static List<MovieTrailer> fetchTrailerData(String requestURL) {
        URL url = createURL(requestURL);
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error closing trailer data input stream", e);
        }
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return extractTrailerFeaturesFromJson(jsonResponse);
    }

    //This method fetches reviews data from the selected movie using its ID
    public static List<MovieReview> fetchReviewData(String requestURL) {
        URL url = createURL(requestURL);
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error closing review data input stream", e);
        }
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return extractReviewFeaturesFromJson(jsonResponse);
    }

    private static URL createURL(String stringURL) {
        URL url = null;
        try {
            url = new URL(stringURL);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error while creating the URL.", e);
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";
        if (url == null) {
            return jsonResponse;
        }
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(8000);
            urlConnection.setConnectTimeout(12000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the movies JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    private static List<Movie> extractMovieFeaturesFromJson(String moviesJSON) {
        if (TextUtils.isEmpty(moviesJSON)) {
            return null;
        }
        List<Movie> movies = new ArrayList<>();

        try {
            JSONObject baseJsonResponse = new JSONObject(moviesJSON);
            JSONArray moviesArray = baseJsonResponse.optJSONArray(ARRAY_RESULTS);
            if (baseJsonResponse.has(ARRAY_RESULTS)) {
                for (int i = 0; i < moviesArray.length(); i++) {
                    JSONObject result = moviesArray.optJSONObject(i);
                    String movieID = result.optString(KEY_ID);
                    String movieTitle = result.optString(KEY_TITLE);
                    String releaseDate = result.optString(KEY_RELEASE_DATE);
                    String moviePoster = result.optString(KEY_MOVIE_POSTER);
                    String voteAverage = result.optString(KEY_VOTE_AVERAGE);
                    String movieSynopsis = result.optString(KEY_SYNOPSIS);

                    movies.add(new Movie(movieID, movieTitle, releaseDate, moviePoster, voteAverage, movieSynopsis));
                }
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing the movies JSON results", e);
            e.printStackTrace();
        }
        return movies;
    }

    private static List<MovieTrailer> extractTrailerFeaturesFromJson(String trailersJSON) {
        if (TextUtils.isEmpty(trailersJSON)) {
            return null;
        }
        List<MovieTrailer> trailers = new ArrayList<>();

        try {
            JSONObject baseJsonResponse = new JSONObject(trailersJSON);
            JSONArray trailersArray = baseJsonResponse.optJSONArray(ARRAY_RESULTS);
            if (baseJsonResponse.has(ARRAY_RESULTS)) {
                for (int i = 0; i < trailersArray.length(); i++) {
                    JSONObject result = trailersArray.optJSONObject(i);
                    String trailerYoutubeKey = result.optString(KEY_YOUTUBE_KEY);
                    String trailerName = result.optString(KEY_NAME);

                    trailers.add(new MovieTrailer(trailerYoutubeKey, trailerName));
                }
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing the trailers JSON results", e);
            e.printStackTrace();
        }
        return trailers;
    }

    private static List<MovieReview> extractReviewFeaturesFromJson(String reviewsJSON) {
        if (TextUtils.isEmpty(reviewsJSON)) {
            return null;
        }
        List<MovieReview> reviews = new ArrayList<>();

        try {
            JSONObject baseJsonResponse = new JSONObject(reviewsJSON);
            JSONArray reviewsArray = baseJsonResponse.optJSONArray(ARRAY_RESULTS);
            if (baseJsonResponse.has(ARRAY_RESULTS)) {
                for (int i = 0; i < reviewsArray.length(); i++) {
                    JSONObject result = reviewsArray.optJSONObject(i);
                    String reviewAuthor = result.optString(KEY_AUTHOR);
                    String reviewURL = result.optString(KEY_URL);
                    String reviewContent = result.optString(KEY_CONTENT);

                    reviews.add(new MovieReview(reviewAuthor, reviewURL, reviewContent));
                }
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing the reviews JSON results", e);
            e.printStackTrace();
        }
        return reviews;
    }
}
