package com.felix.moviedb.moviedb.activities;

import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.felix.moviedb.moviedb.R;
import com.felix.moviedb.moviedb.adapters.PersonDetailsPageViewAdapter;

import org.w3c.dom.Text;

public class PersonDetailsActivity extends AppCompatActivity {
    private ImageView personAvatarView;
    private TextView personNameView;
    private TextView personBirthPlaceView;
    private TextView personBirthDateView;
    private TextView personDateDateView;
    private ViewPager personViewPager;

    private int personId =  0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_details);

//        personAvatarView = (ImageView) findViewById(R.id.person_details_avatar);
//        personNameView = (TextView) findViewById(R.id.person_details_name);
//        personBirthPlaceView = (TextView) findViewById(R.id.person_details_birthplace);
//        personBirthDateView = (TextView) findViewById(R.id.person_details_birthdate);
//        personDateDateView = (TextView) findViewById(R.id.person_details_deathdate);
//        personViewPager = (ViewPager) findViewById(R.id.person_details_pager);
//
//        personViewPager.setAdapter(new PersonDetailsPageViewAdapter(getSupportFragmentManager(), personId));

    }
}
