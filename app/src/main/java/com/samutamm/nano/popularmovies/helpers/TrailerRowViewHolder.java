package com.samutamm.nano.popularmovies.helpers;


import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.samutamm.nano.popularmovies.R;

public class TrailerRowViewHolder {
    public ImageView playTrailer;
    public TextView trailerName;

    public TrailerRowViewHolder(View v) {
        playTrailer = (ImageView)v.findViewById(R.id.playTrailer);
        trailerName = (TextView)v.findViewById(R.id.trailerName);
    }
}
