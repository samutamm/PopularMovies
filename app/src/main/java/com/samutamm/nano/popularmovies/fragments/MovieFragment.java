package com.samutamm.nano.popularmovies.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.samutamm.nano.popularmovies.R;
import com.samutamm.nano.popularmovies.helpers.MovieViewHolder;
import com.samutamm.nano.popularmovies.helpers.Utility;
import com.samutamm.nano.popularmovies.activities.MovieActivity;
import com.samutamm.nano.popularmovies.domain.Movie;
import com.squareup.picasso.Picasso;

public class MovieFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.movie_fragment, container, false);
        MovieViewHolder movieViewHolder = new MovieViewHolder(rootView);

        Movie movie = (Movie) getArguments().get(MovieActivity.MOVIE_TAG);
        movieViewHolder.originalTitle.setText(movie.getOriginalTitle());
        movieViewHolder.movieYear.setText(Utility.parseYear(movie.getReleaseDate()));
        movieViewHolder.averageVote.setText(movie.getVoteAverage() + "/10.0");
        movieViewHolder.synopsis.setText(movie.getOverview());

        final String thumbnailUrl = Utility.getMovieUrl(movie, "w92");
        Picasso.with(rootView.getContext())
                .load(thumbnailUrl)
                .into(movieViewHolder.thumbnail);

        return rootView;
    }
}
