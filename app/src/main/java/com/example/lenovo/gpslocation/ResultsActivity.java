package com.example.lenovo.gpslocation;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class ResultsActivity extends AppCompatActivity
        implements  OnMapReadyCallback {
    private ExpandableListView mExpandableList;
    private GoogleMap mMap;
    GPSTracker gps;
    String groupItems[], category,groupTypeItems[];
    public static DatabaseHandler db;
    String name, info;
    String location;
    float rating;
    final Context context = this;
    ImageView favchck;
    double latitude, lat;
    double longitude, lng;
    ListItem item;
    int flag = 0;
    int ID;
    int favourites;
    String URL,type;
    ArrayList<String> ar = new ArrayList<String>();
    ArrayList<Integer> arlist = new ArrayList<>();
    ArrayList<Integer> favlist = new ArrayList<>();
    ArrayList<String> namelist = new ArrayList<>();
    ArrayList<Double> latlist = new ArrayList<>();
    ArrayList<Double> lnglist = new ArrayList<>();
    ArrayList<Parent> arrayParents = new ArrayList<Parent>();
    ArrayList<String> arrayChildren = new ArrayList<String>();
    ArrayList<Float> arRate = new ArrayList<Float>();
    Cursor cursor;
    Selectitem s_items;
    MyCustomAdapter cust;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        final SQLiteDatabase database = this.openOrCreateDatabase("Mydatabase.db", MODE_PRIVATE, null);
        s_items = new Selectitem();
        db = new DatabaseHandler(ResultsActivity.this, null, null, 14);
        item = new ListItem();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.custom);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_view);
        mapFragment.getMapAsync(this);
        gps = new GPSTracker(ResultsActivity.this);
        if (gps.canGetLocation()) {
            latitude = gps.getLatitude();
            longitude = gps.getLongitude();
           // loadRecyclerViewData(context,categories,URL,db,lattList,langList,item);
        } else {
            gps.showSettingsAlert();
        }
        cursor = getApplicationContext().getContentResolver().query(Uri.parse("android-app://com.example.lenovo.gpslocation"), null, null, null, null);
        mExpandableList = (ExpandableListView) findViewById(R.id.expandable_list);
        Bundle b = getIntent().getExtras();
        groupItems = b.getStringArray("selectedItems");
        groupTypeItems=b.getStringArray("catTypes");
        cust = new MyCustomAdapter(ResultsActivity.this, arrayParents);
        //here we set the parents and the children
        for (int i = 0; i < groupItems.length; i++) {
            //for each "i" create a new Parent object to set the title and the children
            Parent parent = new Parent();
            parent.setTitle(groupItems[i]);
            arrayChildren = new ArrayList<String>();
            for (int j = 0; j < 10; j++) {
                arrayChildren.add("Child " + j);
            }
            parent.setArrayName(namelist);
            parent.setArrayChildren(ar);
            parent.setArrayRate(arRate);
            //in this array we add the Parent object. We will use the arrayParents at the setAdapter
            arrayParents.add(parent);
        }
        //sets the adapter that provides data to the list.
        mExpandableList.setAdapter(cust);
        mExpandableList.deferNotifyDataSetChanged();
        mExpandableList.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                // display a toast with group name whenever a user clicks on a group item
              //  Toast.makeText(getApplicationContext(), "Group Name Is :" + groupItems[groupPosition], Toast.LENGTH_LONG).show();
                category = groupItems[groupPosition];
                type=groupTypeItems[groupPosition];
                ar.clear();
                favlist.clear();
                namelist.clear();
                URL = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" + latitude + "," + longitude + "&radius=1000&type="+type+"&sensor=true&key=AIzaSyAxGdWYQ4wzVFzAXehZpeV-2t7GuOSL5q4";
                arlist.clear();
                latlist.clear();
                lnglist.clear();
                getData(groupPosition);
                s_items.loadRecyclerViewData(context,category,URL,db,latlist,lnglist,item);
                return false;
            }
        });
        mExpandableList.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            int previousGroup = -1;

            @Override
            public void onGroupExpand(int groupPosition) {
                if (groupPosition != previousGroup)
                    mExpandableList.collapseGroup(previousGroup);
                previousGroup = groupPosition;
            }
        });
        //  perform set on child click listener event
        mExpandableList.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, final int groupPosition, final int childPosition, long id) {
                // display a toast with child name whenever a user clicks on a child item
                //Toast.makeText(getApplicationContext(), "Child Name Is :" + arlist.get(childPosition), Toast.LENGTH_LONG).show();

                mMap.clear();
                dialog.setTitle(namelist.get(childPosition));
                lat = latlist.get(childPosition);
                lng = lnglist.get(childPosition);
                onMapSearch(mExpandableList,namelist.get(childPosition).toString());
                info = namelist.get(childPosition);
                TextView text = (TextView) dialog.findViewById(R.id.textv);
                TextView tvname = (TextView) dialog.findViewById(R.id.tvname);
                favchck = (ImageView) dialog.findViewById(R.id.favchck);
                RatingBar rating_bar = (RatingBar) dialog.findViewById(R.id.ratingbar);
                if (favlist.get(childPosition) == 0) {
                    favchck.setImageResource(R.drawable.blankheart);
                } else {
                    favchck.setImageResource(R.drawable.blackheart);
                }

                favchck.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (flag == 1) {
                            flag = 0;
                            db.updatefav(0, arlist.get(childPosition));
                            favchck.setImageResource(R.drawable.blankheart);
                        } else if (flag == 0) {
                            flag = 1;
                            db.updatefav(1, arlist.get(childPosition));
                            favchck.setImageResource(R.drawable.blackheart);
                        }
                        getData(groupPosition);
                        mExpandableList.setAdapter(cust);
                        cust.setNewItems(ResultsActivity.this, arrayParents);

                    }
                });
                tvname.setText(namelist.get(childPosition));
                text.setText(ar.get(childPosition));
                rating_bar.setRating(arRate.get(childPosition));
                dialog.show();

                return false;
            }
        });
    }

    public void getData(int position) {
        cursor = db.getData("select id,place_name,place_add,place_rating,place_icon,latitude,longitude,favourites from selected_data where category = '" + groupItems[position] + "'");
        if (cursor != null && cursor.moveToFirst()) {
            do {
                ID = cursor.getInt(0);
                name = cursor.getString(1);
                location = cursor.getString(2);
                rating = cursor.getFloat(3);
//              image = cursor.getString(4);
                latitude = cursor.getDouble(5);
                longitude = cursor.getDouble(6);
                favourites = cursor.getInt(7);
                ar.add(location);
                arlist.add(ID);
                favlist.add(favourites);
                namelist.add(name);
                if(namelist.isEmpty())
                    Toast.makeText(gps, "Results Not Found.Try changing radius.", Toast.LENGTH_SHORT).show();
                latlist.add(latitude);
                lnglist.add(longitude);
                arRate.add(rating);
            } while (cursor.moveToNext());
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }
    public void onMapSearch(View view, String s) {
        LatLng latLng = new LatLng(lat, lng);
        mMap.addMarker(new MarkerOptions().position(latLng).title(s));
        mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.moveCamera( CameraUpdateFactory.newLatLngZoom(new LatLng(lat,lng) , 14.0f));
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        finish();
        return true;
    }
}