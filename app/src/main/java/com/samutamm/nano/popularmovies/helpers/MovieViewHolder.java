package com.samutamm.nano.popularmovies.helpers;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.samutamm.nano.popularmovies.R;

public class MovieViewHolder {
    public TextView originalTitle;
    public TextView synopsis;
    public TextView averageVote;
    public ImageView thumbnail;
    public TextView movieYear;
    public TextView movieLength;
    public Button markFavoriteButton;
    public LinearLayout trailers;

    public MovieViewHolder(View view) {
        originalTitle = (TextView) view.findViewById(R.id.original_title);
        synopsis = (TextView)view.findViewById(R.id.synopsis);
        averageVote = (TextView)view.findViewById(R.id.averageVote);
        thumbnail = (ImageView)view.findViewById(R.id.thumbnail);
        movieYear = (TextView)view.findViewById(R.id.movieYear);
        movieLength = (TextView)view.findViewById(R.id.movieLength);
        markFavoriteButton = (Button) view.findViewById(R.id.markMovieAsFavorite);
        trailers = (LinearLayout)view.findViewById(R.id.trailers);
    }
}