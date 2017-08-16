package com.example.lenovo.gpslocation;

import android.Manifest;
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
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
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

public class Slidenav extends BaseActivity implements OnMapReadyCallback, AdapterView.OnItemClickListener, PlaceSelectionListener {
    GoogleMap mMap;
    GPSTracker gps;
    double latitude;
    double longitude;
    ListItem item;
    Cursor cursor;
    DatabaseHandler db;
    int ID;
    String name, cateGory;
    double lat, lon;
    int marker_color;
    ArrayList<Double> latlist = new ArrayList<>();
    ArrayList<String> catlist = new ArrayList<>();
    ArrayList<Double> lnglist = new ArrayList<>();
    ArrayList<String> namelist = new ArrayList<>();
    private HashMap<Marker, Integer> mHashMap = new HashMap<Marker, Integer>();
    double la;
    category_model cat_item;
    double lo;
    private Map<Marker, Class> allMarkersMap = new HashMap<Marker, Class>();
    private static final String LOG_TAG = "PlaceSelectionListener";
    Geocoder geocoder;
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
        SupportMapFragment supportMapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mMap = supportMapFragment.getMap();
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
        mMap.setMyLocationEnabled(true);
        prepareData();
        for(int i = 0 ; i < latlist.size() ; i++ ) {
            cursor =db.getData("select id,new_category,marker_color from store_category");
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    String cat_name=cursor.getString(1);
                    if(cat_name.equals(catlist.get(i)))
                    {
                        marker_color=cursor.getInt(2);
                        break;

                    }

                  } while (cursor.moveToNext());
            }
            mMap.addMarker(new MarkerOptions() .position(new LatLng(latlist.get(i), lnglist.get(i))).title(namelist.get(i)).
                    icon(BitmapDescriptorFactory.fromBitmap(getBitmapFromVectorDrawable(this, R.drawable.marker,marker_color))));

        }
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
                onMap( place.getAddress().toString(),place.getAddress().toString());
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
                        String address = addresses.get(0).getAddressLine(0);
                        String sec = addresses.get(0).getSubLocality();
                        String state = addresses.get(0).getAdminArea();
                        String country = addresses.get(0).getCountryName();
                        String postalCode = addresses.get(0).getPostalCode();
                        double lati = addresses.get(0).getLatitude();
                        double longi = addresses.get(0).getLongitude();
                        Intent intent = new Intent(Slidenav.this, MapWindowClick.class);
                        intent.putExtra("key_add", address);
                        intent.putExtra("key_sec", sec);
                        intent.putExtra("key_state", state);
                        intent.putExtra("key_country",country);
                        intent.putExtra("key_postal",postalCode);
                        intent.putExtra("key_lati",lati);
                        intent.putExtra("key_longi",longi);
                        db.fav_item(0);
                        startActivity(intent);
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

    public void onMap(String location, final String name) {
        List<Address> addressList = null;
        if (location != null || !location.equals("")) {
            Geocoder geocoder = new Geocoder(this);
            try {
                addressList = geocoder.getFromLocationName(location, 1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            //IndexOutOfBoundException
            Address address = addressList.get(0);
            la = address.getLatitude();
            lo = address.getLongitude();
            latlist.add(la);
            lnglist.add(lo);
            namelist.add(name);
            for(int i=0;i<latlist.size();i++) {
                LatLng latLng = new LatLng(latlist.get(i), lnglist.get(i));
                Marker marker = mMap.addMarker(new MarkerOptions().position(latLng).title(namelist.get(i)));
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
    private void prepareData() {

        cursor =db.getData("select id,latitude,longitude,place_name,category from selected_data where favourites = 1");
        if (cursor != null && cursor.moveToFirst()) {
            do {
                ID = cursor.getInt(0);
                lat = cursor.getDouble(1);
                lon = cursor.getDouble(2);
                name = cursor.getString(3);
                cateGory=cursor.getString(4);
                latlist.add(lat);
                lnglist.add(lon);
                namelist.add(name);
                catlist.add(cateGory);
            } while (cursor.moveToNext());
        }
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}