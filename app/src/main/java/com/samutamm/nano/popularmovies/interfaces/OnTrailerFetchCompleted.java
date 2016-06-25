package com.samutamm.nano.popularmovies.interfaces;

import com.samutamm.nano.popularmovies.domain.Trailer;
import com.samutamm.nano.popularmovies.helpers.MovieViewHolder;

import java.util.List;

public interface OnTrailerFetchCompleted {
    public void onTrailers(List<Trailer> results, MovieViewHolder holder);
}
