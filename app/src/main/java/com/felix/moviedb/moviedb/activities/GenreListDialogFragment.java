package com.felix.moviedb.moviedb.activities;


import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.telecom.Call;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.felix.moviedb.moviedb.MainActivity;
import com.felix.moviedb.moviedb.R;
import com.felix.moviedb.moviedb.models.Genre;
import com.felix.moviedb.moviedb.utils.Util;

import java.io.Serializable;
import java.util.ArrayList;

public class GenreListDialogFragment extends DialogFragment{
    private Fragment parentFragment;
    private Callbacks callbacks;

    public interface Callbacks {
        public void onGenreSelected(int genreId);
    }

    public GenreListDialogFragment() {
        // Required empty public constructor
    }

    public static GenreListDialogFragment newInstance(Fragment parentFragment) {
        GenreListDialogFragment fragment = new GenreListDialogFragment();
        Bundle args = new Bundle();
        args.putSerializable("parentFragment", parentFragment.toString());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void setCallbacks(Callbacks callbacks) {
        this.callbacks = callbacks;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {


        final  ArrayList<Genre> genreList = Util.getGson().fromJson(Util.getGenreSaaredPreference(getContext()).getString(Util.GENRE_LIST_KEY, null), Util.getGenreType());
        final String[] genres = new String[genreList.size()];
        int i = 0;
        for(Genre genre: genreList) {
            genres[i] = genre.getName();
            i++;
        }

        ArrayAdapter arrayAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, genres);

        return new android.app.AlertDialog.Builder(getContext())
                .setTitle("Filter By Genre")
                .setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.i("item clicked", "item has position = " + i);
                        callbacks.onGenreSelected(genreList.get(i).getId());
                    }
                }).create();
    }
}


