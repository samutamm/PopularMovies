package com.samutamm.nano.popularmovies.helpers;

import android.view.View;
import android.widget.TextView;

import com.samutamm.nano.popularmovies.R;

public class CommentRowViewHolder {
    public TextView author;
    public TextView content;

    public CommentRowViewHolder(View v) {
        author = (TextView)v.findViewById(R.id.commentAuthor);
        content = (TextView)v.findViewById(R.id.commentContent);
    }

}
