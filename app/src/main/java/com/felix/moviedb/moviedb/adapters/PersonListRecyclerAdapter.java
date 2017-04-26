package com.felix.moviedb.moviedb.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.felix.moviedb.moviedb.R;
import com.felix.moviedb.moviedb.models.Person;
import com.felix.moviedb.moviedb.services.VolleyFactory;
import com.felix.moviedb.moviedb.utils.TheMovieDBUtil;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by felix on 2/26/17.
 */

public class PersonListRecyclerAdapter extends RecyclerView.Adapter<PersonListRecyclerAdapter.PersonViewHolder>{
    private ArrayList<Person> persons;
    private Context context;
    private PersonListRecyclerAdapter.PersonViewHolderCallback callback;

    public PersonListRecyclerAdapter(Context context, ArrayList<Person> persons) {
        this.context = context;
        this.persons = persons;

    }
    @Override
    public PersonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.person_viewholder, parent, false);
        return new PersonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final PersonViewHolder holder, int position) {
        final Person person = persons.get(position);
        Log.i("person", "person id is " + person.getId());
        holder.rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("rowclicked", "person id is " + person.getId());
                callback.onRowClick(person.getId());
            }
        });

        holder.nameView.setText(person.getName());
        VolleyFactory.getInstance(context).getImageLoader().get(TheMovieDBUtil.IMAGE_URL + person.getAvatar(), new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                holder.imageView.setImageBitmap(response.getBitmap());
            }

            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
    }


    @Override
    public int getItemCount() {
        return persons.size();
    }

    class PersonViewHolder extends RecyclerView.ViewHolder {
        public CircleImageView imageView;
        public TextView nameView;
        public View rowView;
        public PersonViewHolder(View itemView) {
            super(itemView);
            rowView = itemView;
            imageView = (CircleImageView) itemView.findViewById(R.id.person_image);
            nameView = (TextView) itemView.findViewById(R.id.person_details_name);


        }
    }

    public void setViewHolderCallback(PersonViewHolderCallback callback) {
        this.callback = callback;
    }

    public interface PersonViewHolderCallback {
        public void onRowClick(int personId);
    }
}
