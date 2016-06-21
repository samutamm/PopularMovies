package com.samutamm.nano.popularmovies.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.test.AndroidTestCase;
import android.test.MoreAsserts;
import android.util.Log;

import com.samutamm.nano.popularmovies.R;

import junit.framework.Assert;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashSet;
import static com.samutamm.nano.popularmovies.data.FavoriteContract.FavoriteEntry.*;
import java.io.RandomAccessFile;

public class FavoriteDbTest extends AndroidTestCase {

    public void testCreateDb() throws Throwable {

        mContext.deleteDatabase(FavoriteDbHelper.DATABASE_NAME);
        SQLiteDatabase db = new FavoriteDbHelper(
                this.mContext).getWritableDatabase();
        Assert.assertEquals(true, db.isOpen());

        // have we created the tables we want?
        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);

        Assert.assertTrue("Error: This means that the database has not been created correctly",
                c.moveToFirst());

        // now, do our tables contain the correct columns?
        c = db.rawQuery("PRAGMA table_info(" + FavoriteContract.FavoriteEntry.TABLE_NAME + ")",
                null);

        assertTrue("Error: This means that we were unable to query the database for table information.",
                c.moveToFirst());

        // Build a HashSet of all of the column names we want to look for
        final HashSet<String> favoriteColumns = new HashSet<String>();
        favoriteColumns.add(_ID);
        favoriteColumns.add(COLUMN_MOVIE_ID);
        favoriteColumns.add(COLUMN_AVERAGE_RATE);
        favoriteColumns.add(COLUMN_ORIGINAL_TITLE);
        favoriteColumns.add(COLUMN_OVERVIEW);
        favoriteColumns.add(COLUMN_POSTER);
        favoriteColumns.add(COLUMN_RELEASE_DATE);

        int columnNameIndex = c.getColumnIndex("name");
        do {
            String columnName = c.getString(columnNameIndex);
            favoriteColumns.remove(columnName);
        } while (c.moveToNext());

        assertTrue("Error: The database doesn't contain all of the required location entry columns",
                favoriteColumns.isEmpty());
        db.close();
    }

    public void testFavoriteTable() throws IOException {

        FavoriteDbHelper dbHelper = new FavoriteDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Drawable d = mContext.getDrawable(R.drawable.placeholder);
        Bitmap bitmap = ((BitmapDrawable)d).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] bitmapdata = stream.toByteArray();

        ContentValues favoriteValues = new ContentValues();
        favoriteValues.put(COLUMN_MOVIE_ID, "123");
        favoriteValues.put(COLUMN_AVERAGE_RATE, "5.5");
        favoriteValues.put(COLUMN_ORIGINAL_TITLE, "cinema");
        favoriteValues.put(COLUMN_OVERVIEW, "asdjnasd ad adafg eirjwe");
        favoriteValues.put(COLUMN_RELEASE_DATE, "2011-11-11");
        favoriteValues.put(COLUMN_POSTER, bitmapdata);

        long favoriteRoxId = db.insert(TABLE_NAME, null, favoriteValues);
        assertTrue(favoriteRoxId != -1);

        Cursor cursor = db.query(
                TABLE_NAME,  // Table to Query
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null, // columns to group by
                null, // columns to filter by row groups
                null  // sort order
        );

        assertTrue("Error: No Records returned from favorite query", cursor.moveToFirst());
        final byte[] blob = cursor.getBlob(cursor.getColumnIndex(COLUMN_POSTER));
        MoreAsserts.assertEquals(bitmapdata, blob);
        cursor.close();
        dbHelper.close();
    }
}