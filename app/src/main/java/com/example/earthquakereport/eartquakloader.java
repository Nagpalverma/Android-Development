package com.example.earthquakereport;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class eartquakloader extends AsyncTaskLoader<List<Earthquake_items_prototype>> {
   private String url;
   static final String LOG_TAG=eartquakloader.class.getName();
    public eartquakloader( Context context,String url) {
        super(context);
        this.url=url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Earthquake_items_prototype> loadInBackground() {
        List<Earthquake_items_prototype> earthquakes;
        URL geturl=utility.createUrl(url);

        String Jsonresponse=null;
        try {
            Jsonresponse=utility.makehttp_request(geturl);
        } catch (IOException e) {
            Log.e(LOG_TAG,"in loadInBackground, error in making http request",e);
        }
        earthquakes=utility.extractFromJSON(Jsonresponse);


        return earthquakes;
    }
}
