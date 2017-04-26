package com.felix.moviedb.moviedb.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.felix.moviedb.moviedb.activities.PersonDetailsBioFragment;
import com.felix.moviedb.moviedb.activities.PersonMovieCreditsFragment;
import com.felix.moviedb.moviedb.activities.TvSeriesCreditsFragment;
import com.felix.moviedb.moviedb.activities.TvShowFragment;

/**
 * Created by felix on 3/1/17.
 */

public class PersonDetailsPageViewAdapter extends FragmentPagerAdapter {
    private int personId;

    public PersonDetailsPageViewAdapter(FragmentManager fm, int personId) {
        super(fm);
        this.personId = personId;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        if (position == 0) {
            fragment = PersonDetailsBioFragment.newInstance(personId);
        } else if (position == 1) {
            fragment = PersonMovieCreditsFragment.newInstance();
        } else if (position == 2) {
            fragment = TvSeriesCreditsFragment.newInstance();
        }

        return fragment;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String title = new String();
        if (position == 0) {
            title =  "BIO";
        } else if (position == 1) {
            title =  "MOVIES";
        } else if (position == 2) {
            title = "TV SHOWS";
        }

        return title;
    }
}