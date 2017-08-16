package com.example.lenovo.gpslocation;

import android.content.Intent;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class MyNeeds extends BaseActivity{
    public static DatabaseHandler db;
    private RecyclerView Items_show;
    private RecyclerView.Adapter adapter;
    private List<RecyclerViewListItems> recyclerlistItems;
    ArrayList<RecyclerViewListItems> android_version = new ArrayList<>();
    Cursor cursor;
    RelativeLayout mainlayout;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_needs);
        mainlayout=(RelativeLayout) this.findViewById(R.id.hidelayout);
        mainlayout.setVisibility(View.GONE);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        db = new DatabaseHandler(MyNeeds.this, null, null, 14);
        Items_show = (RecyclerView)findViewById(R.id.card_recycler_view);
        Items_show.setHasFixedSize(true);
        Items_show.setLayoutManager(new LinearLayoutManager(this));
        recyclerlistItems = new ArrayList<>();
        cursor = getApplicationContext().getContentResolver().query(Uri.parse("android-app://com.example.lenovo.gpslocation"), null, null, null, null);
        cursor = db.getData("select id,new_category,marker_color from store_category");
        if (cursor != null && cursor.moveToFirst()) {
            do {
            //    Toast.makeText(this, "My Needs: "+name +"  "+marker_color, Toast.LENGTH_SHORT).show();
                RecyclerViewListItems androidVersion = new RecyclerViewListItems();
                androidVersion.setID(cursor.getInt(0));
                androidVersion.setAndroid_version_name(cursor.getString(1));
                androidVersion.setMarker_Color(cursor.getInt(2));
                android_version.add(androidVersion);
            } while (cursor.moveToNext());


        }else
        {
            mainlayout.setVisibility(View.VISIBLE);

        }

       /* adapter = new RecyclerViewAdapter(recyclerlistItems,getApplicationContext());
        Items_show.setAdapter(adapter);*/
        DataAdapter adapter = new DataAdapter(getApplicationContext(),android_version);
        Items_show.setAdapter(adapter);
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


}
