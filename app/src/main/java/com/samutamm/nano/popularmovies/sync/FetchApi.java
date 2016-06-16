package com.samutamm.nano.popularmovies.sync;

import android.content.Context;
import android.net.Uri;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.samutamm.nano.popularmovies.BuildConfig;
import com.samutamm.nano.popularmovies.domain.Movie;
import com.samutamm.nano.popularmovies.sync.Parser;

import java.util.List;

public class FetchApi{
    private OnFetchCompleted mListener;

    private RequestQueue queue;
    private Parser parser;
    private Context mContext;


    public FetchApi(Context context, OnFetchCompleted listener) {
        mListener = listener;
        queue = Volley.newRequestQueue(context);
        parser = new Parser();
        mContext = context;
    }

    public void fetch(String criteria) {
        String url = getUrl(criteria);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String json) {
                        List<Movie> movies = parser.parseJson(json);
                        mListener.onFetchCompleted(movies);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(
                        mContext,
                        "Please check your internet connection! Cannot load the movies.",
                        Toast.LENGTH_LONG
                ).show();
            }
        });
        queue.add(stringRequest);
    }

    public String getUrl(String urlCriteria) {
        final String MOVIE_BASEURL =
                "http://api.themoviedb.org/3/movie/" + urlCriteria + "?";
        final String API_KEY = "api_key";

        Uri builtUri = Uri.parse(MOVIE_BASEURL).buildUpon()
                .appendQueryParameter(API_KEY, BuildConfig.MOVIE_DATABASE_API_KEY)
                .build();

        return builtUri.toString();
    }

    public interface OnFetchCompleted {
        void onFetchCompleted(List<Movie> results);
    }
}