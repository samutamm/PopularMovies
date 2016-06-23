package com.samutamm.nano.popularmovies.sync;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.samutamm.nano.popularmovies.domain.Comment;
import com.samutamm.nano.popularmovies.domain.Movie;
import com.samutamm.nano.popularmovies.domain.Trailer;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public class Parser {

    private Gson gson;
    public Parser() {
        gson = new Gson();
    }

    //TODO REFACTOR DUPLICATES
    public List<Movie> parseMovies(String jsonString) {
        if (jsonString != null) {
            final JsonObject json = gson.fromJson(jsonString, JsonObject.class);
            final String resultsKey = "results";
            if (json != null && json.has(resultsKey)) {
                final JsonElement results = json.get(resultsKey);
                return gson.fromJson(results, new TypeToken<List<Movie>>(){}.getType());
            }
        }
        return new ArrayList<>();
    }

    public List<Trailer> parseTrailers(String jsonString) {
        if (jsonString != null) {
            final JsonObject json = gson.fromJson(jsonString, JsonObject.class);
            final String resultsKey = "results";
            if (json != null && json.has(resultsKey)) {
                final JsonElement results = json.get(resultsKey);
                return gson.fromJson(results, new TypeToken<List<Trailer>>(){}.getType());
            }
        }
        return new ArrayList<>();
    }

    public List<Comment> parseComments(String jsonString) {
        if (jsonString != null) {
            final JsonObject json = gson.fromJson(jsonString, JsonObject.class);
            final String resultsKey = "results";
            if (json != null && json.has(resultsKey)) {
                final JsonElement results = json.get(resultsKey);
                return gson.fromJson(results, new TypeToken<List<Comment>>(){}.getType());
            }
        }
        return new ArrayList<>();
    }
}
