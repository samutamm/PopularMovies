package com.samutamm.nano.popularmovies.helpers;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.samutamm.nano.popularmovies.R;

public class MovieRowViewHolder extends RecyclerView.ViewHolder {

    public ImageView leftImage;
    public ImageView rightImage;

    public MovieRowViewHolder(View v) {
        super(v);
        leftImage = (ImageView) v.findViewById(R.id.left_image);
        rightImage = (ImageView) v.findViewById(R.id.right_image);
    }
}