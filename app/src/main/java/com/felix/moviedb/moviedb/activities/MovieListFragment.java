package com.felix.moviedb.moviedb.activities;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.felix.moviedb.moviedb.R;
import com.felix.moviedb.moviedb.adapters.MovieRecyclerViewAdapter;
import com.felix.moviedb.moviedb.adapters.NewMovieRecyclerViewAdapter;
import com.felix.moviedb.moviedb.interfaces.MovieViewHolderCallback;
import com.felix.moviedb.moviedb.models.Movie;
import com.felix.moviedb.moviedb.services.VolleyFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MovieListFragment extends Fragment implements GenreListDialogFragment.Callbacks {
    private int position;
    private RecyclerView movieListRecyclerView;
    private RecyclerView movieListNewRecyclerView;

    private ArrayList<Movie> movies = new ArrayList<Movie>();
    private ArrayList<Movie> newMovies = new ArrayList<>();

    private String DISCOVER_URL = "https://api.themoviedb.org/3/discover/movie?api_key=f54dad4d1c97e5e9713189d86d100bf7";
    private String MOVIES_URL = "https://api.themoviedb.org/3/movie/popular?api_key=f54dad4d1c97e5e9713189d86d100bf7";
    private String NEW_MOVIES_URL = "https://api.themoviedb.org/3/movie/upcoming?api_key=f54dad4d1c97e5e9713189d86d100bf7";

    private Context context = getContext();

    private FragmentManager fragmentManager;

    public MovieListFragment() {
        // Required empty public constructor
    }

    public static MovieListFragment newInstance(int position) {
        MovieListFragment fragment = new MovieListFragment();
        Bundle args = new Bundle();
        args.putInt("position", position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentManager = getFragmentManager();
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            position = getArguments().getInt("position");
        }
        getMovies();
        getNewMovies();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_list, container, false);

        movieListRecyclerView = (RecyclerView)  view.findViewById(R.id.movie_list_recyclerview);
        movieListNewRecyclerView = (RecyclerView) view.findViewById(R.id.movies_latest_recyclerview);

        movieListNewRecyclerView.setHasFixedSize(true);
        movieListRecyclerView.setHasFixedSize(true);
        movieListRecyclerView.setNestedScrollingEnabled(false);
        movieListNewRecyclerView.setNestedScrollingEnabled(false);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);

        movieListNewRecyclerView.setLayoutManager(linearLayoutManager);
        movieListNewRecyclerView.setAdapter(new NewMovieRecyclerViewAdapter(context, newMovies));

        movieListRecyclerView.setLayoutManager(staggeredGridLayoutManager);
        movieListNewRecyclerView.setAdapter(new MovieRecyclerViewAdapter(context, movies));

        return view;
    }


    public void getMovies() {
        Log.i("getting", "movines now");

        final JsonObjectRequest moviesRequest = new JsonObjectRequest(Request.Method.GET, MOVIES_URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i("response", response.toString());
                int page = 0;
                try {
                    page = response.getInt("page");
                    JSONArray results = response.getJSONArray("results");

                    // Wrap movies in a Movie object
                    String image, title, rating, releaseDate;
                    int id;
                    JSONObject movie;

                    for(int i = 0; i < results.length(); i++) {
                        movie = results.getJSONObject(i);
                        image = movie.getString("backdrop_path");
                        title = movie.getString("title");
                        rating = movie.getString("vote_average");
                        releaseDate = movie.getString("release_date");
                        id = movie.getInt("id");
                        movies.add(new Movie(id, image, title, rating, releaseDate));
                    }
                    Log.i("page", String.valueOf(page));
                    Log.i("results", results.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                MovieRecyclerViewAdapter movieRecyclerViewAdapter = new MovieRecyclerViewAdapter(context, movies);
                movieRecyclerViewAdapter.setMovieViewHolderCallback(new MovieViewHolderCallback() {
                    @Override
                    public void onImageClick(View view, int tvId) {
                        Intent movieDetailsIntent = new Intent(getActivity(), MovieDetailsActivity.class);
                        movieDetailsIntent.putExtra("movieId", tvId);
                        startActivity(movieDetailsIntent);
                    }
                });
                movieListRecyclerView.setAdapter(movieRecyclerViewAdapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("error", error.toString());
            }
        });
        VolleyFactory.getInstance(getActivity()).getRequestQueue().add(moviesRequest);
    }


    public void getNewMovies() {
        Log.i("getting", "movines now");

        final JsonObjectRequest newMoviesRequest = new JsonObjectRequest(Request.Method.GET, NEW_MOVIES_URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i("response", response.toString());
                int page = 0;
                try {
                    page = response.getInt("page");
                    JSONArray results = response.getJSONArray("results");

                    // Wrap movies in a Movie object
                    String image, title, rating, releaseDate;
                    int id;
                    JSONObject movie;

                    for(int i = 0; i < results.length(); i++) {
                        movie = results.getJSONObject(i);
                        image = movie.getString("backdrop_path");
                        title = movie.getString("title");
                        rating = movie.getString("vote_average");
                        releaseDate = movie.getString(("release_date"));
                        id = movie.getInt("id");
                        newMovies.add(new Movie(id, image, title, rating, releaseDate));
                    }
                    Log.i("page", String.valueOf(page));
                    Log.i("new movies results", results.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                SharedPreferences sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
                sharedPreferences.edit().putInt("new movies_page", page).commit();
                Log.i("new movies", String.valueOf(newMovies.size()));
                NewMovieRecyclerViewAdapter movieRecyclerViewAdapter = new NewMovieRecyclerViewAdapter(context, newMovies);
                movieRecyclerViewAdapter.setMovieViewHolderCallback(new MovieViewHolderCallback() {
                    @Override
                    public void onImageClick(View view, int tvId) {
                        Log.i("ImageClicked", "Image with id " + tvId + "has been clicked");
                        Intent movieDetailsIntent = new Intent(getActivity(), MovieDetailsActivity.class);
                        movieDetailsIntent.putExtra("movieId", tvId);
                        startActivity(movieDetailsIntent);
                    }
                });
                movieListNewRecyclerView.setAdapter(movieRecyclerViewAdapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("error", error.toString());
            }
        });

        VolleyFactory.getInstance(getActivity()).getRequestQueue().add(newMoviesRequest);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.i("item selectd", String.valueOf(item.getItemId()));

        if (item.getItemId() == R.id.action_filter) {
            showGenreListPopupMenu(getActivity().findViewById(R.id.action_filter));
        } else if (item.getItemId() == R.id.search_icon) {
            Intent intent = new Intent(getActivity(), SearchResultActivity.class);
            startActivity(intent);
        }

        return true;
    }

    public void showGenreListPopupMenu(View view) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        if (fragmentManager.findFragmentByTag("genre-dialog") != null) {
            // Remove all fragment
            ft.remove(fragmentManager.findFragmentByTag("genre-dialog"));
            ft.addToBackStack(null);
        }

        // Create own genre dialog
        GenreListDialogFragment genreListDialogFragment = GenreListDialogFragment.newInstance(this);
        genreListDialogFragment.setCallbacks(this);
        genreListDialogFragment.show(getFragmentManager(), "genre-dialog");
    }


    @Override
    public void onGenreSelected(int genreId) {
        Log.i("onGenreSelectred", "Yaah we've been called with genre " + genreId);
        getMoviesByGenre(genreId);
    }


    public void getMoviesByGenre(int genreId) {
        Log.i("getting", "movines now");

        final JsonObjectRequest moviesByGenreRequest = new JsonObjectRequest(Request.Method.GET, DISCOVER_URL + "&with_genres=" + genreId, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                // Let clear out the old movies
                movies.clear();

                Log.i("response", response.toString());
                try {
                    JSONArray results = response.getJSONArray("results");

                    // Wrap movies in a Movie object
                    String image, title, rating, releaseDate;
                    int id;
                    JSONObject movie;

                    for(int i = 0; i < results.length(); i++) {
                        movie = results.getJSONObject(i);
                        image = movie.getString("backdrop_path");
                        title = movie.getString("title");
                        rating = movie.getString("vote_average");
                        releaseDate = movie.getString(("release_date"));
                        id = movie.getInt("id");

                        movies.add(new Movie(id, image, title, rating, releaseDate));
                    }

                    Log.i("hmm movies results", results.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Log.i("new movies", String.valueOf(movies.size()));
                MovieRecyclerViewAdapter movieRecyclerViewAdapter = new MovieRecyclerViewAdapter(context, movies);
                movieListRecyclerView.setAdapter(movieRecyclerViewAdapter);
                movieRecyclerViewAdapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("error", error.toString());
            }
        });

        VolleyFactory.getInstance(getActivity()).getRequestQueue().add(moviesByGenreRequest);
    }

}
