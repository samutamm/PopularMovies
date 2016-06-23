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
import com.samutamm.nano.popularmovies.domain.Trailer;
import com.samutamm.nano.popularmovies.helpers.MovieViewHolder;
import com.samutamm.nano.popularmovies.helpers.OnMovieFetchCompleted;
import com.samutamm.nano.popularmovies.helpers.OnTrailerFetchCompleted;

import java.util.List;

public class APIFetcher {

    private Parser parser;
    private Context mContext;


    public APIFetcher(Context context) {
        parser = new Parser();
        mContext = context;
    }

    public void fetchMovies(String criteria, final OnMovieFetchCompleted listener) {
        final RequestQueue queue = Volley.newRequestQueue(mContext);
        String url = getUrl(URLContract.BASE_URL + criteria + "?");
        System.out.println(url);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String json) {
                        List<Movie> movies = parser.parseMovies(json);
                        listener.onFetchCompleted(movies);
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

    public String getUrl(String baseurl) {;
        final String API_KEY = "api_key";
        Uri builtUri = Uri.parse(baseurl).buildUpon()
                .appendQueryParameter(API_KEY, BuildConfig.MOVIE_DATABASE_API_KEY)
                .build();

        return builtUri.toString();
    }

    public void fetchTrailers(Movie movie,
                              final MovieViewHolder holder,
                              final OnTrailerFetchCompleted mListener) {
        final RequestQueue queue = Volley.newRequestQueue(mContext);
        String url = getUrl(URLContract.BASE_URL + movie.getId() + "/videos?");
        System.out.println(url);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String json) {
                        List<Trailer> trailers = parser.parseTrailers(json);
                        mListener.onFetchCompleted(trailers, holder);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        queue.add(stringRequest);
    }

    public class URLContract {
        public static final String BASE_URL = "http://api.themoviedb.org/3/movie/";
    }
}