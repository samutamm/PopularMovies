package com.samutamm.nano.popularmovies.domain;

import java.io.Serializable;

public class Movie implements Serializable{
    private String poster_path;
    private String title;

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
