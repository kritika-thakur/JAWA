package com.example.lenovo.gpslocation;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

public class Selectitem extends BaseActivity implements OnClickListener {
    Button button ,add_item;
    ListView listView;
    ArrayAdapter<String> adapter;
    GPSTracker gps;
    String URL;
    double latitude,longitude;
    DatabaseHandler db;
    ArrayList<category_model> arr_temp = new ArrayList<>();
    ListItem item;
    category_model cat_item;
    ArrayList<Double> lattList = new ArrayList<>();
    ArrayList<Double> langList = new ArrayList<>();
    String categories,type;
    Cursor cursor;
    String cat_name,add_cat_name;
    String cat,add_cat;
    final Context context = this;
    Map<String, Integer> mapIndex;
    MySelectedItemAdapter myAdapter;
    ArrayList <category_model> foos= new ArrayList<>();
    EditText search;
    ArrayList<String> Lines = new ArrayList<String>();


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selectitem);
        db = new DatabaseHandler(Selectitem.this, null, null, 21);

        listView = (ListView) findViewById(R.id.list1);
        button = (Button) findViewById(R.id.testbutton);
        search = (EditText) findViewById(R.id.search);
        add_item = (Button) findViewById(R.id.add_item);
        add_item.setOnClickListener(this);
        item = new ListItem();
        cat_item = new category_model(categories,type,false);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

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

        //category initialization**
        myAdapter=new MySelectedItemAdapter(this,R.layout.list_view_items,foos);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        listView.setAdapter(myAdapter);
        listView.setTextFilterEnabled(true);
       // arr_temp.addAll(foos);
        cursor = getApplicationContext().getContentResolver().query(Uri.parse("android-app://com.example.lenovo.gpslocation"), null, null, null, null);

        alreadychecked();

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //filter(String.valueOf(s));
                String text = search.getText().toString().toLowerCase(Locale.getDefault());
                myAdapter.filter(text);
            }

            @Override
            public void afterTextChanged(Editable s) {
                //Log.d("BOOMBOOM_after text",s.toString());
            }
        });
        gps = new GPSTracker(Selectitem.this);
        if (gps.canGetLocation()) {
            latitude = gps.getLatitude();
            longitude = gps.getLongitude();
            loadRecyclerViewData(context,categories,URL,db,lattList,langList,item);
        } else {
            gps.showSettingsAlert();
        }
        AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
                // AdapterView is the parent class of ListView
                ListView lv = (ListView) arg0;
                category_model dataModel= foos.get(position);
                db.updateCHeckedCat(true, foos.get(position).category_name);
                dataModel.checked = !dataModel.checked;
                myAdapter.notifyDataSetChanged();
                if (lv.isItemChecked(position)) {

                    categories = foos.get(position).category_name;
                    type= foos.get(position).cat_type;

                    Global g = Global.getInstance();
                    float data=g.getData();
                    URL = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" + latitude + "," + longitude + "&radius=1000&type="+type+"&sensor=true&key=AIzaSyAxGdWYQ4wzVFzAXehZpeV-2t7GuOSL5q4";
                    Lines.add(categories);
                    db.delete_SelectItemTable();
                    loadRecyclerViewData(context,categories,URL,db,lattList,langList,item);
                    try{
                       // Toast.makeText(Selectitem.this,"color"+getRandomColor(),Toast.LENGTH_SHORT).show();
                        db.addToCategory(categories,getRandomColor());
                    }
                    catch(SQLiteConstraintException ex){
                        //what ever you want to do
                        Log.d("Insertion repeated","Already Exists");
                    }
                } else {
                    db.delete_items(foos.get(position).category_name);
                    db.updateCHeckedCat(false, foos.get(position).category_name);
                }
            }
        };
        // Setting the ItemClickEvent listener for the listview
        listView.setOnItemClickListener(itemClickListener);
        button.setOnClickListener(this);
        getIndexList(foos);
        displayIndex();
        foos.clear();
        cursor = db.getData("select id,insert_category,cat_type,cat_checked from add_category");
        if (cursor != null && cursor.moveToFirst()) {
            do {
                boolean cat_check;
                add_cat = cursor.getString(1);
                String cat_checked=cursor.getString(3);
                if(cat_checked.equals("true"))
                    cat_check=true;
                else
                    cat_check=false;
                foos.add(new category_model(cursor.getString(1), cursor.getString(2), cat_check));
                myAdapter.notifyDataSetChanged();
            } while (cursor.moveToNext());
        }
        cursor = db.getData("select id,new_category from store_category");
        while (cursor.moveToNext()) {
            add_cat_name = cursor.getString(1);
            for (int i=0;i<foos.size();i++) {
                if (foos.get(i).category_name.equals(add_cat_name)) {
                    listView.setItemChecked(i, true);
                    foos.get(i).checked = true;

                    myAdapter.notifyDataSetChanged();
                    break;
                }
            }
        }
    }
    public int getRandomColor(){
        Random rnd = new Random();
        return Color.rgb(rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
    }
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.testbutton:
                Bundle bundle = getIntent().getExtras();
                if (bundle!=null) {
                    if(bundle.containsKey("FirstTime")){
                        boolean asdf = bundle.getBoolean("FirstTime");
                        if(asdf)
                        {
                            super.onBackPressed();
                        }
                    }
                } else {
                    Log.i("BOOMBOOM Log", "FirstTime is null");
                    SparseBooleanArray checked = listView.getCheckedItemPositions();
                    ArrayList<String> selectedItems = new ArrayList<>();
                    ArrayList<String> types = new ArrayList<>();
                    for (int i = 0; i < checked.size(); i++) {
                        // Item position in adapter
                        int position = checked.keyAt(i);
                        if (checked.valueAt(i))
                            selectedItems.add(myAdapter.getItem(position).category_name);
                        types.add(myAdapter.getItem(position).cat_type);
                    }

                    String[] outputStrArr = new String[selectedItems.size()];
                    String[] outStrArr = new String[selectedItems.size()];
                    for (int i = 0; i < selectedItems.size(); i++) {
                        outputStrArr[i] = selectedItems.get(i);
                        outStrArr[i]=types.get(i);
                    }
                    Intent intent = new Intent(getApplicationContext(),
                            ResultsActivity.class);
                    // Create a bundle object
                    Bundle b = new Bundle();
                    b.putStringArray("selectedItems", outputStrArr);
                    b.putStringArray("catTypes", outStrArr);
                    intent.putExtras(b);
                    startActivity(intent);
                }
                break;
            case R.id.add_item:
                cat = search.getText().toString().replaceFirst(" ","_").toLowerCase().trim();
                db.addIngCategories(search.getText().toString(),cat,false);
                foos.add(new category_model(search.getText().toString(),cat,false));
                Toast.makeText(Selectitem.this, search.getText()+ " is added successfully.", Toast.LENGTH_SHORT).show();
                finish();
                startActivity(getIntent());

                break;
        }

    }
    public static void loadRecyclerViewData(final Context context, final String categories, final String URL, final DatabaseHandler db, final ArrayList lattList, final ArrayList langList, final ListItem item) {
        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Loading Data...");
        progressDialog.setProgress(0);
        progressDialog.setIndeterminate(true);
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray array = jsonObject.getJSONArray("results");

                    if(array.length()==0)
                    {
                        Toast.makeText(context, "No Results found!!", Toast.LENGTH_SHORT).show();
                    }else
                    {
                        Cursor cursor =db.getData("select latitude,longitude from selected_data");
                        if (cursor != null && cursor.moveToFirst()) {
                            do {
                                lattList.add(cursor.getDouble(0));
                                langList.add(cursor.getDouble(1));
                            } while (cursor.moveToNext());
                        }


                        for (int i = 0; i < array.length(); i++) {
                            JSONObject o = array.getJSONObject(i);
                            String geometry=o.getString("geometry");
                            JSONObject jsonObj = new JSONObject(geometry);
                            JSONObject jsonOb = new JSONObject(jsonObj.getString("location"));
                            item.setCategory(categories);
                            item.setPlace_name(o.getString("name"));
                            item.setPlace_add(o.getString("vicinity"));
                            item.setPlace_icon(o.getString("icon"));
                            if (o.has("rating")) {
                                item.setRating(o.getString("rating"));
                            }else
                            {
                                item.setRating(null);
                            }
                            item.setFavourites(0);
                            item.setLatitude(Double.parseDouble(jsonOb.getString("lat")));
                            item.setLongitude(Double.parseDouble(jsonOb.getString("lng")));
                            if(lattList.contains(item.getLatitude())||(langList.contains(item.getLongitude())))
                            {
                                Log.d("Lat found ","data : "+item.getPlace_name());
                            }else
                            {

                                db.insertitems(item);
                            }
                            StringBuilder builder=new StringBuilder();
                            builder.append(item.getCategory()).append("  ").append(item.getLatitude()).append("  ").append
                                    (item.getLongitude()).append("  ").append(item.getFavourites());
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
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

    public void alreadychecked() {
        cursor = db.getData("select id,new_category from store_category");
        while (cursor.moveToNext()) {
            cat_name = cursor.getString(1);
            int index = -1;
            for (int i=0;i<foos.size();i++) {
                if (foos.get(i).category_name.equals(cat_name)) {
                    index = i;
                    listView.setItemChecked(i, true);
                    foos.get(i).checked = true;
                    myAdapter.notifyDataSetChanged();
                    break;
                }
            }
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    //for side alphabetical list
    private void getIndexList(ArrayList<category_model> fruits) {
        mapIndex = new LinkedHashMap<String, Integer>();
        for (int i = 0; i < fruits.size(); i++) {
            String fruit = fruits.get(i).category_name;
            String index = fruit.substring(0, 1);
            if (mapIndex.get(index) == null)
                mapIndex.put(index, i);
        }
    }
    //for side alphabetical list
    private void displayIndex() {
        LinearLayout indexLayout = (LinearLayout) findViewById(R.id.side_index);
        TextView textView;
        List<String> indexList = new ArrayList<String>(mapIndex.keySet());
        for (String index : indexList) {
            textView = (TextView) getLayoutInflater().inflate(
                    R.layout.side_index_item, null);
            textView.setText(index);
            textView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    TextView selectedIndex = (TextView) v;
                    listView.setSelection(mapIndex.get(selectedIndex.getText()));
                }
            });
            indexLayout.addView(textView);
        }
    }

}