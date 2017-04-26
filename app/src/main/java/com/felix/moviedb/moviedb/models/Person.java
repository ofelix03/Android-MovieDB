package com.felix.moviedb.moviedb.models;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by felix on 1/16/17.
 */


public class Person implements Serializable {

    private int id;
    private String name;
    private String avatar;
    private String character;
    private String biography;
    private String birthDate;
    private String deathDate;
    private String birthPlace;
    private ArrayList<Movie> movieCredits = new ArrayList<>();
    private ArrayList<Movie> seriesMovieCredits = new ArrayList<>();
    private Gallery gallery = new Gallery();
    public Person() {

    }

    public Person(int id, String name, String character, String poster) {
        this.name = name;
        this.avatar = poster;
        this.id = id;
        this.character = character;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCharacter() {
        return character;
    }

    public void setCharacter(String character) {
        this.character = character;
    }

    public void setBiography(String bio) {
        this.biography =  bio;
    }

    public String getBiography() {
        return this.biography;
    }

    public void setBirthDate(String date) {
        this.birthDate = date;
    }

    public String getBirthDate() {
        return this.birthDate;
    }

    public void setBirthPlace(String place) {
        this.birthPlace = place;
    }

    public String getBirthPlace() {
        return birthPlace;
    }

    public void setDeathDate(String date) {
        this.deathDate = date;
    }

    public String getDeathDate() {
        return this.deathDate;
    }

    public void setMovieCredits(ArrayList<Movie> credits) {
        this.movieCredits = credits;
    }

    public void addMovieCredit(Movie movie) {
        movieCredits.add(movie);
    }

    public ArrayList<Movie> getMovieCredits() {
        return movieCredits;
    }

    public void setSeriesMovieCredits(ArrayList<Movie> movies) {
        this.seriesMovieCredits = movies;
    }

    public void addSeriesMovieCredit(Movie movie) {
        seriesMovieCredits.add(movie);
    }
    public ArrayList<Movie> getSeriesMovieCredits() {
        return this.seriesMovieCredits;
    }

    public void addGallery(Gallery gallery) {
        this.gallery = gallery;
    }

    public void addImage(String image) {
        this.gallery.addImage(image);
    }

    public Gallery getGallery() {
        return this.gallery;
    }

    public String getImage(int index) {
        return this.gallery.getImage(index);
    }
}
