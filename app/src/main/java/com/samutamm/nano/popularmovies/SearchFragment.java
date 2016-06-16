package com.samutamm.nano.popularmovies;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.samutamm.nano.popularmovies.domain.Movie;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment implements AdapterView.OnItemSelectedListener, FetchApi.OnFetchCompleted {
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
        final View rootView = inflater.inflate(R.layout.search_fragment, container, false);
        RecyclerView recyclerView = (RecyclerView)rootView.findViewById(R.id.movieRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(rootView.getContext()));
        recyclerView.setAdapter(movieAdapter);
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
        new FetchApi(getActivity(), this).fetch(urlCriteria);
    }


    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {}

    @Override
    public void onFetchCompleted(List<Movie> results) {
        if (results != null) {
            movieList.clear();
            movieList.addAll(results);
            movieAdapter.notifyDataSetChanged();
        }
    }
}
