package com.samutamm.nano.popularmovies.helpers;

import com.samutamm.nano.popularmovies.domain.Movie;

import java.util.List;

public interface OnMovieFetchCompleted {
    void onFetchCompleted(List<Movie> results);
}
