package com.samutamm.nano.popularmovies.data;

import android.content.Context;
import android.database.Cursor;

import com.samutamm.nano.popularmovies.domain.Movie;
import com.samutamm.nano.popularmovies.helpers.OnMovieFetchCompleted;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class DatabaseFetcher {
    private Context mContext;
    private OnMovieFetchCompleted mListener;

    public DatabaseFetcher(Context mContext, OnMovieFetchCompleted mListener) {
        this.mContext = mContext;
        this.mListener = mListener;
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
                    public void onCompleted() {}
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
}
