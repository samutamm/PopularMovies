package com.samutamm.nano.popularmovies.interfaces;

import android.content.Context;

import com.samutamm.nano.popularmovies.domain.Comment;
import com.samutamm.nano.popularmovies.helpers.MovieViewHolder;

import java.util.List;

public interface OnCommentsFetchCompleted {
    void onComments(List<Comment> results, MovieViewHolder holder);
}
