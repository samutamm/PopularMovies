package com.samutamm.nano.popularmovies.helpers;

import com.samutamm.nano.popularmovies.domain.Movie;

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

    public static int[] getMovieListIndexesForTablet(int position, int movieListSize) {
        int[] indexes = new int[3];
        int base = position * 3;
        for (int j = 0; j < 3; j++) {
            final boolean indexOutOfBound = (base + j) >= movieListSize;
            if (indexOutOfBound) {
                indexes[j] = -1;
            } else {
                indexes[j] = base + j;
            }
        }
        return indexes;
    }
}
