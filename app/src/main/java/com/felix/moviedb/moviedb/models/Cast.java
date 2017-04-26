package com.felix.moviedb.moviedb.models;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by felix on 1/16/17.
 */

public class Cast  implements Serializable {
    private ArrayList<Person> cast = new ArrayList<>();



    public Cast() {

    }

    public Cast(ArrayList<Person> cast) {
        this.cast = cast;
    }

    public ArrayList<Person> getCast() {
        return cast;
    }

    public void addPerson(Person person) {
        cast.add(person);
    }

    public int getCount() {
        return cast.size();
    }


}
