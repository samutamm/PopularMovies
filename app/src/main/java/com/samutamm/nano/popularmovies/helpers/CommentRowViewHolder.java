package com.samutamm.nano.popularmovies.helpers;

import android.view.View;
import android.widget.TextView;

import com.samutamm.nano.popularmovies.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CommentRowViewHolder {
    public @BindView(R.id.commentAuthor) TextView author;
    public @BindView(R.id.commentContent)  TextView content;

    public CommentRowViewHolder(View v) {
        ButterKnife.bind(this, v);
    }

}
