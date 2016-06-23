package com.samutamm.nano.popularmovies.helpers;

import com.samutamm.nano.popularmovies.domain.Trailer;

import java.util.List;

public interface OnTrailerFetchCompleted {
    void onFetchCompleted(List<Trailer> results, MovieViewHolder holder);
}
