package com.samutamm.nano.popularmovies;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.samutamm.nano.popularmovies.domain.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MovieAdapter extends BaseAdapter {

    private Context mContext;
    private List<Movie> movieList;

    public MovieAdapter(Context c, List<Movie> movies) {
        mContext = c;
        movieList = movies;
    }

    public int getCount() {
        return movieList.size();
    }

    public Object getItem(int position) {
        return 0;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            imageView = new ImageView(mContext);
        } else {
            imageView = (ImageView) convertView;
        }
        String imageUrl = "http://image.tmdb.org/t/p/w92/" + movieList.get(position).getPoster_path();
        Picasso.with(mContext)
                .load(imageUrl)
                .into(imageView);
        return imageView;
    }
}
