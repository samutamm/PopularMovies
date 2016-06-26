package com.samutamm.nano.popularmovies.helpers;

import android.content.ContentValues;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;

import com.samutamm.nano.popularmovies.domain.Movie;

import java.io.ByteArrayOutputStream;

import static com.samutamm.nano.popularmovies.data.FavoriteContract.FavoriteEntry.*;

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

    public static ContentValues movieToContentValues(Movie movie, MovieViewHolder viewHolder) {
        byte[] bitmapdata = getImageBytes(viewHolder);
        ContentValues values = new ContentValues();
        values.put(COLUMN_MOVIE_ID, movie.getId());
        values.put(COLUMN_RELEASE_DATE, movie.getReleaseDate());
        values.put(COLUMN_OVERVIEW, movie.getOverview());
        values.put(COLUMN_ORIGINAL_TITLE, movie.getOriginalTitle());
        values.put(COLUMN_AVERAGE_RATE, movie.getVoteAverage());
        values.put(COLUMN_POSTER, bitmapdata);
        return values;
    }

    private static byte[] getImageBytes(MovieViewHolder viewHolder) {
        Bitmap bitmap = ((BitmapDrawable)viewHolder.thumbnail.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        return stream.toByteArray();
    }
}
