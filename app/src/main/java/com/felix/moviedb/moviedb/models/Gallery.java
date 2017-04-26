package com.felix.moviedb.moviedb.models;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by felix on 3/22/17.
 */

public class Gallery implements Serializable{
    private ArrayList<String> images = new ArrayList<>();
    public Gallery() {

    }

    public Gallery(String image) {
        this.images.add(image);
    }
    public void addImage(String image) {
        images.add(image);
    }

    public String getImage(int index) {
        return images.get(index);
    }

    public ArrayList<String> getImages() {
        return images;
    }
}
