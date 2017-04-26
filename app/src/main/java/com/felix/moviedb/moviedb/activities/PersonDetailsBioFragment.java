package com.felix.moviedb.moviedb.activities;


import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.telecom.Call;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.felix.moviedb.moviedb.R;
import com.felix.moviedb.moviedb.adapters.TvShowRecyclerViewAdapter;
import com.felix.moviedb.moviedb.eventHandlers.PersonDetailsLoadedBroadcastReceiver;
import com.felix.moviedb.moviedb.models.Movie;
import com.felix.moviedb.moviedb.models.Person;

import java.util.ArrayList;

public class PersonDetailsBioFragment extends Fragment {
    private int personId;
    private Person person = new Person();
    private String biography;
    private TextView biographyView;
    public static String ACTION_DATA_LOADED = "com.felix.moviedb.moviedb.DATA_LOADED";
    private Callback callback;
    private RecyclerView personMovieCreditsView;


    public PersonDetailsBioFragment() {

    }


    public static PersonDetailsBioFragment newInstance(int personId) {
        PersonDetailsBioFragment fragment = new PersonDetailsBioFragment();
        Bundle args = new Bundle();
        args.putInt("personId", personId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("onCreate", "onCreate");
        if (getArguments() != null) {
           personId = getArguments().getInt("personId");
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            callback = (Callback) activity;
        } catch (ClassCastException e) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i("onCreateView", "onCreateView");
        registerDataLoadedBroadcastReceiver();
        View view =  inflater.inflate(R.layout.fragment_person_details_bio, container, false);
        biographyView = (TextView) view.findViewById(R.id.person_details_bio);
        return view;
    }

    public void registerDataLoadedBroadcastReceiver() {
        BroadcastReceiver dataLoadedBroadcastReceiver =  new DataLoadedBroadcastReceiver();
        IntentFilter intentFilter =  new IntentFilter();
        intentFilter.addAction(ACTION_DATA_LOADED);
        getActivity().registerReceiver(dataLoadedBroadcastReceiver, intentFilter);
    }

    public  class  DataLoadedBroadcastReceiver extends PersonDetailsLoadedBroadcastReceiver {

        @Override
        public void updateView(Context context, Intent intent) {
            Log.i("updateView", "UpdateViewCalled");
            biographyView.setText(intent.getStringExtra("personBioDetails"));
            person = callback.deserialize(PersonDetails2Activity.PERSON_OBJECT_FILENAME);
            biographyView.setText(person.getBiography());
        }
    }

    interface Callback {
        public void serialize(Person person, String filename);
        public Person deserialize(String filename);
    }

}
