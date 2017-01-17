package com.felix.moviedb.moviedb.activities;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.felix.moviedb.moviedb.R;
import com.felix.moviedb.moviedb.adapters.MovieRecyclerViewAdapter;
import com.felix.moviedb.moviedb.adapters.TvShowRecyclerViewAdapter;
import com.felix.moviedb.moviedb.models.Movie;
import com.felix.moviedb.moviedb.services.VolleyFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class TvShowFragment extends Fragment {
    private int position;
    private RecyclerView tvShowRecyclerView;
    private ArrayList<Movie> tvShows = new ArrayList<>();

    private String TV_SHOWS_URL = "https://api.themoviedb.org/3/tv/popular?api_key=f54dad4d1c97e5e9713189d86d100bf7&language=en-US&page=1";
    private int currentPage = 1;

    private TvShowRecyclerViewAdapter.MovieViewHolderCallbacks  movieViewHolderCallbacks = new TvShowRecyclerViewAdapter.MovieViewHolderCallbacks() {
        @Override
        public void onImageClick(View view, int tvId) {
            Log.i("felixclicked", "felix clicked");
            Log.i("tvID", String.valueOf(tvId));
            Intent tvShowDetailsIntent = new Intent(getActivity(), TvShowDetailsActivity.class);
            tvShowDetailsIntent.putExtra("tvId", tvId);
            startActivity(tvShowDetailsIntent);
        }
    };

    public TvShowFragment() {
        // Required empty public constructor
    }

    public static TvShowFragment newInstance(int position) {
        TvShowFragment fragment = new TvShowFragment();
        Bundle args = new Bundle();
        args.putInt("position", position);
        fragment.setArguments(args);
        Log.i("starting", "tv show fragment");
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            position = getArguments().getInt("position");
        }

        getTvShows();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_tv_show, container, false);
        tvShowRecyclerView = (RecyclerView) view.findViewById(R.id.tv_shows_recyclerview);
        tvShowRecyclerView.setHasFixedSize(true);
        tvShowRecyclerView.setNestedScrollingEnabled(false);

        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        tvShowRecyclerView.setLayoutManager(staggeredGridLayoutManager);


        return view;
    }

    public void getTvShows() {
        Log.i("getting", "tv show now");

        final JsonObjectRequest tvShowsRequest = new JsonObjectRequest(Request.Method.GET, TV_SHOWS_URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i("response", response.toString());
                int page = 0;
                try {
                    currentPage = response.getInt("page");
                    JSONArray results = response.getJSONArray("results");

                    // Wrap movies in a Movie object
                    String image, title, rating, releaseDate;
                    int id;
                    JSONObject movie;

                    for(int i = 0; i < results.length(); i++) {
                        movie = results.getJSONObject(i);
                        image = movie.getString("poster_path");
                        title = movie.getString("name");
                        rating = movie.getString("vote_average");
                        releaseDate = movie.getString(("first_air_date"));
                        id = movie.getInt("id");

                        tvShows.add(new Movie(id, image, title, rating, releaseDate));
                    }

                    Log.i("tvshows", tvShows.toString());

                    Log.i("page", String.valueOf(page));
                    Log.i("tv shows", results.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                TvShowRecyclerViewAdapter tvShowAdapter= new TvShowRecyclerViewAdapter(getContext(), tvShows);
                tvShowAdapter.setMovieViewHolderCallbacks(movieViewHolderCallbacks);
                tvShowRecyclerView.setAdapter(tvShowAdapter);
                tvShowAdapter.notifyDataSetChanged();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("error", error.toString());
            }
        });

        VolleyFactory.getInstance(getActivity()).getRequestQueue().add(tvShowsRequest);
    }



}
