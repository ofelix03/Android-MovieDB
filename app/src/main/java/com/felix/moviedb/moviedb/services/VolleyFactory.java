package com.felix.moviedb.moviedb.services;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.util.LruCache;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

/**
 * Created by felix on 1/10/17.
 */

public class VolleyFactory {
    private Context context;
    private RequestQueue requestQueue = null;
    private ImageLoader imageLoader = null;
    private static VolleyFactory instance = null;


    private VolleyFactory(Context context) {
        Log.i("volley", "Volley constructor called");
        this.context = context;
        requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        setUpImageLoader();
    }

    public static VolleyFactory getInstance(Context context) {
        if (instance == null ) {
            instance = new VolleyFactory(context);
        }

        return instance;
    }

    public  RequestQueue getRequestQueue() {
       return  getInstance(context).requestQueue;
    }

    private void setUpImageLoader() {
        imageLoader = new ImageLoader(requestQueue, new ImageLoader.ImageCache() {
            private LruCache<String, Bitmap> cache = new LruCache<String, Bitmap>(1024*1024);
            @Override
            public Bitmap getBitmap(String url) {
                return cache.get(url);
            }

            @Override
            public void putBitmap(String url, Bitmap bitmap) {
                cache.put(url, bitmap);
            }
        });
    }

    public ImageLoader getImageLoader() {
        return imageLoader;
    }
}
