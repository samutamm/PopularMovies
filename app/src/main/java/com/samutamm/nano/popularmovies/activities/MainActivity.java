package com.samutamm.nano.popularmovies.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.samutamm.nano.popularmovies.R;
import com.samutamm.nano.popularmovies.fragments.MovieFragment;
import com.samutamm.nano.popularmovies.fragments.SearchFragment;

public class MainActivity extends AppCompatActivity {

    private boolean mTwoPane;
    public static final String MOVIEFRAGMENT_TAG = "moviefragment_tag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (findViewById(R.id.movie_detail_container) != null) {
            mTwoPane = true;
            if (savedInstanceState == null) {
                /*getSupportFragmentManager().beginTransaction()
                        .replace(R.id.movie_detail_container, new MovieFragment(), MOVIEFRAGMENT_TAG)
                        .commit();
                        */
            }
        } else {
            mTwoPane = false;
        }

        final SearchFragment searchFragment = new SearchFragment();
        getFragmentManager().beginTransaction()
                .replace(R.id.container, searchFragment)
                .commit();

        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.search_criteria, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(searchFragment);

        System.out.println("TWO PANE: " + mTwoPane);
    }
}
