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
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.jakewharton.rxbinding.view.RxView;
import com.samutamm.nano.popularmovies.R;
import com.samutamm.nano.popularmovies.data.FavoriteContract;
import com.samutamm.nano.popularmovies.domain.Comment;
import com.samutamm.nano.popularmovies.domain.Trailer;
import com.samutamm.nano.popularmovies.helpers.CommentRowViewHolder;
import com.samutamm.nano.popularmovies.helpers.MovieViewHolder;
import com.samutamm.nano.popularmovies.interfaces.OnCommentsFetchCompleted;
import com.samutamm.nano.popularmovies.interfaces.OnTrailerFetchCompleted;
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
        final Bundle args = getArguments();
        View rootView = null;
        if (args != null) {
            rootView = inflater.inflate(R.layout.movie_fragment, container, false);
            final MovieViewHolder movieViewHolder = new MovieViewHolder(rootView);
            Movie movie = (Movie) args.get(MovieActivity.MOVIE_TAG);
            movieViewHolder.originalTitle.setText(movie.getOriginalTitle());
            movieViewHolder.movieYear.setText(Utility.parseYear(movie.getReleaseDate()));
            movieViewHolder.averageVote.setText(movie.getVoteAverage() + "/10.0");
            movieViewHolder.synopsis.setText(movie.getOverview());
            addFavoriteButton(rootView.getContext(), movieViewHolder, movie);

            if (movie.getPoster().length == 1) { //We are not showing the favorites
                setUpViewWithConnection(rootView.getContext(), movieViewHolder, movie);
            } else { // shoving favorites, let's not fetch trailers, image,or comments
                setUpFavoritesView(movieViewHolder, movie);
            }
        } else {
            rootView = inflater.inflate(R.layout.no_movie_selected, container, false);
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

        setUpCommentsButton(fetcher, viewHolder, movie);
    }

    private void addFavoriteButton(Context context, final MovieViewHolder viewHolder, final Movie movie) {
        Observable.fromCallable(()-> checkIfMovieIsFavorite(context, movie))
        .subscribeOn(Schedulers.io())
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
                    viewHolder.markFavoriteButton.setText(getString( R.string.favoriteButton_checked));
                    RxView.clicks(viewHolder.markFavoriteButton).subscribe(
                            removeMovieFromLocalDB(context, movie, viewHolder)
                    );
                } else {
                    RxView.clicks(viewHolder.markFavoriteButton).subscribe(
                            saveMovieToLocalDB(context, movie, viewHolder)
                    );
                }
            }
        });
    }

    private boolean checkIfMovieIsFavorite(Context context, Movie movie) {
        final ContentResolver resolver = context.getContentResolver();
        final Cursor query = resolver.query(
                FavoriteContract.FavoriteEntry.CONTENT_URI,
                null,
                FavoriteContract.FavoriteEntry.COLUMN_MOVIE_ID + " = ?",
                new String[]{movie.getId()},
                null
        );
        if (query == null) return false;
        final boolean exists = query.moveToFirst();
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
            RxView.clicks(viewHolder.markFavoriteButton).subscribe(
                    saveMovieToLocalDB(context, movie, viewHolder)
            );
            context.getContentResolver().delete(
                    FavoriteContract.FavoriteEntry.CONTENT_URI,
                    FavoriteContract.FavoriteEntry.COLUMN_MOVIE_ID + " = ?",
                    new String[]{movie.getId()}
            );
            viewHolder.markFavoriteButton.setText(getString( R.string.favoriteButton_notChecked));
            String message = getString(R.string.favorite_message_removed);
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        };
    }

    private Action1<Void> saveMovieToLocalDB(
            final Context context, final Movie movie, final MovieViewHolder viewHolder) {
        return (v) -> {
            if (imageDownloaded) {
                viewHolder.markFavoriteButton.setText(getString( R.string.favoriteButton_checked));
                ContentValues values = Utility.movieToContentValues(movie, viewHolder);
                RxView.clicks(viewHolder.markFavoriteButton).subscribe(
                        removeMovieFromLocalDB(context, movie, viewHolder)
                );
                context.getContentResolver().insert(
                        FavoriteContract.FavoriteEntry.CONTENT_URI, values
                );
                String message = getString(R.string.favorite_message_added);
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            } else {
                String message = getString(R.string.favorite_message_nointernet);
                Toast.makeText(context, message, Toast.LENGTH_LONG).show();
            }
        };
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

    private void setUpCommentsButton(APIFetcher fetcher,
                                     MovieViewHolder viewHolder, Movie movie) {
        RxView.clicks(viewHolder.showComments).subscribe((v) -> {
            fetcher.fetchComments(movie, viewHolder, this);
        });
    }

    @Override
    public void onComments(List<Comment> results, MovieViewHolder holder) {
        if (results.size()==0) {
            Toast.makeText(getContext(), "There are no comments!", Toast.LENGTH_SHORT).show();
        }
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
