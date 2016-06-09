package com.samutamm.nano.popularmovies;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.samutamm.nano.popularmovies.domain.Movie;

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
        args.putSerializable(MOVIE_TAG,movie);
        movieFragment.setArguments(args);
        getFragmentManager().beginTransaction()
                .replace(R.id.movie_container, movieFragment)
                .commit();
    }

    public static class MovieFragment extends Fragment {

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            Movie movie = (Movie) getArguments().get(MOVIE_TAG);
            System.out.println(movie.getTitle());
            return super.onCreateView(inflater, container, savedInstanceState);
        }
    }
}
