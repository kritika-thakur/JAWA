package com.example.lenovo.gpslocation;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 * Created by ideafoundation on 30/08/17.
 */

public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private Context _context;
    private ArrayList<category_model> _listDataHeader; // header titles
    DatabaseHandler db;
    CheckBox cb;
    double latitude, longitude;
    String URL;
    GPSTracker gps;
    Cursor cursor;
    ListItem item;
    Selectitem s_items;
    ArrayList<Double> lattList = new ArrayList<>();
    ArrayList<Double> langList = new ArrayList<>();
    private final boolean[] mCheckedState;
    // child data in format of header title, child title
    // private HashMap<String, List<String>> _listDataChild;

    public ExpandableListAdapter(Context context, ArrayList<category_model> listDataHeader) {
        this._context = context;
        mCheckedState = new boolean[listDataHeader.size()];
        this._listDataHeader = listDataHeader;
        //this._listDataChild = listChildData;
    }

    @Override
    public Object getChild(int i, int i1) {
        return _listDataHeader.get(i).getArrayChildren().get(i1);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final String childText = (String) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_item, null);
        }

        TextView txtListChild = (TextView) convertView
                .findViewById(R.id.lblListItem);

        //txtListChild.setText(childText);
        // txtListChild.setText(_listDataHeader.get(groupPosition).getArrayName().get(childPosition));
        return convertView;
    }

    @Override
    public int getChildrenCount(int i) {
        return _listDataHeader.get(i).getArrayChildren().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition).getCategory_name();
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        db = new DatabaseHandler(_context, null, null, 21);
        s_items = new Selectitem();
        cursor = _context.getContentResolver().query(Uri.parse("android-app://com.example.lenovo.gpslocation"), null, null, null, null);

        item = new ListItem();
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_group, null);
        }
        //CheckBox result = (CheckBox)convertView;

        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.lblListHeader);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);
        cb = (CheckBox) convertView.findViewById(R.id.checkBox);

        cursor = db.getData("select id,insert_category,cat_checked from add_category");
        while (cursor.moveToNext()) {
                Log.d("Comaprison",""+(_listDataHeader.get(groupPosition).getCategory_name().equals(cursor.getString(1))));
        }


        cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                gps = new GPSTracker(_context);
                if (gps.canGetLocation()) {
                    latitude = gps.getLatitude();
                    longitude = gps.getLongitude();
                    lattList.add(latitude);
                    langList.add(longitude);
                } else {
                    gps.showSettingsAlert();
                }
                if (isChecked) {
                    Toast.makeText(_context, "Checked  " + _listDataHeader.get(groupPosition).getCategory_name(), Toast.LENGTH_SHORT).show();
                    db.updateCHeckedCat(true, _listDataHeader.get(groupPosition).getCategory_name());
                    URL = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" + latitude + "," + longitude + "&radius=100&type=" + _listDataHeader.get(groupPosition).getCat_type() + "&sensor=true&key=AIzaSyAxGdWYQ4wzVFzAXehZpeV-2t7GuOSL5q4";
                    s_items.loadRecyclerViewData(_context, _listDataHeader.get(groupPosition).getCategory_name(), URL, db, lattList, langList, item);
                    // Toast.makeText(Selectitem.this,"color"+getRandomColor(),Toast.LENGTH_SHORT).show();
                    db.addToCategory(_listDataHeader.get(groupPosition).getCategory_name(), _listDataHeader.get(groupPosition).getCat_type(), getRandomColor());

                } else {
                    db.delete_items(_listDataHeader.get(groupPosition).getCategory_name());
                    db.updateCHeckedCat(false, _listDataHeader.get(groupPosition).getCategory_name());
                    Toast.makeText(_context, "UNChecked  " + _listDataHeader.get(groupPosition).getCategory_name(), Toast.LENGTH_SHORT).show();
                }
                int getPosition = (Integer) buttonView.getTag();  // Here we get the position that we have set for the checkbox using setTag.
                _listDataHeader.get(getPosition).setCat_checked(buttonView.isChecked()); // Set the value of checkbox to maintain its state.
            }
        });
        cb.setTag(groupPosition); // This line is important.
        cb.setChecked(_listDataHeader.get(groupPosition).checked);
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }


    public int getRandomColor() {
        Random rnd = new Random();
        return Color.rgb(rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
    }
}