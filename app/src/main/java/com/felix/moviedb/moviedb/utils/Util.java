package com.felix.moviedb.moviedb.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.AbsListView;

import com.felix.moviedb.moviedb.models.Genre;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by felix on 1/15/17.
 */

public class Util {
    private static SharedPreferences genreSharedPreference = null;
    private static Util instance = null;


    public static String GENRE_LIST_KEY = "genre_list";
    private Util() {
        instance = new Util();
    }

    public static Util getInstance() {
        if (instance == null) {
            instance =  new Util();
        }

        return instance;
    }

    /**
     * Helps with determining the Genre type during the serialization and deserialization
     * of ArrayList<Genre>
     *
     * @return Type
     */
    public static Type getGenreType() {
        Type genreType = new TypeToken<ArrayList<Genre>>(){}.getType();

        return genreType;
    }

    public static Gson getGson() {
        return new Gson();
    }

    public static SharedPreferences getGenreSaaredPreference(Context context) {

        if (genreSharedPreference == null) {
            genreSharedPreference = context.getSharedPreferences(GENRE_LIST_KEY, context.MODE_PRIVATE);
        };

        return genreSharedPreference;
    }
}





