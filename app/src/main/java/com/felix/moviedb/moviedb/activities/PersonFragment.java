package com.felix.moviedb.moviedb.activities;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.felix.moviedb.moviedb.R;
import com.felix.moviedb.moviedb.adapters.CastRecyclerViewAdapter;
import com.felix.moviedb.moviedb.adapters.PersonListRecyclerAdapter;
import com.felix.moviedb.moviedb.interfaces.EndlessScrollListener;
import com.felix.moviedb.moviedb.models.Person;
import com.felix.moviedb.moviedb.services.VolleyFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PersonFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PersonFragment extends Fragment implements PersonListRecyclerAdapter.PersonViewHolderCallback {
    private int position;
    private RecyclerView personListRecyclerView;
    private ArrayList<Person> persons = new ArrayList<>();
    private String PERSONS_URL = "https://api.themoviedb.org/3/person/popular?api_key=f54dad4d1c97e5e9713189d86d100bf7";
    private int currentPage = 0;
    public PersonFragment() {

    }

    public static PersonFragment newInstance(int position) {
        PersonFragment fragment = new PersonFragment();
        Bundle args = new Bundle();
        args.putInt("position", position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            position = getArguments().getInt("position");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_person, container, false);

        personListRecyclerView = (RecyclerView) view.findViewById(R.id.person_recyclerview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        personListRecyclerView.setLayoutManager(linearLayoutManager);
//        EndlessScrollListener scrollListener = new EndlessScrollListener(linearLayoutManager) {
//            @Override
//            public boolean onLoadMore(int page, int totalItemCount) {
//                Log.i("Loading", "page is " + String.valueOf(page));
//                Log.i("Loading", "totalItemcount  " + String.valueOf(totalItemCount));
//                return true;
//            }
//        };

        personListRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                Log.i("onScrolled", "dx = " + String.valueOf(dx));
                Log.i("onSCrolled", "dy = " + String.valueOf(dy));
            }
        });
        getPersons();

        return view;
    }


    public void getPersons() {
        JsonObjectRequest castRequest = new JsonObjectRequest(PERSONS_URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i("cast response", response.toString());
                try {
                    JSONArray castArray = response.getJSONArray("results");
                    currentPage = response.getInt("page");
                    String actor_name, actor_avatar, actor_character;
                    int actor_id;

                    for(int i = 0; i < castArray.length(); i++) {
                        Person person = new Person();

                        JSONObject actor = castArray.getJSONObject(i);
                        actor_name = actor.getString("name");
                        actor_avatar = actor.getString("profile_path");
                        actor_id = actor.getInt("id");
                        person.setName(actor_name);
                        person.setAvatar(actor_avatar);
                        person.setId(actor_id);
                        persons.add(person);
                    }

                    PersonListRecyclerAdapter personListRecyclerAdapter = new PersonListRecyclerAdapter(getActivity(), persons);
                    personListRecyclerAdapter.setViewHolderCallback(new PersonListRecyclerAdapter.PersonViewHolderCallback() {
                        @Override
                        public void onRowClick(int personId) {
                            Log.i("amclicked", "am clicked with person id " + personId);
                            Intent intent = new Intent(getActivity(), PersonDetails2Activity.class);
                            intent.putExtra("personId", personId);
                            startActivity(intent);
                        }
                    });
                    personListRecyclerView.setAdapter(personListRecyclerAdapter);
                    personListRecyclerAdapter.notifyDataSetChanged();
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

        VolleyFactory.getInstance(getActivity().getApplicationContext()).getRequestQueue().add(castRequest);
    }

    @Override
    public void onRowClick(int personId) {
        Log.i("called", "called in frag");
        Intent intent = new Intent(getActivity(), PersonDetails2Activity.class);
        intent.putExtra("personId", personId);
        getActivity().startActivity(intent);
    }
}
