package com.samutamm.nano.popularmovies.sync;


import android.test.AndroidTestCase;

/**
 * This testes uses internet and is not therefore production ready.
 * More like learning tests.
 */
public class HttpClientTest extends AndroidTestCase{


    public void testPopular() {
        HttpClient client = new HttpClient();
        String reponse = client.fetchApi("popular");
        assertNotNull(reponse);
    }
}