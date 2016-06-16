package com.samutamm.nano.popularmovies.sync;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.samutamm.nano.popularmovies.domain.Movie;

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

    public List<Movie> parseJson(String jsonString) {
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
}
