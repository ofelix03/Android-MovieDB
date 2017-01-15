package com.felix.moviedb.moviedb.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import com.felix.moviedb.moviedb.activities.MovieListFragment;
import com.felix.moviedb.moviedb.activities.PersonFragment;
import com.felix.moviedb.moviedb.activities.TvShowFragment;


/**
 * Created by felix on 1/8/17.
 */

public class ViewPagerAdapter  extends FragmentStatePagerAdapter {
    private int NUM_OF_PAGERS = 3;
    private String[] tabs = {"MOVIES", "TV SHOWS", "PERSONS"};

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Log.i("getItem", "position is " + position);
        Fragment frag = MovieListFragment.newInstance(position);

        if (position == 0) {
            Log.i("pso", "0");
            frag = MovieListFragment.newInstance(position);
        } else if (position == 1) {
            Log.i("pos", "tv show");
//            frag = MovieListFragment.newInstance(position);
            frag =  TvShowFragment.newInstance(position);
        } else if (position == 2) {
            Log.i("post", "2");
            frag = MovieListFragment.newInstance(position);
//            frag =  PersonFragment.newInstance(position);
        }

        return frag;
    }

    @Override
    public int getCount() {
        return NUM_OF_PAGERS;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabs[position];
    }
}
