package com.samutamm.nano.popularmovies;

import android.app.Activity;
import android.net.Uri;
import android.view.View;
import android.widget.AdapterView;

import java.net.URL;

public class SearchActivity extends Activity implements AdapterView.OnItemSelectedListener {
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String criteria = (String)adapterView.getItemAtPosition(i);
        String urlCriteria = "";
        if (criteria.equals(getString(R.string.criteria_popular))) {
            urlCriteria = "popular";
        } else {
            urlCriteria = "top_rated";
        }
        final String MOVIE_BASEURL =
                "http://api.themoviedb.org/3/movie/" + urlCriteria + "?";
        final String API_KEY = "api_key";

        Uri builtUri = Uri.parse(MOVIE_BASEURL).buildUpon()
                .appendQueryParameter(API_KEY, "")
                .build();

        final String urlString = builtUri.toString();
        System.out.println(urlString);
       // URL url = new URL(urlString);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        System.out.println("NOTHONG SELECTED");
    }
}
