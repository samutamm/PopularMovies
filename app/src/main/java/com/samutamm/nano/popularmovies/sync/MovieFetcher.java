package com.samutamm.nano.popularmovies.sync;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.samutamm.nano.popularmovies.BuildConfig;
import com.samutamm.nano.popularmovies.data.FavoriteContract;
import com.samutamm.nano.popularmovies.domain.Movie;
import com.samutamm.nano.popularmovies.sync.Parser;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class MovieFetcher {
    private OnFetchCompleted mListener;

    private Parser parser;
    private Context mContext;


    public MovieFetcher(Context context, OnFetchCompleted listener) {
        mListener = listener;
        parser = new Parser();
        mContext = context;
    }

    public void fetchAPI(String criteria) {
        final RequestQueue queue = Volley.newRequestQueue(mContext);
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

    public void fetchFavorites() {
        Observable.fromCallable(new Callable<List<Movie>>() {
            @Override
            public List<Movie> call() throws Exception {
                return readFromDatabase();
            }
        }).subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Observer<List<Movie>>() {
            @Override
            public void onCompleted() {

            }
            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }
            @Override
            public void onNext(List<Movie> movies) {
                mListener.onFetchCompleted(movies);
            }
        });
    }

    private static final String[] MOVIE_COLUMNS = {
            FavoriteContract.FavoriteEntry.COLUMN_MOVIE_ID,
            FavoriteContract.FavoriteEntry.COLUMN_ORIGINAL_TITLE,
            FavoriteContract.FavoriteEntry.COLUMN_OVERVIEW,
            FavoriteContract.FavoriteEntry.COLUMN_RELEASE_DATE,
            FavoriteContract.FavoriteEntry.COLUMN_AVERAGE_RATE,
            FavoriteContract.FavoriteEntry.COLUMN_POSTER
    };

    static final int COL_FAVORITE_MOVIE_ID = 0;
    static final int COL_FAVORITE_TITLE = 1;
    static final int COL_FAVORITE_OVERVIEW = 2;
    static final int COL_FAVORITE_RELEASE_DATE = 3;
    static final int COL_FAVORITE_AVERAGE_RATE = 4;
    static final int COL_FAVORITE_POSTER = 5;


    private List<Movie> readFromDatabase() {
        final Cursor cursor = mContext.getContentResolver().query(
                FavoriteContract.FavoriteEntry.CONTENT_URI,
                MOVIE_COLUMNS,
                null,
                null,
                null
        );
        List<Movie> results = new ArrayList<>();
        if (cursor == null || !cursor.moveToFirst()) {
            return results;
        }
        do {
            Movie movie = new Movie();
            movie.setId(cursor.getString(COL_FAVORITE_MOVIE_ID));
            movie.setOriginal_title(cursor.getString(COL_FAVORITE_TITLE));
            movie.setOverview(cursor.getString(COL_FAVORITE_OVERVIEW));
            movie.setRelease_date(cursor.getString(COL_FAVORITE_RELEASE_DATE));
            movie.setVote_average(cursor.getString(COL_FAVORITE_AVERAGE_RATE));
            movie.setPoster(cursor.getBlob(COL_FAVORITE_POSTER));
            results.add(movie);
        }while (cursor.moveToNext());
        return results;
    }

    public interface OnFetchCompleted {
        void onFetchCompleted(List<Movie> results);
    }
}