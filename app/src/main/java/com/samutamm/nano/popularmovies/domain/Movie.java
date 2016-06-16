package com.samutamm.nano.popularmovies.domain;

import android.os.Parcel;
import android.os.Parcelable;

public class Movie implements Parcelable {
    private String poster_path;
    private String title;
    private String original_title;
    private String overview;
    private String vote_average;
    private String release_date;

    public String getPosterPath() {
        return poster_path;
    }

    /**
     * Use non uniform naming convention for setters
     * to allow Gson to recognize them.
     */
    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOriginalTitle() {
        return original_title;
    }

    public void setOriginal_title(String original_title) {
        this.original_title = original_title;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getVoteAverage() {
        return vote_average;
    }

    public void setVote_average(String vote_average) {
        this.vote_average = vote_average;
    }

    public String getReleaseDate() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }


    protected Movie(Parcel in) {
        poster_path = in.readString();
        title = in.readString();
        original_title = in.readString();
        overview = in.readString();
        vote_average = in.readString();
        release_date = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(poster_path);
        dest.writeString(title);
        dest.writeString(original_title);
        dest.writeString(overview);
        dest.writeString(vote_average);
        dest.writeString(release_date);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}