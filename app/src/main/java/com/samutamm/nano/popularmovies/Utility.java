package com.samutamm.nano.popularmovies;

import com.samutamm.nano.popularmovies.domain.Movie;

public class Utility {

    public static String getMovieUrl(Movie movie, String size) {
        return "http://image.tmdb.org/t/p/" + size + movie.getPosterPath();
    }
}
