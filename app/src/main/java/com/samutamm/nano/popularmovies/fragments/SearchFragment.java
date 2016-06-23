package com.samutamm.nano.popularmovies.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.samutamm.nano.popularmovies.data.DatabaseFetcher;
import com.samutamm.nano.popularmovies.helpers.OnMovieFetchCompleted;
import com.samutamm.nano.popularmovies.sync.APIFetcher;
import com.samutamm.nano.popularmovies.adapters.MovieAdapter;
import com.samutamm.nano.popularmovies.R;
import com.samutamm.nano.popularmovies.domain.Movie;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment implements AdapterView.OnItemSelectedListener, OnMovieFetchCompleted {
    public final String LOG_TAG = SearchFragment.class.getSimpleName();

    private MovieAdapter movieAdapter;
    private List<Movie> movieList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        movieList = new ArrayList<>();
        movieAdapter = new MovieAdapter(getActivity(), movieList);
    }

    public void setTwoPaneModeToAdapter(boolean twoPane) {
        movieAdapter.setTabletMode(twoPane);
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
        final APIFetcher fetcher = new APIFetcher(getActivity());
        if (criteria.equals(parent.getResources().getString(R.string.criteria_popular))) {
            fetcher.fetchMovies("popular", this);
        } else if(criteria.equals(parent.getResources().getString(R.string.criteria_rated))) {
            fetcher.fetchMovies("top_rated", this);
        } else {
            new DatabaseFetcher(getActivity(), this).fetchFavorites();
        }
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
