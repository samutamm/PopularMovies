package com.samutamm.nano.popularmovies;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.test.AndroidTestCase;
import android.test.MoreAsserts;

import java.io.ByteArrayOutputStream;
import java.util.Map;
import java.util.Set;

import static com.samutamm.nano.popularmovies.data.FavoriteContract.FavoriteEntry.*;

public class TestUtilities extends AndroidTestCase{

    public static void validateCursor(String error, Cursor valueCursor, ContentValues expectedValues) {
        assertTrue("Empty cursor returned. " + error, valueCursor.moveToFirst());
        if (expectedValues.containsKey(COLUMN_POSTER)) {
            MoreAsserts.assertEquals((byte[])expectedValues.get(COLUMN_POSTER),
                    valueCursor.getBlob(valueCursor.getColumnIndex(COLUMN_POSTER)));
            expectedValues.remove(COLUMN_POSTER);
        }
        validateCurrentRecord(error, valueCursor, expectedValues);
        valueCursor.close();
    }

    public static void validateCurrentRecord(String error, Cursor valueCursor, ContentValues expectedValues) {
        Set<Map.Entry<String, Object>> valueSet = expectedValues.valueSet();
        for (Map.Entry<String, Object> entry : valueSet) {
            String columnName = entry.getKey();
            int idx = valueCursor.getColumnIndex(columnName);
            assertFalse("Column '" + columnName + "' not found. " + error, idx == -1);
            String expectedValue = entry.getValue().toString();
            assertEquals("Value '" + entry.getValue().toString() +
                    "' did not match the expected value '" +
                    expectedValue + "'. " + error, expectedValue, valueCursor.getString(idx));
        }
    }

    public static ContentValues createFavorite(Context context) {
        Drawable d = context.getDrawable(R.drawable.placeholder);
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
        return favoriteValues;
    }

}
