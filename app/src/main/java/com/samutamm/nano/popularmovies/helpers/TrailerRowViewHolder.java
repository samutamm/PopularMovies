package com.samutamm.nano.popularmovies.helpers;


import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.samutamm.nano.popularmovies.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TrailerRowViewHolder {
    public @BindView(R.id.playTrailer) ImageView playTrailer;
    public @BindView(R.id.trailerName)TextView trailerName;

    public TrailerRowViewHolder(View v) {
        ButterKnife.bind(this,v);
    }
}
