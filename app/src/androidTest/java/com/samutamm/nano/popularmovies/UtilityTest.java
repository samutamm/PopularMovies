package com.samutamm.nano.popularmovies;


import android.test.AndroidTestCase;

public class UtilityTest extends AndroidTestCase{

    public void testBuildConfig() {
        assertNotNull(BuildConfig.MOVIE_DATABASE_API_KEY);
    }
}