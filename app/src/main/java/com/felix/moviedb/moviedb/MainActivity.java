package com.felix.moviedb.moviedb;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ProgressBar;


import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.felix.moviedb.moviedb.adapters.ViewPagerAdapter;
import com.felix.moviedb.moviedb.models.Genre;
import com.felix.moviedb.moviedb.services.VolleyFactory;
import com.felix.moviedb.moviedb.utils.Util;
import com.google.gson.Gson;
//import com.felix.moviedb.moviedb.services.VolleyFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Context context = this;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private String[] tabs = {"MOVIES", "TV SHOWS", "ACTORS"};
    public Toolbar myToolbar;
    public SharedPreferences genreSharedPreference;
    public ProgressBar progressBar;

    private String GENRE_LIST_URL = "https://api.themoviedb.org/3/genre/movie/list?api_key=f54dad4d1c97e5e9713189d86d100bf7&language=en-U";

    public static ArrayList<Genre> genreList = new ArrayList<Genre>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        genreSharedPreference = Util.getGenreSaaredPreference(this);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);

        // Setup toolbar
        myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        viewPager = (ViewPager) findViewById(R.id.person_details_pager);
        viewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager()));

        // Setup tabs
        tabLayout = (TabLayout) findViewById(R.id.tablayout);
        tabLayout.setupWithViewPager(viewPager);

        getGenres();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.movie_list_menu, menu);
        return true;
    }


    public void getGenres() {
        if (genreSharedPreference.getAll() != null) {
            return;
        }

        final JsonObjectRequest genreListRequest = new JsonObjectRequest(Request.Method.GET, GENRE_LIST_URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
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
    }


}
