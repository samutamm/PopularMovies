package com.samutamm.nano.popularmovies;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.samutamm.nano.popularmovies.domain.Movie;

public class MovieActivity extends AppCompatActivity {

    public final static String MOVIE_TAG = "MOVIE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        Movie movie = (Movie)intent.getExtras().get(MOVIE_TAG);
        System.out.println(movie.getTitle());
    }
}
