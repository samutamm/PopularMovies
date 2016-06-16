package com.samutamm.nano.popularmovies.helpers;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.samutamm.nano.popularmovies.R;

public class MovieViewHolder {
    public TextView originalTitle;
    public TextView synopsis;
    public TextView averageVote;
    public ImageView thumbnail;
    public TextView releaseDate;

    public MovieViewHolder(View view) {
        originalTitle = (TextView) view.findViewById(R.id.original_title);
        synopsis = (TextView)view.findViewById(R.id.synopsis);
        averageVote = (TextView)view.findViewById(R.id.vote_average);
        thumbnail = (ImageView)view.findViewById(R.id.thumbnail);
        releaseDate = (TextView)view.findViewById(R.id.release_date);
    }
}