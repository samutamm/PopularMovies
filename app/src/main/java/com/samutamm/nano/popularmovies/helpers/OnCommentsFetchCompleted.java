package com.samutamm.nano.popularmovies.helpers;

import android.content.Context;

import com.samutamm.nano.popularmovies.domain.Comment;

import java.util.List;

public interface OnCommentsFetchCompleted {
    void onComments(Context context, List<Comment> results, MovieViewHolder holder);
}
