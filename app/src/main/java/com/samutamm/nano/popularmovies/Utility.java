package com.samutamm.nano.popularmovies;

import android.content.Context;
import android.util.Log;

import com.samutamm.nano.popularmovies.domain.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

public class Utility {

    public static String getMovieUrl(Movie movie, String size) {
        return "http://image.tmdb.org/t/p/" + size + movie.getPoster_path();
    }
}
