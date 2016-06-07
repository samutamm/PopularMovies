package com.samutamm.nano.popularmovies;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MovieAdapter extends CursorAdapter{

    public MovieAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        final View view = LayoutInflater.from(context).inflate(R.layout.list_movie_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

    }

    public static class ViewHolder {
        public final ImageView iconView;
        public final TextView nameView;

        public ViewHolder(View view) {
            iconView = (ImageView)view.findViewById(R.id.list_movie_image);
            nameView = (TextView)view.findViewById(R.id.list_movie_name);
        }
    }
}
