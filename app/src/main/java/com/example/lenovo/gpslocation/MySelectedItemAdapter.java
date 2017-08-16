package com.example.lenovo.gpslocation;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

public class MySelectedItemAdapter extends ArrayAdapter<category_model> {

    ArrayList<category_model> animalList = new ArrayList<>();
    Context mContext;
    Cursor cursor;
    DatabaseHandler db;
    private ArrayList<category_model> arraylist;
    private static class ViewHolder {
        TextView textView;
        CheckBox checkBox;
    }
    public MySelectedItemAdapter(Context context, int textViewResourceId, ArrayList<category_model> objects) {
        super(context, textViewResourceId, objects);
        db = new DatabaseHandler(context, null, null, 21);
        animalList = objects;
        this.arraylist = new ArrayList<category_model>();
        this.arraylist.addAll(animalList);
    }
    public MySelectedItemAdapter(ArrayList<category_model> data, Context context) {
        super(context, R.layout.list_view_items, data);
        this.animalList = data;
        this.mContext = context;
    }
    @Override
    public int getCount() {
        return super.getCount();
    }
    @Override
    public category_model getItem(int position) {
        return animalList.get(position);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        final View result;
        if (convertView == null) {
        viewHolder = new ViewHolder();
        convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_view_items, parent, false);
            viewHolder.textView = (TextView) convertView.findViewById(R.id.textView);
            viewHolder.checkBox = (CheckBox) convertView.findViewById(R.id.checkBox);
            result=convertView;
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        category_model item = getItem(position);
        viewHolder.textView.setText(item.category_name);
        viewHolder.checkBox.setChecked(item.checked);
        return result;

    }
    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        animalList.clear();
        arraylist.clear();
        cursor = db.getData("select id,insert_category,cat_type,cat_checked from add_category");
        if (cursor != null && cursor.moveToFirst()) {
            do {
                boolean cat_check;
                String cat_checked=cursor.getString(3);
                if(cat_checked.equals("true"))
                    cat_check=true;
                else
                    cat_check=false;
                arraylist.add(new category_model( cursor.getString(1),  cursor.getString(2), cat_check));
            } while (cursor.moveToNext());
        }
        if (charText.length() == 0) {
            animalList.addAll(arraylist);
        }
        else
        {
            for (category_model wp : arraylist)
            {
                if (wp.getCategory_name().toLowerCase(Locale.getDefault()).contains(charText))
                {
                    animalList.add(wp);
                }
            }
        }

        notifyDataSetChanged();
    }
}
