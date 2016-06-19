package com.samutamm.nano.popularmovies;

import org.junit.Test;
import static org.junit.Assert.*;
import com.samutamm.nano.popularmovies.helpers.Utility;

public class UtilityTest {

    @Test
    public void testBuildConfig() {
        assertNotNull(BuildConfig.MOVIE_DATABASE_API_KEY);
    }

    @Test
    public void testParseYear() {
        assertEquals("2016", Utility.parseYear("2016-01-22"));
        assertEquals("1901", Utility.parseYear("1901-08-19"));
        assertEquals("--", Utility.parseYear("216-01-22"));
    }

    @Test
    public void testGetMovieListIndexesForTablet() {
        assertIndexes(Utility.getMovieListIndexesForTablet(0, 7), 0,1,2);
        assertIndexes(Utility.getMovieListIndexesForTablet(1, 7), 3,4,5);
        assertIndexes(Utility.getMovieListIndexesForTablet(2, 7), 6,-1,-1);
        assertIndexes(Utility.getMovieListIndexesForTablet(6, 20), 18,19,-1);
        assertIndexes(Utility.getMovieListIndexesForTablet(7, 20), -1,-1,-1);
    }

    private void assertIndexes(int[] indexes, int a, int b, int c) {
        assertEquals(a, indexes[0]);
        assertEquals(b, indexes[1]);
        assertEquals(c, indexes[2]);
    }
}