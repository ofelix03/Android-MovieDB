package com.felix.moviedb.moviedb.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.felix.moviedb.moviedb.R;
import com.felix.moviedb.moviedb.models.Movie;
import com.felix.moviedb.moviedb.services.VolleyFactory;

import java.util.ArrayList;

/**
 * Created by felix on 1/11/17.
 */

public class NewMovieRecyclerViewAdapter extends  RecyclerView.Adapter<NewMovieRecyclerViewAdapter.MovieViewHolder> {
    private ArrayList<Movie> movies;
    private Context context;


    public NewMovieRecyclerViewAdapter(Context context, ArrayList<Movie> movies) {
        this.movies = movies;
        this.context = context;
    }

    @Override
    public NewMovieRecyclerViewAdapter.MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_new_viewholder, parent, false);


        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NewMovieRecyclerViewAdapter.MovieViewHolder holder, int position) {
        Movie movie = movies.get(position);

        Log.i("setTitle", movie.getTitle());

        holder.imageView.setImageUrl(movie.getImage(), VolleyFactory.getInstance(context).getImageLoader());
        holder.titleView.setText(movie.getTitle());
        holder.ratingView.setText(movie.getRating());
        holder.releaseDateView.setText(movie.getReleaseDate());

    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    class MovieViewHolder extends RecyclerView.ViewHolder  {
        public NetworkImageView imageView;
        public TextView titleView;
        public TextView ratingView;
        public TextView releaseDateView;

        public MovieViewHolder(View itemView) {
            super(itemView);
            imageView = (NetworkImageView) itemView.findViewById(R.id.movie_image);
            titleView = (TextView) itemView.findViewById(R.id.movie_title);
            ratingView = (TextView) itemView.findViewById((R.id.movie_rating));
            releaseDateView = (TextView) itemView.findViewById((R.id.movie_release_date));
        }
    }
}
