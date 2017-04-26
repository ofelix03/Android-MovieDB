package com.felix.moviedb.moviedb.activities;


import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.felix.moviedb.moviedb.R;
import com.felix.moviedb.moviedb.adapters.TvShowRecyclerViewAdapter;
import com.felix.moviedb.moviedb.eventHandlers.PersonDetailsLoadedBroadcastReceiver;
import com.felix.moviedb.moviedb.interfaces.MovieViewHolderCallback;
import com.felix.moviedb.moviedb.models.Person;

import static com.felix.moviedb.moviedb.activities.PersonDetailsBioFragment.ACTION_DATA_LOADED;


public class TvSeriesCreditsFragment extends Fragment {
    private Person person = null;
    private RecyclerView tvCreditRecyclerView;
    private Callback callback = null;
    private MovieViewHolderCallback movieViewHolderCallback = new MovieViewHolderCallback() {
        @Override
        public void onImageClick(View view, int tvId) {
            Intent tvShowDetailsIntent = new Intent(getActivity(), TvShowDetailsActivity.class);
            tvShowDetailsIntent.putExtra("tvId", tvId);
            startActivity(tvShowDetailsIntent);
        }
    };



    public TvSeriesCreditsFragment() {
        // Required empty public constructor
    }

    public static TvSeriesCreditsFragment newInstance() {
        return new TvSeriesCreditsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        registerDataLoadedBroadcastReceiver();
        View view =  inflater.inflate(R.layout.fragment_tv_series_credits, container, false);

        tvCreditRecyclerView = (RecyclerView) view.findViewById(R.id.person_tv_credits);
        tvCreditRecyclerView.setHasFixedSize(false);
        tvCreditRecyclerView.setNestedScrollingEnabled(false);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        tvCreditRecyclerView.setLayoutManager((staggeredGridLayoutManager));

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            callback = (TvSeriesCreditsFragment.Callback) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity shoud implement PersonMovieCreditsFragment.Callback interface");
        }
    }

    public void registerDataLoadedBroadcastReceiver() {
        BroadcastReceiver dataLoadedBroadcastReceiver =  new TvSeriesCreditsFragment.DataLoadedBroadcastReceiver();
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
            Log.i("updateView", "UpdateViewCalled for tv credits");
            person = callback.deserialize(PersonDetails2Activity.PERSON_OBJECT_FILENAME);
            TvShowRecyclerViewAdapter movieCreditsAdapter = new TvShowRecyclerViewAdapter(getActivity(), person.getSeriesMovieCredits());
            movieCreditsAdapter.setMovieViewHolderCallback(movieViewHolderCallback);
            tvCreditRecyclerView.setAdapter(movieCreditsAdapter);
            movieCreditsAdapter.notifyDataSetChanged();
        }
    }
}
