package com.example.earthquakereport;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

//implementing LoaderCallback to handle configurational change

public class MainActivity extends AppCompatActivity implements earthquake_adapter.view_click_handling , LoaderManager.LoaderCallbacks<List<Earthquake_items_prototype>> {

    List<Earthquake_items_prototype> data;
    public static final String LOG_TAG=MainActivity.class.getName();
    earthquake_adapter adapter;
    RecyclerView recyclerView;
    ProgressDialog progressDialog;
    SwipeRefreshLayout swipeRefreshLayout;
    background task;
    TextView emptytextview;
    public static final String USJS_URL = "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&eventtype=earthquake&orderby=time&limit=100";

   //onCreate is overridden
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //SwipeRefershLayuot for fetching updated data from server

        swipeRefreshLayout = findViewById(R.id.swiperefersh);
        //progressDialog for showing  some message to hen data is loading from server.
        progressDialog = new ProgressDialog(MainActivity.this);



        //TextView which show info when there is no internet connection or there is error in fetching data from server.

        emptytextview = findViewById(R.id.textView);

        //RecyclerView initialize

        recyclerView = findViewById(R.id.recycleview);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        ((LinearLayoutManager) layoutManager).setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);


        //SwipeRefershLayout on Refersh
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                progressDialog.show();
                progressDialog.setContentView(R.layout.progresslayout);
                progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

                //calling AsynTask  background for fetching new updated data from server
                task = new background();
                task.execute(USJS_URL);


            }
        });


        //Using  ConnectivityManger for checking Internet connection
        ConnectivityManager connectivityManager=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();
        //if network is live
        if (networkInfo != null && networkInfo.isConnected()) {
            //intialising LoaderManager
            LoaderManager lm = getSupportLoaderManager();
            lm.initLoader(1, null, this).forceLoad();
        }
        //
        else {
            //setting text in textview when there is no internet connection.
            emptytextview.setText("no intenet connection");
        }

        //if adapter is null then setting RecyclerView invisible and TextView Visible
        //but setting text in textview is done in onLoadFinished because it may be, at first, no data is returned from server then
        //adapter is not initialised and our textview show the text at right time.
        if (adapter==null) {
            recyclerView.setVisibility(View.GONE);
            emptytextview.setVisibility(View.VISIBLE);

        }
    }
    //declaring AsycTask class
    class background extends AsyncTask<String,Void,List<Earthquake_items_prototype>>
    {


        @Override
        protected List<Earthquake_items_prototype> doInBackground(String...url) {
            Log.d("check","in starting of do in bg");
            List<Earthquake_items_prototype> earthquakes;
            URL geturl=utility.createUrl(url[0]);

            String Jsonresponse=null;
            try {
                Jsonresponse=utility.makehttp_request(geturl);
            } catch (IOException e) {
                Log.e(LOG_TAG,"in loadInBackground, error in making http request",e);
            }
            earthquakes=utility.extractFromJSON(Jsonresponse);
            Log.d("check","in last of do in bg");
            return earthquakes;
        }

        @Override
        protected void onPostExecute(List<Earthquake_items_prototype> data) {
            if(data!=null && !data.isEmpty())
            {
                updateUI(data);
            }
            swipeRefreshLayout.setRefreshing(false);
            progressDialog.dismiss();
        }
    }

    //handling  item clicking   on recyclerView
    @Override
    public void onViewClick(int position) {
        Earthquake_items_prototype earthquake=data.get(position);
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(earthquake.getUrl())));
    }

    public void updateUI(List<Earthquake_items_prototype> earthaquakes)
    {
        adapter=new earthquake_adapter(earthaquakes,this);
        data=earthaquakes;
        recyclerView.setAdapter(adapter);
            recyclerView.setVisibility(View.VISIBLE);
            emptytextview.setVisibility(View.GONE);

    }


    public Loader<List<Earthquake_items_prototype>> onCreateLoader(int id,  Bundle args) {
        progressDialog.show();
        progressDialog.setContentView(R.layout.progresslayout);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        Log .d("check","in onCreate");
        return new eartquakloader(this,USJS_URL);
    }

    @Override
    public void onLoadFinished( Loader<List<Earthquake_items_prototype>> loader, List<Earthquake_items_prototype> data) {
        emptytextview.setText("no earthquake data");

        if(data!=null && !data.isEmpty())
        {
            updateUI(data);
        }

 progressDialog.dismiss();

        Log.d("check","in on Load finished");
    }

    @Override
    public void onLoaderReset( Loader<List<Earthquake_items_prototype>> loader) {
        data=new ArrayList<Earthquake_items_prototype>();
        adapter=new earthquake_adapter(data,this);

    }
}