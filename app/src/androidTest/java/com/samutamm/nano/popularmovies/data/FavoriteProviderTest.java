package com.samutamm.nano.popularmovies.data;


import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.test.AndroidTestCase;

import com.samutamm.nano.popularmovies.TestUtilities;

public class FavoriteProviderTest extends AndroidTestCase{

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        deleteAllRecordsFromProvider();
    }
    public void deleteAllRecordsFromProvider() {
        mContext.getContentResolver().delete(
                FavoriteContract.FavoriteEntry.CONTENT_URI,
                null,
                null
        );


        Cursor cursor = mContext.getContentResolver().query(
                FavoriteContract.FavoriteEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        assertEquals("Error: Records not deleted from Weather table during delete", 0, cursor.getCount());
        cursor.close();
    }

    public void testProviderRegistry() {
        PackageManager pm = mContext.getPackageManager();
        ComponentName componentName = new ComponentName(mContext.getPackageName(),
                FavoriteProvider.class.getName());
        try {
            ProviderInfo providerInfo = pm.getProviderInfo(componentName, 0);

            assertEquals("Error: FavoriteProvider registered with authority: " + providerInfo.authority +
                            " instead of authority: " + FavoriteContract.CONTENT_AUTHORITY,
                    providerInfo.authority, FavoriteContract.CONTENT_AUTHORITY);
        } catch (PackageManager.NameNotFoundException e) {
            assertTrue("Error: FavoriteProvider not registered at " + mContext.getPackageName(),
                    false);
        }
    }

    public void testGetType() {
        String type = mContext.getContentResolver().getType(FavoriteContract.FavoriteEntry.CONTENT_URI);
        assertEquals("Error: the WeatherEntry CONTENT_URI should return WeatherEntry.CONTENT_TYPE",
                FavoriteContract.FavoriteEntry.CONTENT_TYPE, type);
    }

    public void testFavoriteQuery() {
        FavoriteDbHelper dbHelper = new FavoriteDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues testValues = TestUtilities.createFavorite(mContext);
        long favoriteID = db.insert(FavoriteContract.FavoriteEntry.TABLE_NAME, null, testValues);
        assertTrue("Couldn't add favorite", favoriteID != -1);
        db.close();

        Cursor favoriteCursor = mContext.getContentResolver().query(
                FavoriteContract.FavoriteEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );

        TestUtilities.validateCursor("testFavoriteQuery", favoriteCursor, testValues);
    }

    public void testFavoriteInsert() {
        final ContentResolver resolver = mContext.getContentResolver();
        final ContentValues values = TestUtilities.createFavorite(mContext);
        resolver.insert(
                FavoriteContract.FavoriteEntry.CONTENT_URI,
                values
        );

        final Cursor cursor = resolver.query(
                FavoriteContract.FavoriteEntry.CONTENT_URI, null, null, null, null
        );
        assertTrue(cursor.moveToFirst());

        TestUtilities.validateCursor("", cursor, values);
    }

}