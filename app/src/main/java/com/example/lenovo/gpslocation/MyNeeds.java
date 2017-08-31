package com.example.lenovo.gpslocation;

import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

public class MyNeeds extends BaseActivity{
    public static DatabaseHandler db;
    private RecyclerView Items_show;
    private RecyclerView.Adapter adapter;
    private List<NeedsModel> recyclerlistItems;
    ArrayList<NeedsModel> android_version = new ArrayList<>();
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
                NeedsModel androidVersion = new NeedsModel();
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
        MyNeedsAdapter adapter = new MyNeedsAdapter(getApplicationContext(),android_version);
        Items_show.setAdapter(adapter);
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onRestart() {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }
}
