package com.kentux.popularmovies.utils;

import android.text.TextUtils;
import android.util.Log;

import com.kentux.popularmovies.model.Movie;

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
    private static final String KEY_TITLE = "title";
    private static final String KEY_RELEASE_DATE = "release_date";
    private static final String KEY_MOVIE_POSTER = "poster_path";
    private static final String KEY_VOTE_AVERAGE = "vote_average";
    private static final String KEY_SYNOPSIS = "overview";

    public QueryUtils() {
    }

    public static List<Movie> fetchMovieData(String requestURL) {
        URL url = createURL(requestURL);
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error closing input stream", e);
        }
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return extractMovieFeaturesFromJson(jsonResponse);
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
                    String movieTitle = result.optString(KEY_TITLE);
                    String releaseDate = result.optString(KEY_RELEASE_DATE);
                    String moviePoster = result.optString(KEY_MOVIE_POSTER);
                    String voteAverage = result.optString(KEY_VOTE_AVERAGE);
                    String movieSynopsis = result.optString(KEY_SYNOPSIS);

                    movies.add(new Movie(movieTitle, releaseDate, moviePoster, voteAverage, movieSynopsis));
                }
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing the movies JSON results", e);
            e.printStackTrace();
        }
        return movies;
    }
}
