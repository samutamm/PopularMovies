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

    private List parse(String jsonString, Function function) {
        if (jsonString != null) {
            final JsonObject json = gson.fromJson(jsonString, JsonObject.class);
            final String resultsKey = "results";
            if (json != null && json.has(resultsKey)) {
                final JsonElement results = json.get(resultsKey);
                return function.call(results);
            }
        }
        return new ArrayList<>();
    }

    public List<Movie> parseMovies(String jsonString) {
        return parse(jsonString, (res -> gson.fromJson(res, new TypeToken<List<Movie>>(){}.getType())));
    }

    public List<Trailer> parseTrailers(String jsonString) {
        return parse(jsonString, (res) -> gson.fromJson(res, new TypeToken<List<Trailer>>(){}.getType()));
    }

    public List<Comment> parseComments(String jsonString) {
        return parse(jsonString, (res)->gson.fromJson(res, new TypeToken<List<Comment>>(){}.getType()));
    }

    private interface Function {
        public List<?> call(JsonElement results);
    }
}
