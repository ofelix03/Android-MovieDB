package com.felix.moviedb.moviedb;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.design.widget.TabLayout;
import android.support.v4.content.SharedPreferencesCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;

//import com.android.volley.Request;
//import com.android.volley.Response;
//import com.android.volley.VolleyError;
//import com.android.volley.toolbox.JsonArrayRequest;
//import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.felix.moviedb.moviedb.adapters.ViewPagerAdapter;
import com.felix.moviedb.moviedb.models.Genre;
import com.felix.moviedb.moviedb.services.VolleyFactory;
import com.felix.moviedb.moviedb.utils.Util;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
//import com.felix.moviedb.moviedb.services.VolleyFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private Context context = this;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private String[] tabs = {"MOVIES", "TV SHOWS", "ACTORS"};
    public Toolbar myToolbar;
    public SharedPreferences genreSharedPreference;

    private String GENRE_LIST_URL = "https://api.themoviedb.org/3/genre/movie/list?api_key=f54dad4d1c97e5e9713189d86d100bf7&language=en-U";

    public static ArrayList<Genre> genreList = new ArrayList<Genre>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        genreSharedPreference = Util.getGenreSaaredPreference(this);

        // Setup toolbar
        myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager()));

        // Setup tabs
        tabLayout = (TabLayout) findViewById(R.id.tablayout);
        tabLayout.setupWithViewPager(viewPager);

        getGenres();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = new MenuInflater(context);
        menuInflater.inflate(R.menu.movie_list_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    public void getGenres() {

        final JsonObjectRequest genreListRequest = new JsonObjectRequest(Request.Method.GET, GENRE_LIST_URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i("response", response.toString());
                try {
                    JSONArray genres = response.getJSONArray("genres");

                    // Wrap genre in a Genre object
                    String genre_name;
                    int genre_id;

                    JSONObject genre;

                    for(int i = 0; i < genres.length(); i++) {
                        genre = genres.getJSONObject(i);
                        genre_name = genre.getString("name");
                        genre_id = genre.getInt("id");

                        genreList.add(new Genre(genre_id, genre_name));
                    }

                    saveToGenreSharedPreference(genreList);

             } catch (JSONException e) {
                    Log.i("error", "error");
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("error", error.toString());
            }
        });

        VolleyFactory.getInstance(this).getRequestQueue().add(genreListRequest);
    }


    public void saveToGenreSharedPreference(ArrayList<Genre> genreList) {

        Gson gson = Util.getGson();

        genreSharedPreference.edit().putString(Util.GENRE_LIST_KEY, gson.toJson(genreList, Util.getGenreType())).commit();
        Log.i("contains", String.valueOf(genreSharedPreference.contains(Util.GENRE_LIST_KEY)));
        Log.i("pref items", String.valueOf(genreSharedPreference.getAll()));
    }


}
