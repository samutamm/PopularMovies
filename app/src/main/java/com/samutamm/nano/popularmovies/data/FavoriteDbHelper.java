package com.samutamm.nano.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.samutamm.nano.popularmovies.data.FavoriteContract.FavoriteEntry.*;

public class FavoriteDbHelper extends SQLiteOpenHelper{

    private static final int DATABASE_VERSION = 1;

    static final String DATABASE_NAME = "favorite.db";

    public FavoriteDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    private String createFavoriteTableQuery() {
        return "CREATE TABLE " + TABLE_NAME + " ( " +
                _ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_MOVIE_ID + " REAL UNIQUE NOT NULL," +
                COLUMN_ORIGINAL_TITLE + " TEXT NOT NULL," +
                COLUMN_OVERVIEW + " TEXT NOT NULL," +
                COLUMN_AVERAGE_RATE + " TEXT NOT NULL," +
                COLUMN_RELEASE_DATE + " TEXT NOT NULL," +
                COLUMN_POSTER + " BLOB NOT NULL);";
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(createFavoriteTableQuery());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
    }
}
