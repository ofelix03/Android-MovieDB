package com.felix.moviedb.moviedb.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.felix.moviedb.moviedb.R;
import com.felix.moviedb.moviedb.models.Cast;
import com.felix.moviedb.moviedb.models.Person;
import com.felix.moviedb.moviedb.services.VolleyFactory;
import com.felix.moviedb.moviedb.utils.TheMovieDBUtil;

/**
 * Created by felix on 1/17/17.
 */

public class CastRecyclerViewAdapter extends RecyclerView.Adapter<CastRecyclerViewAdapter.CastViewHolder> {
    private Cast cast;
    private Context context;
    private Callback callback;

    public CastRecyclerViewAdapter(Context context, Cast cast, Object callback) {
        this.cast = cast;
        this.context = context;
        this.callback = (Callback) callback;
    }

    public void setCallback(Object callback) {
        this.callback = (Callback) callback;
    }

    @Override
    public CastViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.viewholder_person, parent, false);

        return new CastViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final CastViewHolder holder, int position) {
        final Person actor = cast.getCast().get(position);

        holder.nameView.setText(actor.getName());
        holder.characterView.setText("(" + actor.getCharacter() + ")");

        VolleyFactory.getInstance(context.getApplicationContext()).getImageLoader().get(TheMovieDBUtil.IMAGE_URL + actor.getAvatar(), new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                Log.i("castImage", response.toString());
                holder.avatarView.setImageBitmap(response.getBitmap());
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("actor image", error.toString());
            }
        });

        holder.castContainerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("castclicked", "cast item clicked");
                callback.onCastClick(actor.getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return cast.getCount();
    }

    class CastViewHolder extends RecyclerView.ViewHolder {

        public RelativeLayout castContainerView;
        public TextView nameView;
        public ImageView avatarView;
        public TextView characterView;

        public CastViewHolder(View itemView) {
            super(itemView);

            castContainerView = (RelativeLayout) itemView.findViewById(R.id.person_container);
            nameView = (TextView) itemView.findViewById(R.id.person_details_name);
            avatarView = (ImageView) itemView.findViewById(R.id.person_details_avatar);
            characterView = (TextView) itemView.findViewById(R.id.person_character);
        }
    }

    public interface Callback {
        public void onCastClick(int castId);
    }
}
