package com.samutamm.nano.popularmovies.helpers;

import android.net.Uri;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.samutamm.nano.popularmovies.R;

public class MovieViewHolder {
    public ScrollView movieScrollView;
    public LinearLayout movieLayout;
    public TextView originalTitle;
    public TextView synopsis;
    public TextView averageVote;
    public ImageView thumbnail;
    public TextView movieYear;
    public TextView movieLength;
    public Button markFavoriteButton;
    public LinearLayout trailers;
    public Button showComments;

    public MovieViewHolder(View view) {
        movieScrollView = (ScrollView)view.findViewById(R.id.movieScrollView);
        movieLayout = (LinearLayout) view.findViewById(R.id.movieLayout);
        originalTitle = (TextView) view.findViewById(R.id.original_title);
        synopsis = (TextView)view.findViewById(R.id.synopsis);
        averageVote = (TextView)view.findViewById(R.id.averageVote);
        thumbnail = (ImageView)view.findViewById(R.id.thumbnail);
        movieYear = (TextView)view.findViewById(R.id.movieYear);
        movieLength = (TextView)view.findViewById(R.id.movieLength);
        markFavoriteButton = (Button) view.findViewById(R.id.markMovieAsFavorite);
        trailers = (LinearLayout)view.findViewById(R.id.trailers);
        showComments = (Button)view.findViewById(R.id.showComments);
    }
}