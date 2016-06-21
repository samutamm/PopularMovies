package com.samutamm.nano.popularmovies.data;

import android.net.Uri;
import android.provider.BaseColumns;

public class FavoriteContract {

    public static final String CONTENT_AUTHORITY = "com.samutamm.nano.popularmovies";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_FAVORITE = "favorite";

    public static final class FavoriteEntry implements BaseColumns {
        public static final String TABLE_NAME = "favorite";

        public static final String COLUMN_MOVIE_ID= "movie_id";

        public static final String COLUMN_ORIGINAL_TITLE = "original_title";

        public static final String COLUMN_OVERVIEW = "overview";

        public static final String COLUMN_AVERAGE_RATE = "average_rate";

        public static final String COLUMN_RELEASE_DATE = "release_date";

        public static final String COLUMN_POSTER = "poster";

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAVORITE).build();
    }
}
