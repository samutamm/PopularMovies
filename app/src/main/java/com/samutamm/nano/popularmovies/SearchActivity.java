package com.samutamm.nano.popularmovies;

import android.app.Activity;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.samutamm.nano.popularmovies.sync.HttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class SearchActivity extends Activity implements AdapterView.OnItemSelectedListener {
    public final String LOG_TAG = SearchActivity.class.getSimpleName();

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String criteria = (String)adapterView.getItemAtPosition(i);
        String urlCriteria = "";
        if (criteria.equals(getString(R.string.criteria_popular))) {
            urlCriteria = "popular";
        } else {
            urlCriteria = "top_rated";
        }
        HttpClient client = new HttpClient();
        String returnString = client.fetchApi(urlCriteria);
    }


    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        System.out.println("NOTHONG SELECTED");
    }

    private class FetchApi extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... strings) {
            return null;
        }
    }
}
