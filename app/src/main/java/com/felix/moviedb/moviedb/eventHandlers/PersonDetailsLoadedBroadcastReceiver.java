package com.felix.moviedb.moviedb.eventHandlers;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.felix.moviedb.moviedb.models.Person;

/**
 * Created by felix on 3/26/17.
 */

public abstract class PersonDetailsLoadedBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("onReceived", "onReceived");
        updateView(context, intent);
    }

    public abstract void updateView(Context context, Intent intent);
}
