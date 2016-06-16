package com.samutamm.nano.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jakewharton.rxbinding.view.RxView;
import com.samutamm.nano.popularmovies.domain.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

import rx.functions.Action1;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView leftImage;
        public ImageView rightImage;

        public ViewHolder(View v) {
            super(v);
            leftImage = (ImageView) v.findViewById(R.id.left_image);
            rightImage = (ImageView) v.findViewById(R.id.right_image);
        }
    }
    private Context mContext;
    private List<Movie> movieList;

    public MovieAdapter(Context c, List<Movie> movies) {
        mContext = c;
        movieList = movies;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_row, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Movie leftMovie = movieList.get(position);
        Movie rightMovie = movieList.get(movieList.size() - position - 1);

        addImageToView(leftMovie, holder.leftImage);
        addImageToView(rightMovie, holder.rightImage);
    }

    @Override
    public int getItemCount() {
        return movieList.size() / 2; // Becouse we have 2 images on one 'item'
    }

    public void addImageToView(final Movie movie, View convertView) {
        ImageView imageView = (ImageView) convertView;
        String size = "w185";
        imageView.setTag(movie);
        String imageUrl = Utility.getMovieUrl(movie, size);
        Picasso.with(mContext.getApplicationContext())
                .load(imageUrl)
                .placeholder(R.drawable.placeholder)
                .into(imageView);

        RxView.clicks(imageView).subscribe(startMovieActivity(movie));
    }

    private Action1<Void> startMovieActivity(final Movie movie) {
        return new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Intent movieIntent = new Intent(mContext, MovieActivity.class);
                movieIntent.putExtra(MovieActivity.MOVIE_TAG, movie);
                mContext.startActivity(movieIntent);
            }
        };
    }
}
