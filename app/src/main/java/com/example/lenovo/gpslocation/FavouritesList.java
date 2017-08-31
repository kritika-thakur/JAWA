package com.example.lenovo.gpslocation;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
    Dialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favouriteslist);
        db = new DatabaseHandler(FavouritesList.this, null, null, 21);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mainlayout=(RelativeLayout) this.findViewById(R.id.hidelayout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // Dialog box Initialization
        dialog = new Dialog(FavouritesList.this);
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
                lat = latlist.get(position);
                lng = lnglist.get(position);
                onMapSearch(recyclerView,namelist.get(position).toString());
                TextView text = (TextView) dialog.findViewById(R.id.textv);
                TextView tvname = (TextView) dialog.findViewById(R.id.tvname);
                RatingBar rating_bar = (RatingBar) dialog.findViewById(R.id.ratingbar);
                LayerDrawable stars = (LayerDrawable) rating_bar.getProgressDrawable();
                stars.getDrawable(2).setColorFilter(Color.parseColor("#FDA32B"), PorterDuff.Mode.SRC_ATOP);
                stars.getDrawable(1).setColorFilter(Color.parseColor("#FDA32B"), PorterDuff.Mode.SRC_ATOP);
                tvname.setText(namelist.get(position));
                text.setText(ar.get(position));
                if(arRate.get(position)!=null)
                    rating_bar.setRating(Float.parseFloat(arRate.get(position)));
                else
                    rating_bar.setRating(0);
                dialog.show();
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
    @Override
    public void onRestart() {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }
    class FavAdapter extends RecyclerView.Adapter<FavAdapter.MyViewHolder> {
        private List<Fav> moviesList;
        public DatabaseHandler db;
        private Context context;
        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView title, genre;
            RatingBar year;
            ImageView btn_del;
            LinearLayout linear1;
            public MyViewHolder(View view) {
                super(view);
                title = (TextView) view.findViewById(R.id.title);
                genre = (TextView) view.findViewById(R.id.genre);
                year = (RatingBar)view.findViewById(R.id.year);
                btn_del = (ImageView)view.findViewById(R.id.btn_del);
             //   linear1 = (LinearLayout) view.findViewById(R.id.linear1);
                LayerDrawable stars = (LayerDrawable) year.getProgressDrawable();
                stars.getDrawable(2).setColorFilter(Color.parseColor("#FDA32B"), PorterDuff.Mode.SRC_ATOP);
                stars.getDrawable(1).setColorFilter(Color.parseColor("#FDA32B"), PorterDuff.Mode.SRC_ATOP);
            }
        }
        public FavAdapter(List<Fav> moviesList)  {
            this.moviesList = moviesList;
        }
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.fav_list_row, parent, false);
            return new MyViewHolder(itemView);
        }
        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {
            final Fav fav_model = moviesList.get(position);
            holder.title.setText(fav_model.getTitle());
            holder.genre.setText(fav_model.getGenre());
            if(fav_model.getYear()!=null)
                holder.year.setRating(Float.parseFloat(fav_model.getYear()));
            holder.btn_del.setOnClickListener(new View.OnClickListener() {
                public void onClick(final View v) {

                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(v.getContext());
                    dialog.dismiss();
                    alertDialog.setTitle("Delete Item");
                    alertDialog.setMessage("Are you sure you want to delete this?");
                    alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int which) {
                            db = new DatabaseHandler(v.getContext(), null, null, 21);
                            db.updatefav(0,fav_model.getId());
                            //notifyItemRemoved(fav_model.getId());
                            ((Activity)v.getContext()).finish();
                            v.getContext().startActivity(((Activity) v.getContext()).getIntent());
                            // Toast.makeText(v.getContext(), "You clicked on YES", Toast.LENGTH_SHORT).show();
                        }
                    });
                    alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            //   Toast.makeText(v.getContext(), "You clicked on NO", Toast.LENGTH_SHORT).show();
                            dialog.cancel();
                        }
                    });

                    // Showing Alert Message
                    alertDialog.show();
                }
            });
        }
        @Override
        public int getItemCount() {
            return moviesList.size();
        }
    }

}