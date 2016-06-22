package com.samutamm.nano.popularmovies.fragments;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.jakewharton.rxbinding.view.RxView;
import com.samutamm.nano.popularmovies.R;
import com.samutamm.nano.popularmovies.data.FavoriteContract;
import com.samutamm.nano.popularmovies.helpers.MovieViewHolder;
import com.samutamm.nano.popularmovies.helpers.Utility;
import com.samutamm.nano.popularmovies.activities.MovieActivity;
import com.samutamm.nano.popularmovies.domain.Movie;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.ByteArrayOutputStream;
import java.util.concurrent.Callable;

import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

import static com.samutamm.nano.popularmovies.data.FavoriteContract.FavoriteEntry.*;

public class MovieFragment extends Fragment {

    private boolean imageDownloaded = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.movie_fragment, container, false);
        final MovieViewHolder movieViewHolder = new MovieViewHolder(rootView);

        final Bundle args = getArguments();
        if (args != null) {
            Movie movie = (Movie) args.get(MovieActivity.MOVIE_TAG);
            movieViewHolder.originalTitle.setText(movie.getOriginalTitle());
            movieViewHolder.movieYear.setText(Utility.parseYear(movie.getReleaseDate()));
            movieViewHolder.averageVote.setText(movie.getVoteAverage() + "/10.0");
            movieViewHolder.synopsis.setText(movie.getOverview());
            addFavoriteButton(rootView, movieViewHolder, movie);

            if (movie.getPoster().length == 1) {
                loadImageFromInternet(rootView, movieViewHolder, movie);
            } else {
                final byte[] poster = movie.getPoster();
                Bitmap bitmap = BitmapFactory.decodeByteArray(poster, 0, poster.length);
                movieViewHolder.thumbnail.setImageBitmap(bitmap);
            }
        }
        return rootView;
    }

    private void addFavoriteButton(final View rootView, final MovieViewHolder viewHolder, final Movie movie) {
        Observable.fromCallable(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return checkIfMovieIsFavorite(rootView, movie);
            }
        }).subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Observer<Boolean>() {
            @Override
            public void onCompleted() {}
            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }
            @Override
            public void onNext(Boolean existsInDatabase) {
                if(existsInDatabase) {
                    setFavoriteButtonText(
                            viewHolder, rootView.getContext(), R.string.favoriteButton_checked
                    );
                    RxView.clicks(viewHolder.markFavoriteButton).subscribe(
                            removeMovieFromLocalDB(rootView.getContext(), movie, viewHolder)
                    );
                } else {
                    RxView.clicks(viewHolder.markFavoriteButton).subscribe(
                            saveMovieToLocalDB(rootView.getContext(), movie, viewHolder)
                    );
                }
            }
        });
    }

    private void setFavoriteButtonText(MovieViewHolder movieViewHolder, Context context, int id) {
        movieViewHolder.markFavoriteButton.setText(
                context.getResources().getString(id)
        );
    }


    private boolean checkIfMovieIsFavorite(View rootView, Movie movie) {
        final ContentResolver resolver = rootView.getContext().getContentResolver();
        final Cursor query = resolver.query(
                FavoriteContract.FavoriteEntry.CONTENT_URI,
                null,
                FavoriteContract.FavoriteEntry.COLUMN_MOVIE_ID + " = ?",
                new String[]{movie.getId()},
                null
        );
        final boolean exists = query != null && query.moveToFirst();
        query.close();
        return exists;
    }

    private void loadImageFromInternet(View rootView, final MovieViewHolder movieViewHolder, Movie movie) {
        final String thumbnailUrl = Utility.getMovieUrl(movie, "w92");
        Picasso.with(rootView.getContext())
                .load(thumbnailUrl)
                .into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        movieViewHolder.thumbnail.setImageBitmap(bitmap);
                        imageDownloaded = true;
                    }
                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {}
                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {}
                });
    }

    private Action1<Void> removeMovieFromLocalDB(
            final Context context, final Movie movie, final MovieViewHolder viewHolder) {
        return new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                context.getContentResolver().delete(
                        FavoriteContract.FavoriteEntry.CONTENT_URI,
                        FavoriteContract.FavoriteEntry.COLUMN_MOVIE_ID + " = ?",
                        new String[]{movie.getId()}
                );
                setFavoriteButtonText(viewHolder, context, R.string.favoriteButton_notChecked);
                Toast.makeText(context, "Deleted", Toast.LENGTH_LONG).show();
            }
        };
    }

    private Action1<Void> saveMovieToLocalDB(
            final Context context, final Movie movie, final MovieViewHolder viewHolder) {
        return new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                setFavoriteButtonText(viewHolder, context, R.string.favoriteButton_checked);
                String message = "";
                if (imageDownloaded) {
                    byte[] bitmapdata = getImageBytes(viewHolder);
                    ContentValues values = new ContentValues();
                    values.put(COLUMN_MOVIE_ID, movie.getId());
                    values.put(COLUMN_RELEASE_DATE, movie.getReleaseDate());
                    values.put(COLUMN_OVERVIEW, movie.getOverview());
                    values.put(COLUMN_ORIGINAL_TITLE, movie.getOriginalTitle());
                    values.put(COLUMN_AVERAGE_RATE, movie.getVoteAverage());
                    values.put(COLUMN_POSTER, bitmapdata);

                    context.getContentResolver().insert(
                            FavoriteContract.FavoriteEntry.CONTENT_URI, values
                    );
                    message = "Movie added to favorites!";
                } else {
                    message = "Cannot load the image, please check your internet connection.";
                }
                Toast.makeText(context, message, Toast.LENGTH_LONG).show();
            }
        };
    }

    private byte[] getImageBytes(MovieViewHolder viewHolder) {
        Bitmap bitmap = ((BitmapDrawable)viewHolder.thumbnail.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        return stream.toByteArray();
    }
}
