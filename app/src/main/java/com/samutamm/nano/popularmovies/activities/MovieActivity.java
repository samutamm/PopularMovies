package com.samutamm.nano.popularmovies.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.samutamm.nano.popularmovies.R;
import com.samutamm.nano.popularmovies.domain.Movie;
import com.samutamm.nano.popularmovies.fragments.MovieFragment;

public class MovieActivity extends AppCompatActivity {

    public final static String MOVIE_TAG = "MOVIE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_activity);
        Intent intent = getIntent();
        Movie movie = (Movie)intent.getExtras().get(MOVIE_TAG);

        final MovieFragment movieFragment = new MovieFragment();
        Bundle args = new Bundle();
        args.putParcelable(MOVIE_TAG,movie);
        movieFragment.setArguments(args);
        getFragmentManager().beginTransaction()
                .replace(R.id.movie_container, movieFragment)
                .commit();
    }
}
