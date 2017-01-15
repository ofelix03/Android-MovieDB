package com.felix.moviedb.moviedb.models;

/**
 * Created by felix on 1/14/17.
 */

public class Genre {
    private int id;
    private String name;

    public Genre(){

    }

    public Genre(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
