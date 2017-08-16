package com.example.lenovo.gpslocation;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MapWindowClick extends AppCompatActivity {
    TextView txt_win_add;
    ImageView favchck;
    int flag = 0;
    final Context context = this;
    public static DatabaseHandler db;
    int mar_id,favo;
    double mark_lat,mark_lng;
    ListItem item;
    String name,sector,state,country,postal_code,s;
    ArrayList<Integer> arfav = new ArrayList<>();
    Cursor cursor;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_window_click);
        db = new DatabaseHandler(MapWindowClick.this, null, null, 20);
        item = new ListItem();
        final List<String> list = new ArrayList<String>();
        cursor = getApplicationContext().getContentResolver().query(getReferrer(), null, null, null, null);
        final Dialog dialog = new Dialog(context);
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_list_categories);
        txt_win_add = (TextView)findViewById(R.id.txt_win_add);
        favchck = (ImageView)findViewById(R.id.favchck);
        Intent intent = getIntent();
        name  = intent.getStringExtra("key_add");
        sector = intent.getStringExtra("key_sec");
        state = intent.getStringExtra("key_state");
        country = intent.getStringExtra("key_country");
        postal_code = intent.getStringExtra("key_postal");
        mark_lat = intent.getDoubleExtra("key_lati",0);
        mark_lng = intent.getDoubleExtra("key_longi",0);
        txt_win_add.setText(name+" "+sector+" "+state+" "+country+" "+postal_code+"\n"+mark_lat+"\n"+mark_lng);
        //INSERTING SEARCHED DATA
        String address=sector+state+country+postal_code;
        InsertSearch(name,address,0,mark_lat,mark_lng,"No icon");
        //getting data for key id

        cursor = db.getData("select id from selected_data WHERE latitude="+mark_lat);
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
                } else if (flag == 0) {
                    flag = 1;
                    db.updatefav(1,mar_id);
                    favchck.setImageResource(R.drawable.blackheart);
                    dialog.setTitle("Add to need table");
                    Spinner category_items = (Spinner)dialog.findViewById(R.id.category_items);

                    cursor = db.getData("select id,insert_category,cat_checked from add_category");
                    while (cursor.moveToNext()) {
                        String add_cat_name = cursor.getString(1);
                        Log.d("CatName",cursor.getString(2));
                        list.add(add_cat_name);
                    }
                    ArrayAdapter<String> adp1 = new ArrayAdapter<String>(MapWindowClick.this,
                            android.R.layout.simple_list_item_1, list);
                    adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    category_items.setAdapter(adp1);

                    Button testbutton = (Button) dialog.findViewById(R.id.testbutton);
                    category_items.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            s = parent.getItemAtPosition(position).toString();
                            Toast.makeText(parent.getContext(), "Item 333Selected : " + s, Toast.LENGTH_SHORT).show();
                            db.addtomarkerdata(name,mark_lat,mark_lng,s);
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                        }
                    });
                    testbutton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

//                            db.addToCategory(s,randomColor);
                            db.updateCategory(s,mar_id);
                            dialog.dismiss();
                            //Log.d("BOOMBOOM",s+":id: "+mar_id);
                        }
                    });
                    dialog.show();
                }
              //  getFav();
            }
        });
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

    public void getFav(){
        cursor = MapWindowClick.db.getData("select id,fav_item from add_marker_data where id = '" + mar_id + "'");
        while (cursor.moveToNext()) {
            favo =  cursor.getInt(1);
            arfav.add(favo);
        }
    }
}