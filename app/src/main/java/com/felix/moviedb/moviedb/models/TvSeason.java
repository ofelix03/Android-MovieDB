package com.felix.moviedb.moviedb.models;

import java.io.Serializable;

/**
 * Created by felix on 1/16/17.
 */

public class TvSeason implements Serializable {

    private int number;
    private int episodes;
    private String poster;

    public TvSeason() {

    }

    public TvSeason(int seasonNumber, int episodes, String poster) {
        this.number = seasonNumber;
        this.episodes = episodes;
        this.poster = poster;
    }

    public int getEpisodes() {
        return episodes;
    }

    public int getNumber() {
        return number;
    }

    public void setEpisodes(int episodes) {
        this.episodes = episodes;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }
}
