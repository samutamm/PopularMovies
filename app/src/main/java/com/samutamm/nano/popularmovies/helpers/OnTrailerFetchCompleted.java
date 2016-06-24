package com.samutamm.nano.popularmovies.helpers;

import com.samutamm.nano.popularmovies.domain.Trailer;

import java.util.List;

public interface OnTrailerFetchCompleted {
    public void onTrailers(List<Trailer> results, MovieViewHolder holder);
}
