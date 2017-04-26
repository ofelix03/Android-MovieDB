package com.felix.moviedb.moviedb.activities;

import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.felix.moviedb.moviedb.R;
import com.felix.moviedb.moviedb.adapters.PersonDetailsPageViewAdapter;
import com.felix.moviedb.moviedb.models.Movie;
import com.felix.moviedb.moviedb.models.Person;
import com.felix.moviedb.moviedb.models.Series;
import com.felix.moviedb.moviedb.services.VolleyFactory;
import com.felix.moviedb.moviedb.utils.TheMovieDBUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class PersonDetails2Activity extends AppCompatActivity  implements PersonDetailsBioFragment.Callback,
                                                                        PersonMovieCreditsFragment.Callback,
                                                                        TvSeriesCreditsFragment.Callback
                                                                        {

    private ViewPager viewPager;
    private TabLayout tabLayout;
    private TextView nameView;
    private ImageView avatarView;
    private TextView birthDateView;
    private TextView birthPlaceView;
    private TextView deathDateView;
    private Toolbar toolbarView;
    private PersonDetailsPageViewAdapter personDetailsPageViewAdapter;
    private CollapsingToolbarLayout collapsingToolbarLayoutView;
    private Person person = new Person();
    private Boolean dataLoaded = false;
    public static String PERSON_OBJECT_FILENAME = "person_object.txt";


    private int personId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("onCreateMainact", "onCreateMainAct()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_details2);
        personId = getIntent().getIntExtra("personId", 0);

        toolbarView= (Toolbar) findViewById(R.id.person_details_toolbar);
        setSupportActionBar(toolbarView);
        viewPager = (ViewPager) findViewById(R.id.person_details_pager);
        personDetailsPageViewAdapter = new PersonDetailsPageViewAdapter(getSupportFragmentManager(), personId);
        viewPager.setAdapter(personDetailsPageViewAdapter);
        viewPager.setOffscreenPageLimit(3);
        tabLayout = (TabLayout) findViewById(R.id.person_details_tablayout);
        tabLayout.setupWithViewPager(viewPager);

        collapsingToolbarLayoutView = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayoutView.setTitleEnabled(false);

        setTitle("");

        nameView = (TextView) findViewById(R.id.person_details_name);
        avatarView = (ImageView) findViewById(R.id.person_details_avatar);
        birthPlaceView = (TextView) findViewById(R.id.person_details_birthplace);
        birthDateView = (TextView) findViewById(R.id.person_details_birthdate);
        deathDateView = (TextView) findViewById(R.id.person_details_deathdate);

        getDetails();
    }

    public void seedDetailsView() {
        nameView.setText(person.getName().toString());
        birthPlaceView.setText(person.getBirthPlace());
        birthDateView.setText(person.getBirthDate());

        if (person.getDeathDate() != " ") {
            deathDateView.setText((person.getDeathDate()));
        } else {
            deathDateView.setText("N/A");
        }
        VolleyFactory.getInstance(this.getApplicationContext()).getImageLoader().get(TheMovieDBUtil.IMAGE_URL + person.getAvatar(), new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                avatarView.setImageBitmap(response.getBitmap());
            }

            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
    }

    public void getDetails() {
        Log.i("fetching", "fetching person details for id " + personId);
        final JsonObjectRequest personDetailsRequest = new JsonObjectRequest(getPersonDetailsUrl(), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i("response", response.toString());

                try {
                    int id = response.getInt("id");
                    String name = response.getString("name");
                    String birthPlace = response.getString("place_of_birth");
                    String birthDate = response.getString("birthday");
                    String deathDate = response.getString("deathday");
                    String bio = response.getString("biography");
                    String avatar = response.getString("profile_path");

                    person.setId(id);
                    person.setAvatar(avatar);
                    person.setBiography(bio);
                    person.setName(name);
                    person.setBirthPlace(birthPlace);
                    person.setBirthDate(birthDate);
                    person.setDeathDate(deathDate);

                    JSONArray personMovieCreditsArray = response.getJSONObject("movie_credits").getJSONArray("cast");
                    JSONArray personSeriesCreditsArray = response.getJSONObject("tv_credits").getJSONArray("cast");
                    for (int i = 0; i < personMovieCreditsArray.length(); i++) {
                        JSONObject  movieObject = personMovieCreditsArray.getJSONObject(i);
                        Movie movie = new Movie();
                        movie.setImage(movieObject.getString("poster_path"));
                        movie.setTitle(movieObject.getString("original_title"));
                        movie.setReleaseDate(movieObject.getString("release_date"));
                        movie.setId(movieObject.getInt("id"));
                        person.addMovieCredit(movie);
                    }

                    for (int j = 0; j < personSeriesCreditsArray.length(); j++) {
                        JSONObject movieObject = personSeriesCreditsArray.getJSONObject(j);
                        Log.i("tvseries", personSeriesCreditsArray.toString());
                        Movie movie = new Movie();
                        movie.setId(movieObject.getInt("id"));
                        movie.setTitle(movieObject.getString("name"));
                        movie.setImage(movieObject.getString("poster_path"));
                        movie.setReleaseDate((movieObject.getString("first_air_date")));
                        person.addSeriesMovieCredit(movie);
                    }

                    Log.i("personDetails", person.toString());
                    Log.i("personMovieCred", person.getMovieCredits().toString());
                    Log.i("personSeriesCred", person.getSeriesMovieCredits().toString());

                    seedDetailsView();
                    dataLoaded = true;
                    serialize(person, PERSON_OBJECT_FILENAME);
                    Intent personBioDetailsIntent = new Intent();
                    personBioDetailsIntent.setAction(PersonDetailsBioFragment.ACTION_DATA_LOADED);
                    sendBroadcast(personBioDetailsIntent);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("errorResponse", error.toString());
            }
        });

        VolleyFactory.getInstance(this.getApplicationContext()).getRequestQueue().add(personDetailsRequest);
    }

    public String getPersonDetailsUrl() {
        return "https://api.themoviedb.org/3/person/" + personId + "?api_key=f54dad4d1c97e5e9713189d86d100bf7&language=en-US&append_to_response=movie_credits,tv_credits,images";
    }

    public ArrayList<Movie> getMovieCredits() {
        Log.i("called", "getMovieCredits()");
        return person.getMovieCredits();
    }

    public ArrayList<Movie> getSeriesCredits() {
        Log.i("called", "getTvSereisCredit()");
        return person.getSeriesMovieCredits();
    }

    public void serialize(Person person, String filename) {
        try {
            FileOutputStream fos = openFileOutput(filename, MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(person);
            oos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Person deserialize(String filename) {
        Person personObject = null;
        try {
            FileInputStream fis = openFileInput(filename);
            ObjectInputStream ois = new ObjectInputStream(fis);
            personObject = (Person) ois.readObject();
            ois.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return personObject;
    }
}
