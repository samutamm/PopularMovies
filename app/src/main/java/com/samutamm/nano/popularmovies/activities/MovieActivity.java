package com.samutamm.nano.popularmovies.activities;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.samutamm.nano.popularmovies.R;
import com.samutamm.nano.popularmovies.Utility;
import com.samutamm.nano.popularmovies.domain.Movie;
import com.squareup.picasso.Picasso;

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

    public static class MovieFragment extends Fragment {

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            final View rootView = inflater.inflate(R.layout.movie_fragment, container, false);
            ViewHolder viewHolder = new ViewHolder(rootView);

            Movie movie = (Movie) getArguments().get(MOVIE_TAG);
            viewHolder.originalTitle.setText(movie.getOriginalTitle());
            viewHolder.synopsis.setText(movie.getOverview());
            viewHolder.averageVote.setText(movie.getVoteAverage());
            viewHolder.releaseDate.setText(movie.getReleaseDate());

            final String thumbnailUrl = Utility.getMovieUrl(movie, "w92");
            Picasso.with(rootView.getContext())
                    .load(thumbnailUrl)
                    .resize(500, 800)
                    .into(viewHolder.thumbnail);

            return rootView;
        }

        public static class ViewHolder {
            public TextView originalTitle;
            public TextView synopsis;
            public TextView averageVote;
            public ImageView thumbnail;
            public TextView releaseDate;

            public ViewHolder(View view) {
                originalTitle = (TextView) view.findViewById(R.id.original_title);
                synopsis = (TextView)view.findViewById(R.id.synopsis);
                averageVote = (TextView)view.findViewById(R.id.vote_average);
                thumbnail = (ImageView)view.findViewById(R.id.thumbnail);
                releaseDate = (TextView)view.findViewById(R.id.release_date);
            }
        }
    }
}
