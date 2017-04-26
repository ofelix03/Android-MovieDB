package com.felix.moviedb.moviedb.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.felix.moviedb.moviedb.R;
import com.felix.moviedb.moviedb.adapters.CastRecyclerViewAdapter;
import com.felix.moviedb.moviedb.adapters.PersonDetailsPageViewAdapter;
import com.felix.moviedb.moviedb.adapters.ViewPagerAdapter;
import com.felix.moviedb.moviedb.models.Cast;
import com.felix.moviedb.moviedb.models.Genre;
import com.felix.moviedb.moviedb.models.Movie;
import com.felix.moviedb.moviedb.models.Person;
import com.felix.moviedb.moviedb.services.VolleyFactory;
import com.felix.moviedb.moviedb.utils.TheMovieDBUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;

import static android.R.attr.bitmap;

public class MovieDetailsActivity extends AppCompatActivity implements CastRecyclerViewAdapter.Callback {

    private final Context context = this;
    private Toolbar toolbar;
    private int movieId;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private Movie movie = new Movie();

    private String MOVIE_DETAILS_URL;
    private String MOVIE_CAST_URL;

    private ImageView posterView;
    private TextView ratingView;
    private TextView statusView;
    private GridView creatorListView;
    private TextView languageView;
    private TextView genreView;
    private TextView overviewView;
    private RecyclerView castView;
    private TextView productionComapniesView;

    private ViewPager viewPager;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        Bundle args = getIntent().getExtras();

        setMovieId(args.getInt("movieId"));

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        toolbar = (Toolbar) findViewById(R.id.toolbar);


        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);

        collapsingToolbarLayout.setCollapsedTitleTextColor(getResources().getColor(android.R.color.holo_red_light));
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.holo_red_light));

        castView = (RecyclerView) findViewById(R.id.cast_recyclerview);
        castView.setNestedScrollingEnabled(false);
        GridLayoutManager castGridLayout =  new GridLayoutManager(this, 2, GridLayoutManager.HORIZONTAL, false);
        castView.setLayoutManager(castGridLayout);

//        viewPager = (ViewPager) findViewById(R.id.myviewpager);
//        viewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager()));
//
//        tabLayout = (TabLayout) findViewById(R.id.mytablayout);
//        tabLayout.setupWithViewPager(viewPager );


        getMovieDetails();
        getMovieCast();
    }

    public void setUpView() {

        collapsingToolbarLayout.setTitle(movie.getTitle());
        posterView = (ImageView) findViewById(R.id.poster);
        VolleyFactory.getInstance(context).getImageLoader().get(movie.getImage(), new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                Bitmap bitmap = response.getBitmap();
                posterView.setImageBitmap(bitmap);
            }

            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        creatorListView = (GridView) findViewById(R.id.creator_list);
        creatorListView.setVisibility(GridView.GONE);

        ratingView = (TextView) findViewById(R.id.rating);
        ratingView.setText(movie.getRating());

        statusView = (TextView) findViewById(R.id.status);
        statusView.setText(movie.getStatus());

        languageView = (TextView) findViewById(R.id.language);
        languageView.setText(movie.getLanguage());

        overviewView = (TextView) findViewById(R.id.overview_text);
        overviewView.setText(movie.getOverview());

        String genreString = "";
        ArrayList<Genre> genres = movie.getGenres();
        for(int i = 0; i < genres.size(); i++) {
            if (i == genres.size() - 1) {
                genreString += genres.get(i).getName();
            } else {
                genreString  += genres.get(i).getName() + ", ";
            }
        }
        genreView = (TextView) findViewById(R.id.genre);
        genreView.setText(genreString);


        String productions = "";
        String[] productionComapnies = movie.getProductionCompanies();
        for(int i = 0; i < productionComapnies.length; i++) {
            if (i == productionComapnies.length - 1) {
                productions += productionComapnies[i];
            } else {
                productions += productionComapnies[i] + ", ";
            }
        }

        productionComapniesView = (TextView) findViewById(R.id.production_companies);
        productionComapniesView.setText(productions);

    }

    private void setMovieId(int movieId) {
        this.movieId = movieId;

        MOVIE_DETAILS_URL = "https://api.themoviedb.org/3/movie/" + movieId + "h?api_key=f54dad4d1c97e5e9713189d86d100bf7&language=en-US";
        MOVIE_CAST_URL = "https://api.themoviedb.org/3/movie/" + movieId + "/credits?api_key=f54dad4d1c97e5e9713189d86d100bf7";
    }

    private void getMovieDetails() {
        JsonObjectRequest movieDetailsRequest = new JsonObjectRequest(MOVIE_DETAILS_URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Movie newMovie = movie;
                try {
                    newMovie.setId(response.getInt("id"));
                    newMovie.setRating(response.getString("vote_average"));
                    newMovie.setImage(response.getString("poster_path"));
                    newMovie.setLanguage(response.getString("original_language"));
                    newMovie.setTitle(response.getString("original_title"));
                    newMovie.setOverview(response.getString("overview"));
                    newMovie.setStatus(response.getString("status"));
                    newMovie.setReleaseDate(response.getString("release_date"));

                    JSONArray productionCompaniesArray = response.getJSONArray("production_companies");
                    String[] productionCompanies = new String[productionCompaniesArray.length()];
                    for(int i = 0; i < productionCompaniesArray.length(); i++) {
                        productionCompanies[i] = productionCompaniesArray.getJSONObject(i).getString("name");
                    }
                    newMovie.setProductionCompanies(productionCompanies);

                    JSONArray genreArray = response.getJSONArray("genres");
                    for(int i = 0; i < genreArray.length(); i++) {
                        JSONObject genreObject = genreArray.getJSONObject(i);
                        newMovie.addGenre(new Genre(genreObject.getInt("id"), genreObject.getString("name")));
                    }

                    // Set the movie to the movie instance
                    movie = newMovie;
                    setUpView();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("errorREsponse", error.toString());
            }
        });

        VolleyFactory.getInstance(context).getRequestQueue().add(movieDetailsRequest);
    }


    public void getMovieCast() {

        JsonObjectRequest castRequest = new JsonObjectRequest(MOVIE_CAST_URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Cast movieCast = new Cast();
                try {
                    JSONArray cast = response.getJSONArray("cast");
                    String name, avatar, character;
                    int id;
                    for(int i = 0; i < cast.length(); i++) {
                        JSONObject personObject = cast.getJSONObject(i);
                        name = personObject.getString("name");
                        id = personObject.getInt("id");
                        avatar = personObject.getString("profile_path");
                        character = personObject.getString("character");

                        movieCast.addPerson(new Person(id, name, character, avatar));
                    }

                    if (movie.getCast().getCount() == 0 ) {
                        movie.setCast(movieCast);
                        CastRecyclerViewAdapter movieCastAdapter = new CastRecyclerViewAdapter(context, movie.getCast(), context);
                        castView.setAdapter(movieCastAdapter);
                        movieCastAdapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("castError", error.toString());
            }
        });

        VolleyFactory.getInstance(context).getRequestQueue().add(castRequest);
    }

    @Override
    public void onCastClick(int castId) {
        Log.i("onCastClick", "called with cast id " + String.valueOf(castId));
        Intent intent = new Intent(this, PersonDetails2Activity.class);
        intent.putExtra("personId", castId);
        startActivity(intent);
    }
}
