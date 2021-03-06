package com.samutamm.nano.popularmovies.sync;

import android.content.Context;
import android.net.Uri;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.samutamm.nano.popularmovies.BuildConfig;
import com.samutamm.nano.popularmovies.R;
import com.samutamm.nano.popularmovies.domain.Comment;
import com.samutamm.nano.popularmovies.domain.Movie;
import com.samutamm.nano.popularmovies.domain.Trailer;
import com.samutamm.nano.popularmovies.helpers.MovieViewHolder;
import com.samutamm.nano.popularmovies.interfaces.OnCommentsFetchCompleted;
import com.samutamm.nano.popularmovies.interfaces.OnMovieFetchCompleted;
import com.samutamm.nano.popularmovies.interfaces.OnTrailerFetchCompleted;

import java.util.List;

public class APIFetcher {

    private Parser parser;
    private Context mContext;


    public APIFetcher(Context context) {
        parser = new Parser();
        mContext = context;
    }

    private void fetch(String url, Response.Listener<String> success, Response.ErrorListener fail) {
        final RequestQueue queue = Volley.newRequestQueue(mContext);
        System.out.println(url);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, success, fail);
        queue.add(stringRequest);
    }

    public void fetchMovies(String criteria, final OnMovieFetchCompleted listener) {
        String url = getUrl(URLContract.BASE_URL + criteria + "?");
        fetch(url,(json)-> {
                    List<Movie> movies = parser.parseMovies(json);
                    listener.onFetchCompleted(movies);
                }, (error) -> {
                    error.printStackTrace();
                    String message = mContext.getString(R.string.movies_nointernet);
                    Toast.makeText(mContext, message, Toast.LENGTH_LONG
                    ).show();
                }
        );
    }

    public void fetchTrailers(Movie movie,
                              final MovieViewHolder holder,
                              final OnTrailerFetchCompleted mListener) {

        String url = getUrl(URLContract.BASE_URL + movie.getId() + URLContract.VIDEOS +"?");
        fetch(url, (json) -> {
                    List<Trailer> trailers = parser.parseTrailers(json);
                    mListener.onTrailers(trailers, holder);
                }, (error) -> error.printStackTrace());
    }

    public void fetchComments(Movie movie,
                              final MovieViewHolder holder,
                              final OnCommentsFetchCompleted mListener) {
        String url = getUrl(URLContract.BASE_URL + movie.getId() + URLContract.REVIEWS +"?");
        fetch(url, (json)-> {
                        List<Comment> comments = parser.parseComments(json);
                        mListener.onComments(comments, holder);
                }, (error -> error.printStackTrace()));
    }

    public String getUrl(String baseurl) {;;
        Uri builtUri = Uri.parse(baseurl).buildUpon()
                .appendQueryParameter(URLContract.API_KEY, BuildConfig.MOVIE_DATABASE_API_KEY)
                .build();

        return builtUri.toString();
    }

    public class URLContract {
        public static final String BASE_URL = "http://api.themoviedb.org/3/movie/";
        public static final String VIDEOS = "/videos";
        public static final String REVIEWS = "/reviews";

        public static final String API_KEY = "api_key";
    }
}