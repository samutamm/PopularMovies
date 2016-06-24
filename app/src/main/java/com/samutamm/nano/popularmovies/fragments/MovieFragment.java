package com.samutamm.nano.popularmovies.fragments;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.jakewharton.rxbinding.view.RxView;
import com.samutamm.nano.popularmovies.R;
import com.samutamm.nano.popularmovies.data.FavoriteContract;
import com.samutamm.nano.popularmovies.domain.Comment;
import com.samutamm.nano.popularmovies.domain.Trailer;
import com.samutamm.nano.popularmovies.helpers.CommentRowViewHolder;
import com.samutamm.nano.popularmovies.helpers.MovieViewHolder;
import com.samutamm.nano.popularmovies.helpers.OnCommentsFetchCompleted;
import com.samutamm.nano.popularmovies.helpers.OnTrailerFetchCompleted;
import com.samutamm.nano.popularmovies.helpers.TrailerRowViewHolder;
import com.samutamm.nano.popularmovies.helpers.Utility;
import com.samutamm.nano.popularmovies.activities.MovieActivity;
import com.samutamm.nano.popularmovies.domain.Movie;
import com.samutamm.nano.popularmovies.sync.APIFetcher;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.concurrent.Callable;

import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

import static com.samutamm.nano.popularmovies.data.FavoriteContract.FavoriteEntry.*;

public class MovieFragment extends Fragment implements OnTrailerFetchCompleted, OnCommentsFetchCompleted {

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

            if (movie.getPoster().length == 1) { //We are not showing the favorites
                setUpViewWithConnection(rootView.getContext(), movieViewHolder, movie);
            } else { // shoving favorites, let's not fetch trailers, image,or comments
                setUpFavoritesView(movieViewHolder, movie);
            }
        }
        return rootView;
    }

    private void setUpFavoritesView(MovieViewHolder movieViewHolder, Movie movie) {
        final byte[] poster = movie.getPoster();
        Bitmap bitmap = BitmapFactory.decodeByteArray(poster, 0, poster.length);
        movieViewHolder.thumbnail.setImageBitmap(bitmap);
    }

    private void setUpViewWithConnection(Context context, MovieViewHolder viewHolder, Movie movie) {
        loadImageFromInternet(context, viewHolder, movie);
        APIFetcher fetcher = new APIFetcher(context);
        fetcher.fetchTrailers(movie, viewHolder ,this);

        setUpCommentsButton(context, fetcher, viewHolder, movie);
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

    private void loadImageFromInternet(Context context, final MovieViewHolder movieViewHolder, Movie movie) {
        final String thumbnailUrl = Utility.getMovieUrl(movie, "w92");
        Picasso.with(context)
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
        return (v) -> {
            context.getContentResolver().delete(
                    FavoriteContract.FavoriteEntry.CONTENT_URI,
                    FavoriteContract.FavoriteEntry.COLUMN_MOVIE_ID + " = ?",
                    new String[]{movie.getId()}
            );
            setFavoriteButtonText(viewHolder, context, R.string.favoriteButton_notChecked);
            Toast.makeText(context, "Deleted", Toast.LENGTH_LONG).show();
        };
    }

    private Action1<Void> saveMovieToLocalDB(
            final Context context, final Movie movie, final MovieViewHolder viewHolder) {
        return (v) -> {
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
        };
    }

    private byte[] getImageBytes(MovieViewHolder viewHolder) {
        Bitmap bitmap = ((BitmapDrawable)viewHolder.thumbnail.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        return stream.toByteArray();
    }

    @Override
    public void onTrailers(List<Trailer> result, MovieViewHolder holder) {
        if (result != null) {
            final LinearLayout trailers = holder.trailers;
            for (Trailer trailer: result) {
                if (trailer.getSite().equals("YouTube")) {
                    final View row = LayoutInflater
                            .from(getContext()).inflate(R.layout.trailer_row, trailers, false);
                    handleOneTrailer(trailer, row);
                    trailers.addView(row);
                }
            }
        }
    }

    private void handleOneTrailer(final Trailer trailer, View row) {
        TrailerRowViewHolder trailerHolder = new TrailerRowViewHolder(row);
        trailerHolder.trailerName.setText(trailer.getName());
        RxView.clicks(trailerHolder.playTrailer).subscribe((v) -> {
                String youtubeID = trailer.getKey();
                startActivity(new Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("http://www.youtube.com/watch?v=" + youtubeID)));
        });
    }

    private void setUpCommentsButton(Context context, APIFetcher fetcher,
                                     MovieViewHolder viewHolder, Movie movie) {
        RxView.clicks(viewHolder.showComments).subscribe((v) -> {
            fetcher.fetchComments(context, movie, viewHolder, this);
        });
    }

    @Override
    public void onComments(Context context, List<Comment> results, MovieViewHolder holder) {
        LinearLayout comments = new LinearLayout(getContext());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        comments.setOrientation(LinearLayout.VERTICAL);
        comments.setLayoutParams(params);
        for (Comment c: results) {
            final View row = LayoutInflater
                    .from(getContext()).inflate(R.layout.comment_row, comments, false);
            CommentRowViewHolder commentHolder = new CommentRowViewHolder(row);
            commentHolder.author.setText(c.getAuthor());
            commentHolder.content.setText(c.getContent());
            comments.addView(row);
        }

        holder.movieLayout.addView(comments);
    }
}
