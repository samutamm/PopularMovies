package com.samutamm.nano.popularmovies;

import android.app.Activity;
import android.app.Fragment;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.samutamm.nano.popularmovies.domain.Movie;
import com.samutamm.nano.popularmovies.sync.HttpClient;
import com.samutamm.nano.popularmovies.sync.Parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    public final String LOG_TAG = SearchFragment.class.getSimpleName();

    private MovieAdapter movieAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        List<String> list = new ArrayList<>();
        movieAdapter = new MovieAdapter(getActivity(), null, 0);//new ArrayAdapter<String>(getActivity(),
                //android.R.layout.simple_list_item_1, android.R.id.text1, list);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.movie_list, container, false);
        ListView movieList = (ListView)rootView.findViewById(R.id.movieList);
        movieList.setAdapter(movieAdapter);
        return rootView;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {
        String criteria = (String)parent.getItemAtPosition(i);
        String urlCriteria = "";
        if (criteria.equals(parent.getResources().getString(R.string.criteria_popular))) {
            urlCriteria = "popular";
        } else {
            urlCriteria = "top_rated";
        }
        new FetchApi().execute(urlCriteria);
    }


    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        System.out.println("NOTHONG SELECTED");
    }

    private class FetchApi extends AsyncTask<String, String, String[][]> {

        private  HttpClient client;
        private Parser parser;
        private ArrayAdapter adapter;

        public FetchApi() {
            client = new HttpClient();
            parser = new Parser();
        }

        @Override
        protected String[][] doInBackground(String... strings) {
            final String json = client.fetchApi(strings[0]);
            List<Movie> movies = parser.parseJson(json);

            String[][] mArray = new String[movies.size()][3];
            for (int i = 0; i< mArray.length; i++) {
                Movie movie = movies.get(i);
                String[] row = new String[]{i + 1+"", movie.getTitle(), movie.getPoster_path()};
                mArray[i] = row;
            }
            return mArray;
        }

        @Override
        protected void onPostExecute(String[][] results) {
            if (results != null) {
                String[] columns = new String[]{"_id", "name", "image"};
                MatrixCursor cursor = new MatrixCursor(columns);
                for (int i = 0; i < results.length; i++){
                    cursor.addRow(results[i]);
                }
                movieAdapter.swapCursor(cursor);
            }
        }
    }
}
