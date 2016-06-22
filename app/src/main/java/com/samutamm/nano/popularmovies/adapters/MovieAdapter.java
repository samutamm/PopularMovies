package com.samutamm.nano.popularmovies.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jakewharton.rxbinding.view.RxView;
import com.samutamm.nano.popularmovies.R;
import com.samutamm.nano.popularmovies.helpers.MovieRowViewHolder;
import com.samutamm.nano.popularmovies.helpers.Utility;
import com.samutamm.nano.popularmovies.domain.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

import rx.functions.Action1;

public class MovieAdapter extends RecyclerView.Adapter<MovieRowViewHolder> {

    private Context mContext;
    private List<Movie> movieList;
    private boolean tabletMode;

    public MovieAdapter(Context c, List<Movie> movies) {
        mContext = c;
        movieList = movies;
        tabletMode = false;
    }

    @Override
    public MovieRowViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_row, parent, false);
        return new MovieRowViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MovieRowViewHolder holder, int position) {
        if (tabletMode) {
            handleBigScreen(holder, position);
        } else {
            handleSmallScreen(holder, position);
        }
    }

    private void handleSmallScreen(MovieRowViewHolder holder, int position) {
        Movie leftMovie = movieList.get(position);
        Movie rightMovie = movieList.get(movieList.size() - position - 1);

        addImageToView(leftMovie, holder.leftImage);
        addImageToView(rightMovie, holder.rightImage);
    }

    private void handleBigScreen(MovieRowViewHolder holder, int position) {
        int[] indexes = Utility.getMovieListIndexesForTablet(position, movieList.size());
        ImageView[] images = new ImageView[]{holder.leftImage, holder.middleImage, holder.rightImage};
        for(int i = 0; i < 3; i++) {
            if (indexes[i] != -1) {
                Movie movie = movieList.get(indexes[i]);
                addImageToView(movie, images[i]);
            }
        }
    }

    @Override
    public int getItemCount() {
        if (tabletMode) {
            return movieList.size() / 3; // Becouse we have 3 images on one 'item'
        }
        return movieList.size() / 2; // Becouse we have 2 images on one 'item'
    }

    public void addImageToView(final Movie movie, ImageView imageView) {
        imageView.setTag(movie);
        if(movie.getPoster().length == 1) {
            downloadImageFromInternet(movie, imageView);
        } else {
            final byte[] poster = movie.getPoster();
            Bitmap bitmap = BitmapFactory.decodeByteArray(poster, 0, poster.length);
            imageView.setImageBitmap(bitmap);
        }
        RxView.clicks(imageView).subscribe(startMovieActivity(movie));
    }

    private void downloadImageFromInternet(Movie movie, ImageView imageView) {
        String size = "w185";
        String imageUrl = Utility.getMovieUrl(movie, size);
        Picasso.with(mContext.getApplicationContext())
                .load(imageUrl)
                .placeholder(R.drawable.placeholder)
                .into(imageView);
    }

    private Action1<Void> startMovieActivity(final Movie movie) {
        final OnImageClickCallback callback = (OnImageClickCallback)mContext;
        return new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
               callback.onImageClick(movie);
            }
        };
    }

    public void setTabletMode(boolean tabletMode) {
        this.tabletMode = tabletMode;
    }

    public interface OnImageClickCallback {
        void onImageClick(Movie movie);
    }
}
