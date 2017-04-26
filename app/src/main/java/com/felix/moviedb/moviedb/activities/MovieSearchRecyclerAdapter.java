package com.felix.moviedb.moviedb.activities;

import android.content.Context;
import android.media.Image;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.felix.moviedb.moviedb.R;
import com.felix.moviedb.moviedb.interfaces.MovieViewHolderCallback;
import com.felix.moviedb.moviedb.models.Movie;
import com.felix.moviedb.moviedb.services.VolleyFactory;
import com.felix.moviedb.moviedb.utils.TheMovieDBUtil;
import com.felix.moviedb.moviedb.utils.Util;

import java.util.ArrayList;


/**
 * Created by felix on 2/23/17.
 */

public class MovieSearchRecyclerAdapter extends RecyclerView.Adapter<MovieSearchRecyclerAdapter.ResultItemViewHolder> {
    private ArrayList<Movie> searchResult;
    private Context context;
    private MovieViewHolderCallback callback;

    public MovieSearchRecyclerAdapter(Context context, ArrayList<Movie> searchResults) {
        this.context = context;
        this.searchResult = searchResults;
    }

    public void setMovieCallback(Object callback) {
        try {
            this.callback = (MovieViewHolderCallback) callback;
        } catch(Exception e) {

        }
    }

    @Override
    public ResultItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.search_result_item_view, parent, false);

        return new ResultItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ResultItemViewHolder holder, int position) {
        final Movie movie = searchResult.get(position);
        holder.titleView.setText(movie.getTitle());
        holder.releaseDateView.setText(movie.getReleaseDate());
        VolleyFactory.getInstance(context.getApplicationContext()).getImageLoader().get(movie.getImage(), new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                holder.imageView.setImageBitmap(response.getBitmap());
            }

            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });

        holder.containerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callback.onImageClick(view, movie.getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return searchResult.size();
    }

    class ResultItemViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView titleView;
        public TextView releaseDateView;
        public View containerView;

        public ResultItemViewHolder(View itemView) {
            super(itemView);

            containerView = itemView;
            imageView = (ImageView) itemView.findViewById(R.id.movie_result_image);
            titleView =  (TextView) itemView.findViewById(R.id.movie_result_title);
            releaseDateView = (TextView) itemView.findViewById(R.id.movie_result_release_date);
        }
    }
}


