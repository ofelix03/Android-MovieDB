package com.felix.moviedb.moviedb.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.felix.moviedb.moviedb.R;
import com.felix.moviedb.moviedb.models.TvSeason;
import com.felix.moviedb.moviedb.services.VolleyFactory;
import com.felix.moviedb.moviedb.utils.TheMovieDBUtil;

import java.util.ArrayList;
import java.util.zip.Inflater;

/**
 * Created by felix on 1/16/17.
 */

public class SeasonListRecyclerViewAdapter extends RecyclerView.Adapter<SeasonListRecyclerViewAdapter.SeasonViewHolder> {

    private Context context;
    private ArrayList<TvSeason> series = new ArrayList<>();

    public SeasonListRecyclerViewAdapter(Context context, ArrayList<TvSeason> series) {
        this.context = context;
        this.series = series;
    }

    @Override
    public SeasonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.viewholder_season_item, parent, false);

        return new SeasonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SeasonViewHolder holder, int position) {
         TvSeason season = series.get(position);

        holder.numberView.setText("S" + String.valueOf(season.getNumber()));

        holder.episodesView.setText(String.valueOf(season.getEpisodes()));
        VolleyFactory.getInstance(context.getApplicationContext()).getImageLoader().get(TheMovieDBUtil.IMAGE_URL + season.getPoster(), new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                holder.posterView.setImageBitmap(response.getBitmap());
            }

            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return series.size();
    }

    class SeasonViewHolder extends RecyclerView.ViewHolder{
        public TextView numberView;
        public TextView episodesView;
        public ImageView posterView;

        public SeasonViewHolder(View itemView) {
            super(itemView);

            numberView = (TextView) itemView.findViewById(R.id.season_number);
            episodesView = (TextView) itemView.findViewById(R.id.season_episodes);
            posterView = (ImageView) itemView.findViewById(R.id.season_poster);

        }
    }
}
