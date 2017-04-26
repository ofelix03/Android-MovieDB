package com.felix.moviedb.moviedb.activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.felix.moviedb.moviedb.R;
import com.felix.moviedb.moviedb.adapters.MovieRecyclerViewAdapter;
import com.felix.moviedb.moviedb.interfaces.EndlessScrollListener;
import com.felix.moviedb.moviedb.interfaces.MovieViewHolderCallback;
import com.felix.moviedb.moviedb.models.Movie;
import com.felix.moviedb.moviedb.services.VolleyFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;

public class SearchResultActivity extends AppCompatActivity implements MovieViewHolderCallback{
    private AppCompatActivity activity = this;
    private SearchView searchView;
    private ListView searchResultListView;
    private ArrayList<Movie> searchResults = new ArrayList<>();
    private TextView emptyResultView;
    private NestedScrollView nestedScrollView;
    private String[] recentSearches = new String[] {};
    private TextView searchResultEmpty;
    private TextView searchHeaderView;
    private String query;
    private Integer page = 1;

    private RecyclerView movieSearchResultRecyclerView;
    private ProgressBar progressBar;

    private String MOVIE_SEARCH_URL = "https://api.themoviedb.org/3/search/movie?api_key=f54dad4d1c97e5e9713189d86d100bf7";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        nestedScrollView = (NestedScrollView) findViewById(R.id.nested_scrollview);

        searchResultEmpty  = (TextView) findViewById(R.id.search_result_empty);
        searchResultEmpty.setVisibility(View.GONE);

        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        progressBar.setVisibility(View.GONE);

        searchHeaderView = (TextView) findViewById(R.id.search_header);
        searchHeaderView.setVisibility(View.GONE);

        movieSearchResultRecyclerView = (RecyclerView) findViewById(R.id.movie_search_result_recyclerview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        movieSearchResultRecyclerView.setLayoutManager(linearLayoutManager);
        movieSearchResultRecyclerView.addItemDecoration(dividerItemDecoration);
        movieSearchResultRecyclerView.setHasFixedSize(true);


        handleIntent(getIntent());
    }

    public void showSearchResult(ArrayList<Movie> results) {
        hideProgressBar();
        setHeaderText("Search results");
        page = page + 1;

        MovieSearchRecyclerAdapter movieSearchRecyclerAdapter = new MovieSearchRecyclerAdapter(activity, results);
        movieSearchRecyclerAdapter.setMovieCallback(this);
        movieSearchResultRecyclerView.setAdapter(movieSearchRecyclerAdapter);
        movieSearchRecyclerAdapter.notifyDataSetChanged();
        movieSearchResultRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                Integer firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
                Integer lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
            }
        });


        if (results.size() == 0) {
            showSearchResultEmptyFeedback();
        }
    }

    public void setHeaderText(String text) {
        searchHeaderView.setVisibility(View.VISIBLE);
        searchHeaderView.setText(text);
    }

    public void showSearchResultEmptyFeedback() {
        searchResultEmpty.setVisibility(View.VISIBLE);
    }

    public void hideSearchResultEmptyFeedback() {
        searchResultEmpty.setVisibility(View.GONE);
    }

    public void showProgressBar() {
        hideSearchResultEmptyFeedback();
        progressBar.setVisibility(View.VISIBLE);
    }

    public void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    public void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            query = intent.getStringExtra(SearchManager.QUERY);
            searchView.setQuery(query, false);
            getMovies(query);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconified(false);
        searchView.setSelected(false);
        searchView.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        searchView.clearFocus();
        return true;
    }

    public void getMovies(String query) {
        if (page == 1) {
            showProgressBar();
        }

        String url = MOVIE_SEARCH_URL + "&query=" + URLEncoder.encode(query);
        searchResults = new ArrayList<>();
        final JsonObjectRequest moviesRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
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

                        searchResults.add(new Movie(id, image, title, rating, releaseDate));
                    }
                    showSearchResult(searchResults);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("error", error.toString());
            }
        });
        VolleyFactory.getInstance(this).getRequestQueue().add(moviesRequest);
    }


    @Override
    public void onImageClick(View view, int tvId) {
        Intent movieDetailsIntent = new Intent(this, MovieDetailsActivity.class);
        movieDetailsIntent.putExtra("movieId", tvId);
        startActivity(movieDetailsIntent);
    }
}
