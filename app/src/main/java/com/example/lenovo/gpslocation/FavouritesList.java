package com.example.lenovo.gpslocation;

import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class FavouritesList extends BaseActivity implements OnMapReadyCallback {

    private List<Fav> movieList = new ArrayList<>();
    private RecyclerView recyclerView;
    private FavAdapter mAdapter;
    DatabaseHandler db;
    Cursor cursor;
    private GoogleMap mMap;
    int ID;
    //Array list declaration
    ArrayList<String> ar = new ArrayList<String>();
    ArrayList<String> arRate = new ArrayList<String>();
    final Context context = this;
    String name, location,rating;
    double latitude,longitude,lat,lng;// lat and lng
    ArrayList<String> namelist = new ArrayList<>();
    //Array list declaration
    ArrayList<Double> latlist = new ArrayList<>();
    ArrayList<Double> lnglist = new ArrayList<>();
    RelativeLayout mainlayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favouriteslist);
        db = new DatabaseHandler(FavouritesList.this, null, null, 14);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mainlayout=(RelativeLayout) this.findViewById(R.id.hidelayout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // Dialog box Initialization
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.custom_favorites);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_view1);
        mapFragment.getMapAsync(this);

        mAdapter = new FavAdapter(movieList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        mainlayout.setVisibility(View.GONE);

        // OnClick for Click on Recycler View
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                mMap.clear();
                Fav fav = movieList.get(position);
                dialog.setTitle(namelist.get(position));
                onMapSearch(recyclerView,namelist.get(position).toString());
                TextView text = (TextView) dialog.findViewById(R.id.textv);
                TextView tvname = (TextView) dialog.findViewById(R.id.tvname);
                RatingBar rating_bar = (RatingBar) dialog.findViewById(R.id.ratingbar);
                dialog.show();
                lat = latlist.get(position);
                lng = lnglist.get(position);
                tvname.setText(namelist.get(position));
                text.setText(ar.get(position));
                if(arRate.get(position)!=null)
                rating_bar.setRating(Float.parseFloat(arRate.get(position)));
                else
                    rating_bar.setRating(0);
            }
            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        prepareMovieData();

    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    private void prepareMovieData() {
        cursor =db.getData("select id,place_name,place_add,place_rating,place_icon,latitude,longitude from selected_data where favourites = 1");
        if (cursor != null && cursor.moveToFirst()) {
            do {
                ID = cursor.getInt(0);
                name = cursor.getString(1);
                location = cursor.getString(2);
                rating = cursor.getString(3);
                //image = cursor.getString(4);
                latitude = cursor.getDouble(5);
                longitude = cursor.getDouble(6);
                namelist.add(name);
                //Array list for name
                Fav movie = new Fav(ID,name, location, rating);
                movieList.add(movie);
                latlist.add(latitude);// Array list for latitude
                lnglist.add(longitude);// Array list for longitude
                mAdapter.notifyDataSetChanged();
                ar.add(location);// Array list for location
                arRate.add(rating);// Array list for rating
            } while (cursor.moveToNext());
            //R.java
          /*  public static final int drawer=0x7f0200a4;*/

        }else
        {
            mainlayout.setVisibility(View.VISIBLE);

        }
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    // on map Ready for dialog
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }
    // On map search for dialog box
    public void onMapSearch(View view, String s) {
        LatLng latLng = new LatLng(lat, lng);
        mMap.addMarker(new MarkerOptions().position(latLng).title(s));
        mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat,lng) , 14.0f));
    }
}