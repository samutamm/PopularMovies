package com.samutamm.nano.popularmovies.helpers;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.samutamm.nano.popularmovies.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieRowViewHolder extends RecyclerView.ViewHolder {

    public @BindView(R.id.left_image)ImageView leftImage;
    public @BindView(R.id.middle_image)ImageView middleImage;
    public @BindView(R.id.right_image)ImageView rightImage;

    public MovieRowViewHolder(View v) {
        super(v);
        ButterKnife.bind(this, v);
    }
}