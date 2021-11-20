package com.example.earthquakereport;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class utility {



    public static final String LOG_TAG = utility.class.getName();

    public static URL createUrl(String url) {
        URL myURL = null;
        try {
            myURL = new URL(url);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "error in creating url", e);
        }
        return myURL;
    }
    public static String makehttp_request(URL url) throws IOException {
        String JSONresponse = " ";

        if (url == null) {
            return JSONresponse;
        }
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                JSONresponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "error response code  " + urlConnection.getResponseCode());
            }

        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem in retriving json result", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies than an IOException
                // could be thrown.
                inputStream.close();
            }


        }
        return JSONresponse;
    }

    public static String readFromStream(InputStream inputStream) {
        StringBuilder stringBuilder = new StringBuilder();
        if (inputStream != null) {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line;

            try {
                while ((line = bufferedReader.readLine()) != null)
                    stringBuilder.append(line);
            } catch (IOException e) {
                Log.e(LOG_TAG, "erroe in retriving info from inputStream", e);
            }
        }

        return stringBuilder.toString();
    }

    public static ArrayList<Earthquake_items_prototype> extractFromJSON(String JSONresponse)
    {


        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(JSONresponse)) {
            return null;
        }


        ArrayList<Earthquake_items_prototype> Earthquake_List=new ArrayList<Earthquake_items_prototype>();
        try {
            JSONObject root=new JSONObject(JSONresponse);
            JSONArray features=root.getJSONArray("features");
            for(int i=0;i<features.length();i++) {
                JSONObject details = features.getJSONObject(i);
                JSONObject properties=details.getJSONObject("properties");
                String url=properties.getString("url");
                String place=properties.getString("place");
                double magnitude=properties.getDouble("mag");
                long time=properties.getLong("time");
                Earthquake_List.add(new Earthquake_items_prototype(magnitude,url,place,time));
            }
        } catch (JSONException e) {
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }
        return Earthquake_List;
    }
}

