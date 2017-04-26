package com.felix.moviedb.moviedb.models;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by felix on 1/16/17.
 */

public class Series implements Serializable{

    private ArrayList<TvSeason> series = new ArrayList<>();
    private int id;
    private String title;
    private String status;
    private String rating;
    private String language;
    private int seasons; // number of
    private ArrayList<Person> creators = new ArrayList<>();
    private String poster;
    private String[] productionCompanies = {};
    private String overview;
    private Cast cast = new Cast();
    private ArrayList<Genre> genres = new ArrayList<>();


    public Series() {

    }

    public Series(ArrayList<TvSeason> series) {
        this.series = series;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<TvSeason> getSeries() {
        return series;
    }

    public void setSeries(ArrayList<TvSeason> series) {
        this.series = series;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getRating() {
        return rating;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public int getSeasons() {
        return seasons;
    }

    public void setSeasons(int seasons) {
        this.seasons = seasons;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getPoster() {
        return poster;
    }

    public String[] getProductionCompanies() {
        return productionCompanies;
    }

    public void setProductionCompanies(String[] productionCompanies) {
        this.productionCompanies = productionCompanies;
    }

    public void addSeason(TvSeason season) {
        series.add(season);
    }

    public int getSeasonCount() {
        return series.size();
    }

    public TvSeason getSeason(int number) {
        return series.get(number - 1);
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public Cast getCast() {
        return cast;
    }

    public void setCast(Cast cast) {
        this.cast = cast;
    }

    public ArrayList<Person> getCreators() {
        return creators;
    }

    public int getCreatorsCount() {
        return creators.size();
    }

    public void setCreators(ArrayList<Person> creators) {
        this.creators = creators;
    }

    public void addCreator(Person person) {
        creators.add(person);
    }

    public ArrayList<Genre> getGenres() {
        return genres;
    }

    public void setGenres(ArrayList<Genre> genres) {
        this.genres = genres;
    }

    public void addGenre(Genre genre){
        genres.add(genre);
    }
}
