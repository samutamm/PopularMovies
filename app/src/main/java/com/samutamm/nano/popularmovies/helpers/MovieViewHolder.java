package com.samutamm.nano.popularmovies.helpers;

import android.net.Uri;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.samutamm.nano.popularmovies.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieViewHolder {
    public @BindView(R.id.movieScrollView)ScrollView movieScrollView;
    public @BindView(R.id.movieLayout)LinearLayout movieLayout;
    public @BindView(R.id.original_title)TextView originalTitle;
    public @BindView(R.id.synopsis)TextView synopsis;
    public @BindView(R.id.averageVote)TextView averageVote;
    public @BindView(R.id.thumbnail)ImageView thumbnail;
    public @BindView(R.id.movieYear)TextView movieYear;
    public @BindView(R.id.movieLength)TextView movieLength;
    public @BindView(R.id.markMovieAsFavorite)Button markFavoriteButton;
    public @BindView(R.id.trailers)LinearLayout trailers;
    public @BindView(R.id.showComments)Button showComments;

    public MovieViewHolder(View view) {
        ButterKnife.bind(this, view);
    }
}