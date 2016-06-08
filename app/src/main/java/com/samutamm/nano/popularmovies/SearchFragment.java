package com.samutamm.nano.popularmovies;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import com.samutamm.nano.popularmovies.domain.Movie;
import com.samutamm.nano.popularmovies.sync.HttpClient;
import com.samutamm.nano.popularmovies.sync.Parser;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    public final String LOG_TAG = SearchFragment.class.getSimpleName();

    private MovieAdapter movieAdapter;
    private List<Movie> movieList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        movieList = new ArrayList<>();
        movieAdapter = new MovieAdapter(getActivity(), movieList);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.search_fragment, container, false);
        GridView gridView = (GridView)rootView.findViewById(R.id.movieGrid);
        gridView.setAdapter(movieAdapter);
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

    private class FetchApi extends AsyncTask<String, String, Movie[]> {

        private  HttpClient client;
        private Parser parser;
        private ArrayAdapter adapter;

        public FetchApi() {
            client = new HttpClient();
            parser = new Parser();
        }

        @Override
        protected Movie[] doInBackground(String... strings) {
            final String json = client.fetchApi(strings[0]);
            List<Movie> movies = parser.parseJson(json);

            String[][] mArray = new String[movies.size()][3];
            for (int i = 0; i< mArray.length; i++) {
                Movie movie = movies.get(i);
                String[] row = new String[]{i + 1+"", movie.getTitle(), movie.getPoster_path()};
                mArray[i] = row;
            }
            return movies.toArray(new Movie[movies.size()]);
        }

        @Override
        protected void onPostExecute(Movie[] results) {
            if (results != null) {
                movieList.clear();
                for (int i = 0; i < results.length; i++){
                    movieList.add(results[i]);
                }
                movieAdapter.notifyDataSetChanged();
            }
        }
    }
}
