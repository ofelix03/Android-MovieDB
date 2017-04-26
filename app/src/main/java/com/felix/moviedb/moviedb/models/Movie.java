package com.felix.moviedb.moviedb.models;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by felix on 1/9/17.
 */

public class Movie implements Serializable {

    private String IMAGE_URL_PREFIX =  "https://image.tmdb.org/t/p/w500";
    private String image;
    private String title;
    private String rating;
    private String releaseDate;
    private String overview;
    private String[] productionCompanies = new String[] {};
    private ArrayList<Genre> genres = new ArrayList<>();
    private String language;
    private String status;
    private int id;
    private Cast cast = new Cast();

    public Movie() {

    }

    public Movie(int id, String image, String title, String rating, String releaseDate) {
        this.image = IMAGE_URL_PREFIX +  image;
        this.title = title;
        this.rating = rating;
        this.releaseDate = releaseDate;
        this.id = id;
    }

    public void setImage(String image) {
        this.image = IMAGE_URL_PREFIX + image;
    }

    public String getImage() {
        return image;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getRating() {
        return rating;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public void setGenres(ArrayList<Genre> genres) {
        this.genres = genres;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLanguage() {
        return language;
    }

    public String getStatus() {
        return status;
    }

    public ArrayList<Genre> getGenres() {
        return genres;
    }

    public String[] getProductionCompanies() {
        return productionCompanies;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void setProductionCompanies(String[] productionCompanies) {
        this.productionCompanies = productionCompanies;
    }

    public void addGenre(Genre genre) {
        genres.add(genre);
    }

    public void setCast(Cast cast) {
        this.cast = cast;
    }

    public Cast getCast() {
        return cast;
    }
}
