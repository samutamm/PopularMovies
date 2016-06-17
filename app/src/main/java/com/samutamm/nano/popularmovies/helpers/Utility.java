package com.samutamm.nano.popularmovies.helpers;

import com.samutamm.nano.popularmovies.domain.Movie;

import java.util.Date;

public class Utility {

    public static String getMovieUrl(Movie movie, String size) {
        return "http://image.tmdb.org/t/p/" + size + movie.getPosterPath();
    }

    public static String parseYear(String exampleDate) {
        final String[] split = exampleDate.split("-");
        if (split.length > 1 && split[0].length() == 4) {
            return split[0];
        }
        return "--";
    }
}
