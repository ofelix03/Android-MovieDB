package com.felix.moviedb.moviedb.models;

/**
 * Created by felix on 1/9/17.
 */

public class Movie {

    private String IMAGE_URL_PREFIX =  "https://image.tmdb.org/t/p/w500";
    private String image;
    private String title;
    private String rating;
    private String releaseDate;

    public Movie() {

    }

    public Movie(String image, String title, String rating, String releaseDate) {
        this.image = IMAGE_URL_PREFIX +  image;
        this.title = title;
        this.rating = rating;
        this.releaseDate = releaseDate;
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
}
