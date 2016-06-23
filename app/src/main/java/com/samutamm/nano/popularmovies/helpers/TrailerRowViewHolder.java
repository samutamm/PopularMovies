package com.samutamm.nano.popularmovies.helpers;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.samutamm.nano.popularmovies.R;

public class TrailerRowViewHolder {
    public TextView trailerUrl;

    public TrailerRowViewHolder(View v) {
        trailerUrl = (TextView)v.findViewById(R.id.trailerUrl);
    }
}
