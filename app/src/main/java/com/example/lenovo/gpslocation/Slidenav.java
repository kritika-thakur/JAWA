package com.example.lenovo.gpslocation;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.media.ThumbnailUtils;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

public class Slidenav extends BaseActivity implements OnMapReadyCallback, AdapterView.OnItemClickListener, PlaceSelectionListener {
    GoogleMap mMap;
    GPSTracker gps;
    double latitude;
    double longitude;
    ListItem item;
    Cursor cursor;
    DatabaseHandler db;
    int ID ,mar_id,flag=0,mar_image;
    String name, cateGory,s,search_item_name;
    double lat, lon;
    int marker_color = R.color.gradient_background;;
    ArrayList<Double> latlist = new ArrayList<>();
    ArrayList<String> catlist = new ArrayList<>();
    ArrayList<Double> lnglist = new ArrayList<>();
    ArrayList<String> namelist = new ArrayList<>();
    ArrayList<String> favcatlist = new ArrayList<>();
    ArrayList<String> needcatlist = new ArrayList<>();
    ArrayList<Double> arlist_lati = new ArrayList<>();
    ArrayList<Double> arlist_longi = new ArrayList<>();
    private HashMap<Marker, Integer> mHashMap = new HashMap<Marker, Integer>();
    double la;
    category_model cat_item;
    double lo;
    private Map<Marker, Class> allMarkersMap = new HashMap<Marker, Class>();
    private static final String LOG_TAG = "PlaceSelectionListener";
    Geocoder geocoder;
    List<String> list = new ArrayList<String>();
    List<Address> addresses;
    Place place;
    ArrayList <category_model> foos= new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slidenav);
      // cat_item = new category_model();
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        db = new DatabaseHandler(Slidenav.this, null, null, 20);

        MapFragment supportMapFragment = (MapFragment) getFragmentManager() .findFragmentById(R.id.map);
        supportMapFragment.getMapAsync(this);
        prepareData();
        if (Build.VERSION.SDK_INT >= 23){


            if (checkPermission(Manifest.permission.ACCESS_FINE_LOCATION,getApplicationContext(),this)) {
                //You fetch the Location here
                //code to use the
            }
            else
            {
                requestPermission(Manifest.permission.ACCESS_FINE_LOCATION,100,getApplicationContext(),this);
            }

        }


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        //mMap.setMyLocationEnabled(true);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        item = new ListItem();
        supportMapFragment.getMapAsync(this);
        gps = new GPSTracker(Slidenav.this);
        if(gps.canGetLocation()){
            latitude = gps.getLatitude();
            longitude = gps.getLongitude();
           // Log.d("FIRSTPageTest",""+latitude+"LOng: "+longitude);
        }else{
            gps.showSettingsAlert();
        }
    }
    public void onItemClick(AdapterView adapterView, View view, int position, long id) {
        String str = (String) adapterView.getItemAtPosition(position);
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng location = new LatLng(latitude, longitude);
        mMap.clear();
        mMap.addMarker(new MarkerOptions().position(location).title("Your Location").icon(BitmapDescriptorFactory
                .defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude,longitude) , 15f) );
        mMap.moveCamera(CameraUpdateFactory.newLatLng(location));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.
                PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);
        prepareData();
        for(int i = 0 ; i < arlist_lati.size() ; i++ ) {
            cursor =db.getData("select id,category,marker_color,favourites from selected_data");
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    String cat_name=cursor.getString(1);
                    Log.d("Favorites",cursor.getString(1));
                    if(cat_name.equals(catlist.get(i)))
                    {
                        marker_color=cursor.getInt(2);
                        if(favcatlist.get(i).equals("1"))
                        {
                            marker_color=Color.RED;
                            mar_image=R.drawable.blackheart;
                        }
                        else
                            mar_image=R.drawable.marker;
                        break;

                    }else {
                        mar_image=R.drawable.marker;
                        marker_color=R.color.gradient_background;
                    }

                } while (cursor.moveToNext());
            }

            mMap.addMarker(new MarkerOptions() .position(new LatLng(arlist_lati.get(i), arlist_longi.get(i))).title(namelist.get(i)).
                    icon(BitmapDescriptorFactory.fromBitmap(getBitmapFromVectorDrawable(this, mar_image,marker_color))));
        }

    }
    public static Bitmap getBitmapFromVectorDrawable(Context context, int drawableId, int color) {
        Drawable drawable = ContextCompat.getDrawable(context, drawableId);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            drawable = (DrawableCompat.wrap(drawable)).mutate();
        }

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        int [] allpixels = new int [bitmap.getHeight() * bitmap.getWidth()];

        bitmap.getPixels(allpixels, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());

        for(int i = 0; i < allpixels.length; i++)
        {
            if(allpixels[i] == Color.BLACK)
            {
                allpixels[i] = color;
            }
        }
        bitmap.setPixels(allpixels,0,bitmap.getWidth(),0, 0, bitmap.getWidth(),bitmap.getHeight());
        return bitmap;
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

    // A place has been received; use requestCode to track the request.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        geocoder = new Geocoder(this, Locale.getDefault());
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                // retrive the data by using getPlace() method.
                place = PlaceAutocomplete.getPlace(this, data);
                onMap( place.getAddress().toString(),place.getAddress().toString(),data);

                mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                    @Override
                    public void onInfoWindowClick(Marker marker) {

                        marker.setVisible(false);
                        double latitude = marker.getPosition().latitude;
                        double longitude = marker.getPosition().longitude;
                        try {
                            addresses = geocoder.getFromLocation(latitude, longitude, 1);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        final String address = addresses.get(0).getAddressLine(0);
                        String sec = addresses.get(0).getSubLocality();
                        String state = addresses.get(0).getAdminArea();
                        String country = addresses.get(0).getCountryName();
                        String postalCode = addresses.get(0).getPostalCode();
                        final double lati = addresses.get(0).getLatitude();
                        final double longi = addresses.get(0).getLongitude();
                        db.fav_item(0);
                        final Dialog dialog = new Dialog(Slidenav.this);
                        dialog.setContentView(R.layout.dialog_map_window_click);
                        TextView txt_win_add = (TextView)dialog.findViewById(R.id.txt_win_add);
//                        TextView txt_place_name = (TextView)dialog.findViewById(R.id.txt_place_name);
                        final Button submit_selected_item = (Button) dialog.findViewById(R.id.submit_selected_item);
                        final ImageView favchck = (ImageView)dialog.findViewById(R.id.favchck);
                        final Spinner category_items = (Spinner)dialog.findViewById(R.id.category_items);
                        final Button add_item = (Button) dialog.findViewById(R.id.add_item);
//                        txt_place_name.setText(search_item_name);
                        dialog.setTitle(search_item_name);
                        txt_win_add.setText(address+" "+sec+" "+state+" "+country);
                        String add=sec+state+country+postalCode;
                        InsertSearch(address,add,0,lati,longi,"No icon");
                        cursor = db.getData("select id from selected_data WHERE latitude="+lati);
                        while (cursor.moveToNext()) {
                            mar_id= cursor.getInt(0);
                        }
                        if (flag == 0) {
                            favchck.setImageResource(R.drawable.blankheart);
                        } else {
                            favchck.setImageResource(R.drawable.blackheart);
                        }
                        favchck.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (flag == 1) {
                                    flag = 0;
                                    db.updatefav(0,mar_id);
                                    favchck.setImageResource(R.drawable.blankheart);
                                    category_items.setVisibility(View.GONE);
                                    add_item.setVisibility(View.GONE);
                                    submit_selected_item.setVisibility(View.GONE);

                                } else if (flag == 0) {
                                    flag = 1;
                                    db.updatefav(1,mar_id);
                                    favchck.setImageResource(R.drawable.blackheart);
                                    category_items.setVisibility(View.VISIBLE);
                                    add_item.setVisibility(View.VISIBLE);
                                    submit_selected_item.setVisibility(View.VISIBLE);
                                    cursor = db.getData("select id,insert_category,cat_checked from add_category");
                                    while (cursor.moveToNext()) {
                                        String add_cat_name = cursor.getString(1);
                                        Log.d("CatName",cursor.getString(2));
                                        list.add(add_cat_name);
                                    }
                                    ArrayAdapter<String> adp1 = new ArrayAdapter<String>(Slidenav.this,
                                            android.R.layout.simple_list_item_1, list);
                                    adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    category_items.setAdapter(adp1);

                                    category_items.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                        @Override
                                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                            s = parent.getItemAtPosition(position).toString();
                                          //  Toast.makeText(parent.getContext(), "Item 333Selected : " + s, Toast.LENGTH_SHORT).show();
                                            db.addtomarkerdata(address,lati,longi,s);
                                        }
                                        @Override
                                        public void onNothingSelected(AdapterView<?> parent) {
                                        }
                                    });
                                    submit_selected_item.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
