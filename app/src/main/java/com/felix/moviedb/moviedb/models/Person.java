package com.felix.moviedb.moviedb.models;

/**
 * Created by felix on 1/16/17.
 */

public class Person {

    private int id;
    private String name;
    private String avatar;
    private String character;

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
}
