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
}