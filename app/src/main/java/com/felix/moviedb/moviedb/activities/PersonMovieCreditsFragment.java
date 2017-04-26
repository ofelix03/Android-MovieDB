package com.felix.moviedb.moviedb.activities;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.felix.moviedb.moviedb.R;
import com.felix.moviedb.moviedb.adapters.MovieRecyclerViewAdapter;
import com.felix.moviedb.moviedb.adapters.TvShowRecyclerViewAdapter;
import com.felix.moviedb.moviedb.eventHandlers.PersonDetailsLoadedBroadcastReceiver;
import com.felix.moviedb.moviedb.interfaces.MovieViewHolderCallback;
import com.felix.moviedb.moviedb.models.Movie;
import com.felix.moviedb.moviedb.models.Person;
import com.felix.moviedb.moviedb.services.VolleyFactory;

import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;

import static com.felix.moviedb.moviedb.activities.PersonDetailsBioFragment.ACTION_DATA_LOADED;

public class PersonMovieCreditsFragment extends Fragment {
    private Person person = new Person();
    private Callback callback = null;
    private MovieViewHolderCallback movieItemClickedCallback = new MovieViewHolderCallback() {

        @Override
        public void onImageClick(View view, int tvId) {
            Intent movieDetailsIntent = new Intent(getActivity(), MovieDetailsActivity.class);
            movieDetailsIntent.putExtra("movieId", tvId);
            startActivity(movieDetailsIntent);
        }
    };

    private RecyclerView personMovieCreditsView;

    private MovieViewHolderCallback movieViewHolderCallback = new MovieViewHolderCallback() {
        @Override
        public void onImageClick(View view, int tvId) {
            Intent tvShowDetailsIntent = new Intent(getActivity(), TvShowDetailsActivity.class);
            tvShowDetailsIntent.putExtra("tvId", tvId);
            startActivity(tvShowDetailsIntent);
        }
    };

    public PersonMovieCreditsFragment() {
        // Required empty public constructor
    }

    public static PersonMovieCreditsFragment newInstance() {
        return  new PersonMovieCreditsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        registerDataLoadedBroadcastReceiver();

        View view  =  inflater.inflate(R.layout.fragment_person_movie_credits, container, false);

        personMovieCreditsView = (RecyclerView) view.findViewById(R.id.person_movie_credits);
        personMovieCreditsView.setHasFixedSize(false);
        personMovieCreditsView.setNestedScrollingEnabled(false);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        personMovieCreditsView.setLayoutManager((staggeredGridLayoutManager));

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            callback = (Callback) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity shoud implement PersonMovieCreditsFragment.Callback interface");
        }
    }

    public void registerDataLoadedBroadcastReceiver() {
        BroadcastReceiver dataLoadedBroadcastReceiver =  new DataLoadedBroadcastReceiver();
        IntentFilter intentFilter =  new IntentFilter();
        intentFilter.addAction(ACTION_DATA_LOADED);
        getActivity().registerReceiver(dataLoadedBroadcastReceiver, intentFilter);
    }

    interface Callback {
        public void serialize(Person person, String filename);
        public Person deserialize(String filename);

    }

    public  class  DataLoadedBroadcastReceiver extends PersonDetailsLoadedBroadcastReceiver {

        @Override
        public void updateView(Context context, Intent intent) {
            Log.i("updateView", "UpdateViewCalled for movie credits");
            person = callback.deserialize(PersonDetails2Activity.PERSON_OBJECT_FILENAME);
            MovieRecyclerViewAdapter movieCreditsAdapter = new MovieRecyclerViewAdapter(getActivity(), person.getMovieCredits());
            personMovieCreditsView.setAdapter(movieCreditsAdapter);
            movieCreditsAdapter.setMovieViewHolderCallback(movieItemClickedCallback);
            movieCreditsAdapter.notifyDataSetChanged();
        }
    }


}