//                                      db.addToCategory(s,randomColor);
                                            db.updateCategory(s,mar_id);
                                            db.addToCategory(s,search_item_name,getRandomColor());
                                            finish();
                                            startActivity(getIntent());
                                            dialog.dismiss();
                                        }
                                    });
                                }
                            }
                        });
                        dialog.show();
                    }

                    private void InsertSearch(String name,String address, int fav, double mark_lat,double mark_lng,String icon) {
                        item.setCategory("");
                        item.setPlace_name(name);
                        item.setPlace_add(address);
                        item.setPlace_icon(icon);
                        // item.setRating(o.getString("rating"));
                        item.setFavourites(fav);
                        item.setLatitude(mark_lat);
                        item.setLongitude(mark_lng);
                        db.insertitems(item);
                    }

                });
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                // TODO: Handle the error.
                Log.e("Tag", status.getStatusMessage());
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }

    public void onMap(String location, final String name,Intent data) {

        if (location != null || !location.equals("")) {
            Geocoder geocoder = new Geocoder(this);

            Place place = PlaceAutocomplete.getPlace(this, data);
            la = place.getLatLng().latitude;
            lo = place.getLatLng().longitude;
            search_item_name=name;
            latlist.add(la);
            lnglist.add(lo);
            for(int i=0;i<latlist.size();i++) {
                LatLng latLng = new LatLng(latlist.get(i), lnglist.get(i));
                Marker marker = mMap.addMarker(new MarkerOptions().position(latLng).title(search_item_name));
                mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));

                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,15));
                // Zoom in, animating the camera.
                mMap.animateCamera(CameraUpdateFactory.zoomIn());
                // Zoom out to zoom level 10, animating with a duration of 2 seconds.
                mMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);

                mHashMap.put(marker, i);
                Log.d("i_pos", "" + i);
            }
        }
    }

    @Override
    public void onPlaceSelected(Place place) {
        Log.i(LOG_TAG, "Place Selected: " + place
                .getName()+place.getAddress()+place.getPhoneNumber()+place
                .getWebsiteUri()+ place.getRating()+ place.getId());
    }
    @Override
    public void onError(Status status) {
        Log.e(LOG_TAG, "onError: Status = " + status.toString());
        Toast.makeText(this, "Place selection failed: " + status.getStatusMessage(),Toast.LENGTH_SHORT).show();
    }
    public int getRandomColor(){
        Random rnd = new Random();
        return Color.rgb(rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
    }
    private void prepareData() {

        cursor =db.getData("select id,latitude,longitude,place_name,category,favourites from selected_data");
        if (cursor != null && cursor.moveToFirst()) {
            do {
                ID = cursor.getInt(0);
                lat = cursor.getDouble(1);
                lon = cursor.getDouble(2);
                name = cursor.getString(3);
                cateGory=cursor.getString(4);
                arlist_lati.add(lat);
                arlist_longi.add(lon);
                favcatlist.add(cursor.getString(5));
                namelist.add(name);
                catlist.add(cateGory);
            } while (cursor.moveToNext());
        }
        cursor =db.getData("select new_category from store_category");
        if (cursor != null && cursor.moveToFirst()) {
            do {
                needcatlist.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    public void requestPermission(String strPermission, int perCode, Context _c, Activity _a){

        if (ActivityCompat.shouldShowRequestPermissionRationale(_a,strPermission)){
            Toast.makeText(Slidenav.this,"GPS permission allows us to access location data. Please allow in App Settings for additional functionality.",Toast.LENGTH_LONG).show();
        } else {

            ActivityCompat.requestPermissions(_a,new String[]{strPermission},perCode);
        }
    }

    public static boolean checkPermission(String strPermission,Context _c,Activity _a){
        int result = ContextCompat.checkSelfPermission(_c, strPermission);
        if (result == PackageManager.PERMISSION_GRANTED){

            return true;

        } else {

            return false;

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

           // Toast.makeText(Slidenav.this,"Permission Denied, You cannot access location data.",Toast.LENGTH_LONG).show();
        switch(requestCode)
        {
            case 100:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    prepareData();
                    for(int i = 0 ; i < arlist_longi.size() ; i++ ) {
                        cursor =db.getData("select id,category,marker_color,favourites from selected_data");
                        if (cursor != null && cursor.moveToFirst()) {
                            do {
                                String cat_name=cursor.getString(1);
                                if(cat_name.equals(catlist.get(i)))
                                {
                                    marker_color=cursor.getInt(2);
                                    if(cursor.getString(3).equals("1"))
                                    {
                                        marker_color=Color.RED;
                                        mar_image=R.drawable.blackheart;
                                    }
                                    else
                                        mar_image=R.drawable.marker;
                                    break;

                                }else {
                                    mar_image=R.drawable.marker;
                                    marker_color=R.color.gradient_background;
                                }

                            } while (cursor.moveToNext());
                        }
                        mMap.addMarker(new MarkerOptions() .position(new LatLng(arlist_lati.get(i), arlist_longi.get(i))).title(namelist.get(i)).
                                icon(BitmapDescriptorFactory.fromBitmap(getBitmapFromVectorDrawable(this, mar_image,marker_color))));

                    }
                    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
                    if (toolbar != null) {
                        setSupportActionBar(toolbar);
                    }

                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                    getSupportActionBar().setDisplayShowHomeEnabled(true);
                    item = new ListItem();
                   // supportMapFragment.getMapAsync(this);

                    gps = new GPSTracker(Slidenav.this);
                    if(gps.canGetLocation()){
                        latitude = gps.getLatitude();
                        longitude = gps.getLongitude();
                        Log.d("FIRSTPageTestOnREQ",""+latitude+"LOng: "+longitude);
                    }else{
                        gps.showSettingsAlert();
                    }
                   // fetchLocationData();

                } else {

                    Toast.makeText(getApplicationContext(),"Permission Denied, You cannot access location data.",Toast.LENGTH_LONG).show();

                }
                break;

        }
    }
    @Override
    public void onRestart() {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }
}