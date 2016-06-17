package com.samutamm.nano.popularmovies.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.samutamm.nano.popularmovies.R;
import com.samutamm.nano.popularmovies.adapters.MovieAdapter;
import com.samutamm.nano.popularmovies.domain.Movie;
import com.samutamm.nano.popularmovies.fragments.MovieFragment;
import com.samutamm.nano.popularmovies.fragments.SearchFragment;

public class MainActivity extends AppCompatActivity implements MovieAdapter.OnImageClickCallback{

    private boolean mTwoPane;
    public static final String MOVIEFRAGMENT_TAG = "moviefragment_tag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (findViewById(R.id.movie_detail_container) != null) {
            mTwoPane = true;
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.movie_detail_container, new MovieFragment(), MOVIEFRAGMENT_TAG)
                        .commit();
            }
        } else {
            mTwoPane = false;
        }

        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.search_criteria, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        SearchFragment searchFragment = (SearchFragment) getFragmentManager().findFragmentById(R.id.search_fragment);
        spinner.setOnItemSelectedListener(searchFragment);

        System.out.println("TWO PANE: " + mTwoPane);
    }

    @Override
    public void onImageClick(Movie movie) {
        if (mTwoPane) {
            //// TODO: 17/06/16  Start movieactivity as second view.
        } else {
            Intent movieIntent = new Intent(this, MovieActivity.class);
            movieIntent.putExtra(MovieActivity.MOVIE_TAG, movie);
            startActivity(movieIntent);
        }
    }
}
