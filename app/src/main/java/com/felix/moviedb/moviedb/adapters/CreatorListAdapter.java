package com.felix.moviedb.moviedb.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.felix.moviedb.moviedb.R;
import com.felix.moviedb.moviedb.models.Person;
import com.felix.moviedb.moviedb.services.VolleyFactory;
import com.felix.moviedb.moviedb.utils.TheMovieDBUtil;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by felix on 1/21/17.
 */


public class CreatorListAdapter extends ArrayAdapter<Person> {
    private ArrayList<Person> creators;

    public CreatorListAdapter(Context context, int resource, ArrayList<Person> objects) {
        super(context, resource, objects);
        this.creators = objects;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.creators_viewholder, parent, false);
        Person creator = creators.get(position);

        final CreatorViewHolder holder = new CreatorViewHolder(view);
        holder.nameView.setText(creator.getName());
        VolleyFactory.getInstance(getContext()).getImageLoader().get(TheMovieDBUtil.IMAGE_URL + creator.getAvatar(), new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                holder.imageView.setImageBitmap(response.getBitmap());
            }

            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        return holder.getView();
    }

    @Override
    public int getCount() {
        return creators.size();
    }
}

class CreatorViewHolder{
    private View view;
    public CircleImageView imageView;
    public TextView  nameView;

    public CreatorViewHolder(View view) {
        this.view = view;
        imageView = (CircleImageView) view.findViewById(R.id.creator_image);
        nameView = (TextView) view.findViewById(R.id.creator_name);
    }

    public View getView() {
        return view;
    }
}



