package com.example.lenovo.gpslocation;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class PlaceCategoriesActivity extends BaseActivity  implements OnMapReadyCallback {
    private ExpandableListView mExpandableList;
    private GoogleMap mMap;
    GPSTracker gps;
    // String groupItems[], groupTypeItems[];
    public static DatabaseHandler db;
    String name, info,category,location,URL;
    float rating;
    final Context context = this;
    ImageView favchck;
    double latitude, lat;
    double longitude, lng;
    ListItem item;
    CheckBox cb;
    ArrayList <category_model> foos= new ArrayList<>();
    int flag = 0,ID,favourites,checkedOrNot=0;
    ArrayList<String> ar = new ArrayList<String>();
    ArrayList<String> CatsListingSelected= new ArrayList<String>();
    ArrayList<String> Cat_typeList = new ArrayList<String>();
    ArrayList<Integer> Colormarker = new ArrayList<Integer>();
    ArrayList<Integer> IDList = new ArrayList<Integer>();
    ArrayList<Integer> arlist = new ArrayList<>();
    ArrayList<Integer> favlist = new ArrayList<>();
    ArrayList<String> namelist = new ArrayList<>();
    ArrayList<Double> latlist = new ArrayList<>();
    ArrayList<Double> lnglist = new ArrayList<>();
    ArrayList<category_model> arrayParents = new ArrayList<category_model>();
    ArrayList<String> arrayChildren = new ArrayList<String>();
    ArrayList<Float> arRate = new ArrayList<Float>();
    Cursor cursor;
    Selectitem s_items;
    ExpandableListAdapter cust;
    RelativeLayout mainlayout;
    category_model cat_item;
    String categories,type,cat,add_cat;
    ArrayList<String> Lines = new ArrayList<String>();

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_categories);
        final SQLiteDatabase database = this.openOrCreateDatabase("Mydatabase.db", MODE_PRIVATE, null);
        s_items = new Selectitem();
        db = new DatabaseHandler(PlaceCategoriesActivity.this, null, null, 14);
        item = new ListItem();
        mainlayout=(RelativeLayout) this.findViewById(R.id.hidelayout);
        mainlayout.setVisibility(View.GONE);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        final Dialog dialog = new Dialog(context);
        cat_item = new category_model(categories,type,false);

        dialog.setContentView(R.layout.custom);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_view);
        mapFragment.getMapAsync(this);
        gps = new GPSTracker(PlaceCategoriesActivity.this);
        if (gps.canGetLocation()) {
            latitude = gps.getLatitude();
            longitude = gps.getLongitude();
            // loadRecyclerViewData(context,categories,URL,db,lattList,langList,item);
        } else {
            gps.showSettingsAlert();
        }
        cursor = getApplicationContext().getContentResolver().query(Uri.parse("android-app://com.example.lenovo.gpslocation"), null, null, null, null);
        mExpandableList = (ExpandableListView) findViewById(R.id.expandable_list);

        foos = new ArrayList<category_model>();
        foos.add(new category_model("ATM","atm",false));
        foos.add(new category_model("Airport","airport",false));
        foos.add(new category_model("Accounting","accounting",false));
        foos.add(new category_model("Amusement Park","amusement_park",false));
        foos.add(new category_model("Aquarium","aquarium",false));
        foos.add(new category_model("Art Gallery","art_gallery",false));
        foos.add(new category_model("Bank","bank",false));
        foos.add(new category_model("Bakery","bakery",false));
        foos.add(new category_model("Bar","bar",false));
        foos.add(new category_model("Beauty Salon", "beauty_salon",false));
        foos.add(new category_model("Bicycle Store", "bicycle_store",false));
        foos.add(new category_model("Bowling Alley", "bowling_alley",false));
        foos.add(new category_model("Book Store","book_store",false));
        foos.add(new category_model("Bus Station","bus_station",false));
        foos.add(new category_model("Cafe","cafe",false));
        foos.add(new category_model("Car Repair","car_repair",false));
        foos.add(new category_model("Camp Ground", "campground",false));
        foos.add(new category_model("Car Dealer", "car_dealer",false));
        foos.add(new category_model("Car Rental", "car_rental",false));
        foos.add(new category_model("Car Wash", "car_wash",false));
        foos.add(new category_model("Casino", "casino",false));
        foos.add(new category_model("Cemetery", "cemetery",false));
        foos.add(new category_model("Church", "church",false));
        foos.add(new category_model("City Hall", "city_hall",false));
        foos.add(new category_model("Clothing Store", "clothing_store",false));
        foos.add(new category_model("Convenience Store", "convenience_store",false));
        foos.add(new category_model("Court House" ,"courthouse",false));
        foos.add(new category_model("Dentist","dentist",false));
        foos.add(new category_model("Department Store", "department_store",false));
        foos.add(new category_model("Doctor", "doctor",false));
        foos.add(new category_model("Electrician", "electrician",false));
        foos.add(new category_model("Electronics Store", "electronics_store",false));
        foos.add(new category_model("Embassy", "embassy",false));
        foos.add(new category_model("Fire Station","fire_station",false));
        foos.add(new category_model("Florist", "florist",false));
        foos.add(new category_model("Funeral Home", "funeral_home",false));
        foos.add(new category_model("Furniture Store", "furniture_store",false));
        foos.add(new category_model("Gas Station","gas_station",false));
        foos.add(new category_model("Gym","gym",false));
        foos.add(new category_model("Hospital","hospital",false));
        foos.add(new category_model("Hair Care", "hair_care",false));
        foos.add(new category_model("Hardware Store", "hardware_store",false));
        foos.add(new category_model("Hindu Temple", "hindu_temple",false));
        foos.add(new category_model("Home Goods Store" ,"home_goods_store",false));
        foos.add(new category_model("Insurance Agency", "insurance_agency",false));
        foos.add(new category_model("Jewellery Store", "jewelry_store",false));
        foos.add(new category_model("Laundry", "laundry",false));
        foos.add(new category_model("Lawyer", "lawyer",false));
        foos.add(new category_model("Liquor Store", "liquor_store",false));
        foos.add(new category_model("Local Government Office","local_government_office",false));
        foos.add(new category_model("Locksmith", "locksmith",false));
        foos.add(new category_model("Lodging", "lodging",false));
        foos.add(new category_model("Library","library",false));
        foos.add(new category_model("Meal Delivery", "meal_delivery",false));
        foos.add(new category_model("Meal Takeaway" ,"meal_takeaway",false));
        foos.add(new category_model("Mosque", "mosque",false));
        foos.add(new category_model("Movie Rental", "movie_rental",false));
        foos.add(new category_model("Movie Theatre" ,"movie_theater",false));
        foos.add(new category_model("Movie Theatre" ,"movie_theater",false));
        foos.add(new category_model("Moving Company","moving_company",false));
        foos.add(new category_model("Museum", "museum",false));
        foos.add(new category_model("Night Club", "night_club",false));
        foos.add(new category_model("Pharmacy","pharmacy",false));
        foos.add(new category_model("Police","police",false));
        foos.add(new category_model("Painter", "painter",false));
        foos.add(new category_model("Park", "park",false));
        foos.add(new category_model("Parking", "parking",false));
        foos.add(new category_model("Pet Store", "pet_store",false));
        foos.add(new category_model("Physiotherapist", "physiotherapist",false));
        foos.add(new category_model("Plumber", "plumber",false));
        foos.add(new category_model("Post Office","post_office",false));
        foos.add(new category_model("Real Estate Agency", "real_estate_agency",false));
        foos.add(new category_model("Roofing Contractor", "roofing_contractor",false));
        foos.add(new category_model("Restaurant","restaurant",false));
        foos.add(new category_model("RV Park", "rv_park",false));
        foos.add(new category_model("Shoe Store", "shoe_store",false));
        foos.add(new category_model("Shopping Mall", "shopping_mall",false));
        foos.add(new category_model("Spa", "spa",false));
        foos.add(new category_model("Stadium" ,"stadium",false));
        foos.add(new category_model("Storage" ,"storage",false));
        foos.add(new category_model("Store" ,"store",false));
        foos.add(new category_model("Subway Station" ,"subway_station",false));
        foos.add(new category_model("Synagogue" ,"synagogue",false));
        foos.add(new category_model("Taxi stand" ,"taxi_stand",false));
        foos.add(new category_model("Train Station" ,"train_station",false));
        foos.add(new category_model("Transit Station" ,"transit_station",false));
        foos.add(new category_model("Travel Agency" ,"travel_agency",false));
        foos.add(new category_model("University" ,"university",false));
        foos.add(new category_model("Veterinary Care" ,"veterinary_care",false));
        foos.add(new category_model("Zoo" ,"zoo",false));

        for(int i=0;i<foos.size();i++)
        {
            cursor = db.getData("select * from add_category WHERE insert_category='"+foos.get(i).category_name+"'");
            if (cursor != null && cursor.moveToFirst()) {
                Log.d("Exists Already",""+add_cat);
            }else {
                cat_item.setCategory_name(foos.get(i).category_name);
                cat_item.setCat_type(foos.get(i).cat_type);
                cat_item.setCat_checked(foos.get(i).checked);
                db.insertAllcategoryList(cat_item);
                //  foos.add(new category_model(foos.get(i).category_name,foos.get(i).cat_type,false));
            }

        }
        cust = new ExpandableListAdapter(context, arrayParents);
        cursor = db.getData("select id,insert_category,cat_type from add_category");
        if (cursor != null && cursor.moveToFirst()) {
            do {
                IDList.add(cursor.getInt(0));
                CatsListingSelected.add(cursor.getString(1));
              //  Colormarker.add(cursor.getInt(2));
                Cat_typeList.add(cursor.getString(2));
            } while (cursor.moveToNext());
        }else
            mainlayout.setVisibility(View.VISIBLE);

        //here we set the parents and the children
        for (int i = 0; i < CatsListingSelected.size(); i++) {
            //for each "i" create a new Parent object to set the title and the children
            category_model parent = new category_model();
            try {
                parent.setCategory_name(CatsListingSelected.get(i));
                /*parent.setMarker_Color(Colormarker.get(i));
                parent.setID(IDList.get(i));*/
            }
            catch(IndexOutOfBoundsException exception) {
                exception.printStackTrace();
            }

            arrayChildren = new ArrayList<String>();
            for (int j = 0; j < 10; j++) {
                arrayChildren.add("Child " + j);
            }
          //  parent.setArrayName(namelist);
            parent.setArrayChildren(ar);
         //   parent.setArrayRate(arRate);
            //in this array we add the Parent object. We will use the arrayParents at the setAdapter
            arrayParents.add(parent);

        }
        cursor = db.getData("select id,new_category from store_category");
        while (cursor.moveToNext()) {
            for (int i=0;i<foos.size();i++) {
                if (foos.get(i).category_name.equals(cursor.getString(1))) {

                    mExpandableList.setItemChecked(i, true);
                    foos.get(i).checked = true;
                    Log.d("Cate_checked"," "+foos.get(i).checked);
                    mExpandableList.deferNotifyDataSetChanged();
                    cust.notifyDataSetChanged();
                    break;
                }
            }
        }
        //sets the adapter that provides data to the list.
        mExpandableList.setAdapter(cust);
        mExpandableList.deferNotifyDataSetChanged();
        alreadychecked();
        mExpandableList.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                // display a toast with group name whenever a user clicks on a group item
                cb=(CheckBox)v.findViewById(R.id.checkBox);
                Toast.makeText(PlaceCategoriesActivity.this, "Clicked Group  "+foos.get(groupPosition).getCategory_name(), Toast.LENGTH_SHORT).show();
                /*category_model dataModel= foos.get(groupPosition);
                db.updateCHeckedCat(true, foos.get(groupPosition).category_name);
                dataModel.checked = !dataModel.checked;

                if(checkedOrNot==0)
                {
                    checkedOrNot=1;
                    mExpandableList.setItemChecked(groupPosition,true);
                }else{
                    checkedOrNot=0;
                    mExpandableList.setItemChecked(groupPosition,false);
                }

                cust.notifyDataSetChanged();
                if (mExpandableList.isItemChecked(groupPosition)) {

                    categories = foos.get(groupPosition).category_name;
                    type= foos.get(groupPosition).cat_type;

                    Global g = Global.getInstance();
                    float data=g.getData();
                    Log.d("BOOMBOOM Radius: ",""+data);
                    URL = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" + latitude + "," + longitude + "&radius=100&type="+type+"&sensor=true&key=AIzaSyAxGdWYQ4wzVFzAXehZpeV-2t7GuOSL5q4";
                    Lines.add(categories);
                    s_items.loadRecyclerViewData(context,category,URL,db,latlist,lnglist,item);
                    try{
                         Toast.makeText(PlaceCategoriesActivity.this,"color"+getRandomColor(),Toast.LENGTH_SHORT).show();
                        db.addToCategory(categories,type,getRandomColor());
                    }
                    catch(SQLiteConstraintException ex){
                        //what ever you want to do
                        Log.d("Insertion repeated","Already Exists");
                    }
                } else {
                    db.delete_items(foos.get(groupPosition).category_name);
                    db.updateCHeckedCat(false, foos.get(groupPosition).category_name);
                }
                try {
                    category = CatsListingSelected.get(groupPosition);
                    Log.d("BOOMBOOM type cat",Cat_typeList.get(groupPosition));
                    type=Cat_typeList.get(groupPosition);
                }
                catch(IndexOutOfBoundsException exception) {
                    exception.printStackTrace();
                }*/
             /*   ar.clear();
                Colormarker.clear();
                favlist.clear();
                namelist.clear();
                IDList.clear();
                Global g = Global.getInstance();
                float data=g.getData();
                Log.d("BOOMBOOM RadResult: ",""+data);
                URL = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" + latitude + "," + longitude + "&radius="+data+"&type="+type+"&query="+type+"&sensor=true&key=AIzaSyAxGdWYQ4wzVFzAXehZpeV-2t7GuOSL5q4";
                arlist.clear();
                latlist.clear();
                lnglist.clear();
                getData(groupPosition);
                s_items.loadRecyclerViewData(context,category,URL,db,latlist,lnglist,item);*/


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
                LayerDrawable stars = (LayerDrawable) rating_bar.getProgressDrawable();
                stars.getDrawable(2).setColorFilter(Color.parseColor("#FDA32B"), PorterDuff.Mode.SRC_ATOP);
                stars.getDrawable(1).setColorFilter(Color.parseColor("#FDA32B"), PorterDuff.Mode.SRC_ATOP);
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
                       // cust.setNewItems(PlaceCategoriesActivity.this, arrayParents);

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

    public void alreadychecked() {
        cursor = db.getData("select id,new_category from store_category");
        while (cursor.moveToNext()) {
            int index = -1;
            for (int i=0;i<foos.size();i++) {
                if (foos.get(i).category_name.equals(cursor.getString(1))) {
                    index = i;
                    mExpandableList.setItemChecked(i, true);
                    foos.get(i).checked = true;
                    cust.notifyDataSetChanged();
                    mExpandableList.deferNotifyDataSetChanged();
                    break;
                }
            }
        }
    }

    public void getData(int position) {
        cursor = db.getData("select id,place_name,place_add,place_rating,place_icon,latitude,longitude,favourites from selected_data where category = '" + CatsListingSelected.get(position) + "'");
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
                latlist.add(latitude);
                lnglist.add(longitude);
                arRate.add(rating);
            } while (cursor.moveToNext());
        }else
            Toast.makeText(PlaceCategoriesActivity.this, "Results Not Found.Try changing radius.", Toast.LENGTH_SHORT).show();
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
        return true;
    }
    public int getRandomColor(){
        Random rnd = new Random();
        return Color.rgb(rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
    }
    @Override
    public void onRestart() {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }
}