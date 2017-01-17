package com.felix.moviedb.moviedb.activities;

import android.content.Context;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.felix.moviedb.moviedb.R;
import com.felix.moviedb.moviedb.adapters.CastRecyclerViewAdapter;
import com.felix.moviedb.moviedb.adapters.SeasonListRecyclerViewAdapter;
import com.felix.moviedb.moviedb.models.Cast;
import com.felix.moviedb.moviedb.models.Person;
import com.felix.moviedb.moviedb.models.Series;
import com.felix.moviedb.moviedb.models.TvSeason;
import com.felix.moviedb.moviedb.services.VolleyFactory;
import com.felix.moviedb.moviedb.utils.TheMovieDBUtil;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TvShowDetailsActivity extends AppCompatActivity {
    private Context context = this;
    private Toolbar toolbar;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private int tvId = 1622; // tv series ID

    private final Series series = new Series();
    private final Cast cast = new Cast();
    private String SERIES_DETAILS_URL;

    private String SERIES_CAST_URL;
    private TextView  overviewTextView;


    private ImageView posterView;
    private TextView ratingView;
    private TextView statusView;
    private TextView createdByView;
    private TextView productionCompaniesView;
    private TextView languageView;
    private RecyclerView seasonListRecyclerView;
    private RecyclerView castRecyclerView;


    public String  getSeriesDetailsUrl() {
        SERIES_DETAILS_URL = "https://api.themoviedb.org/3/tv/" +  tvId + "?api_key=f54dad4d1c97e5e9713189d86d100bf7&language=en-US";

        return SERIES_DETAILS_URL;
    }

    public String getSeriesCastUrl() {
        SERIES_CAST_URL = "https://api.themoviedb.org/3/tv/" + tvId + "/credits?api_key=f54dad4d1c97e5e9713189d86d100bf7&language=en-US";

        return SERIES_CAST_URL;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tv_show_details);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tvId = getIntent().getIntExtra("tvId", tvId);

        seasonListRecyclerView = (RecyclerView) findViewById(R.id.season_list);
        GridLayoutManager seasonListGridLayout = new GridLayoutManager(this, 2, LinearLayoutManager.HORIZONTAL, false);
        seasonListRecyclerView.setLayoutManager(seasonListGridLayout);
        seasonListRecyclerView.setHasFixedSize(true);
        seasonListRecyclerView.setNestedScrollingEnabled(false);

        castRecyclerView = (RecyclerView) findViewById(R.id.cast_recyclerview);
        castRecyclerView.setNestedScrollingEnabled(false);
        GridLayoutManager castGridLayout =  new GridLayoutManager(this, 2, GridLayoutManager.HORIZONTAL, false);
        castRecyclerView.setLayoutManager(castGridLayout);

        setTitle("");


        getTvSeriesDetails();
        getTvSeriesCastData();
    }

    public void setUpView(Series series) {

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle(series.getTitle());

        collapsingToolbarLayout.setCollapsedTitleTextColor(getResources().getColor(android.R.color.holo_red_light));
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.holo_red_light));


        overviewTextView = (TextView) findViewById(R.id.overview_text);
        overviewTextView.setText(series.getOverview());

        languageView = (TextView) findViewById(R.id.language);
        languageView.setText(series.getLanguage());

        ratingView = (TextView) findViewById(R.id.rating);
        ratingView.setText(series.getRating());

        statusView = (TextView) findViewById(R.id.status);
        statusView.setText(series.getStatus());

        createdByView = (TextView) findViewById(R.id.created_by);
        createdByView.setText("Felix Otoo, Samuel Mensah, Henry Crook");

        productionCompaniesView = (TextView) findViewById(R.id.production_companies);

        String productionCompanies = "";
        for(int i = 0; i < series.getProductionCompanies().length; i++) {
            productionCompanies += series.getProductionCompanies()[i] + " ||  ";
        }
        productionCompaniesView.setText(productionCompanies);

        posterView = (ImageView) findViewById(R.id.poster);
        VolleyFactory.getInstance(this).getImageLoader().get(TheMovieDBUtil.IMAGE_URL + series.getPoster(), new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                posterView.setImageBitmap(response.getBitmap());
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("ImageLoadERror", error.toString());
            }
        });

        SeasonListRecyclerViewAdapter seasonListRecyclerViewAdapter = new SeasonListRecyclerViewAdapter(this, series.getSeries());
        seasonListRecyclerView.setAdapter(seasonListRecyclerViewAdapter);
        seasonListRecyclerViewAdapter.notifyDataSetChanged();

    }


    public void getTvSeriesDetails() {
        Log.i("getting", "tv series details now");
        Log.i("URL", getSeriesDetailsUrl());

        JsonObjectRequest tvSeriesDetailsRequest = new JsonObjectRequest(getSeriesDetailsUrl(), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i("response", response.toString());

                try {
                    series.setPoster(response.getString("backdrop_path"));
                    series.setLanguage(response.getString("original_language"));
                    series.setTitle(response.getString("original_name"));
                    series.setRating(response.getString("vote_average"));
                    series.setStatus(response.getString("status"));
                    series.setOverview(response.getString("overview"));
                    series.setId(response.getInt("id"));

                    JSONArray seasons = response.getJSONArray("seasons");
                    for(int i = 0; i < seasons.length(); i++) {
                          JSONObject seasonObject = seasons.getJSONObject(i);

                        series.addSeason(new TvSeason(seasonObject.getInt("season_number"), seasonObject.getInt("episode_count"), seasonObject.getString("poster_path")));
                    }

                    JSONArray productions = response.getJSONArray("production_companies");
                    String[] productionCompanies = new String[productions.length()];
                    for(int i = 0; i < productions.length(); i++) {
                        JSONObject productionObject = productions.getJSONObject(i);
                        productionCompanies[i] = productionObject.getString("name");
                    }

                    series.setProductionCompanies(productionCompanies);

                    Log.i("language", series.getLanguage());
                    Log.i("poster", series.getPoster());
                    Log.i("title", series.getTitle());
                    Log.i("size", String.valueOf(series.getSeasonCount()));


                    setUpView(series);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("error", error.toString());
            }
        });

        VolleyFactory.getInstance(this).getRequestQueue().add(tvSeriesDetailsRequest);
    }


    public void getTvSeriesCastData() {

        JsonObjectRequest castRequest = new JsonObjectRequest(getSeriesCastUrl(), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i("cast response", response.toString());
                try {
                    JSONArray castArray = response.getJSONArray("cast");
                    String actor_name, actor_avatar, actor_character;
                    int actor_id;

                    for(int i = 0; i < castArray.length(); i++) {
                        JSONObject actor = castArray.getJSONObject(i);
                        actor_name = actor.getString("name");
                        actor_avatar = actor.getString("profile_path");
                        actor_character = actor.getString("character");
                        actor_id = actor.getInt("id");

                        cast.addPerson(new Person(actor_id, actor_name, actor_character, actor_avatar));
                    }

                    if (series.getCast().getCount() == 0) {
                        // Series cast has not been set, let do that now
                        series.setCast(cast);
                    }

                    CastRecyclerViewAdapter castRecyclerViewAdapter = new CastRecyclerViewAdapter(context, cast);
                    castRecyclerView.setAdapter(castRecyclerViewAdapter);
                    castRecyclerViewAdapter.notifyDataSetChanged();

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

        VolleyFactory.getInstance(this.getApplicationContext()).getRequestQueue().add(castRequest);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = new MenuInflater(context);
        menuInflater.inflate(R.menu.movie_list_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
