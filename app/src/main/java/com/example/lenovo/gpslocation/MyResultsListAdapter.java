package com.example.lenovo.gpslocation;

/**
 * Created by Lenovo on 6/16/2017.
 */
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
public class MyResultsListAdapter extends BaseExpandableListAdapter{
    private LayoutInflater inflater;
    private ArrayList<Parent> mParent;
    public DatabaseHandler db;

    public MyResultsListAdapter(Context context, ArrayList<Parent> parent){
        mParent = parent;
        inflater = LayoutInflater.from(context);
    }
    public void setNewItems(Context context, ArrayList<Parent> parent) {
        mParent = parent;
        inflater = LayoutInflater.from(context);
        notifyDataSetChanged();
    }
    @Override
    //counts the number of group/parent items so the list knows how many times calls getGroupView() method
    public int getGroupCount() {
        return mParent.size();
    }
    @Override
    //counts the number of children items so the list knows how many times calls getChildView() method
    public int getChildrenCount(int i) {
        return mParent.get(i).getArrayChildren().size();
    }
    @Override
    //gets the title of each parent/group
    public Object getGroup(int i) {
        return mParent.get(i).getTitle();
    }
    @Override
    //gets the name of each item
    public Object getChild(int i, int i1) {
        return mParent.get(i).getArrayChildren().get(i1);
    }
    @Override
    public long getGroupId(int i) {
        return i;
    }
    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }
    @Override
    public boolean hasStableIds() {
        return true;
    }
    @Override
    //in this method you must set the text to see the parent/group on the list
    public View getGroupView(final int groupPosition, boolean b, View view, ViewGroup viewGroup) {
        ViewHolder holder = new ViewHolder();
        holder.groupPosition = groupPosition;
        if (view == null) {
            view = inflater.inflate(R.layout.list_item_parent, viewGroup,false);
        }
        final TextView textView = (TextView) view.findViewById(R.id.list_item_text_view);
        ImageView imageView = (ImageView) view.findViewById(R.id.img_android);
        ImageView imageDel = (ImageView) view.findViewById(R.id.img_delete);
        textView.setText(getGroup(groupPosition).toString());
        imageView.setColorFilter(mParent.get(groupPosition).getMarker_Color());
        imageDel.setOnClickListener(new View.OnClickListener() {
            public void onClick(final View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(v.getContext());
                alertDialog.setTitle("Delete Item");
                alertDialog.setMessage("Are you sure you want to delete this?");
                alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int which) {
                        db = new DatabaseHandler(v.getContext(), null, null, 14);
                        db.delete_need(mParent.get(groupPosition).getID());
                        db.updateCHeckedCat(false,getGroup(groupPosition).toString());
                        db.delete_needfromSelectedTable(getGroup(groupPosition).toString());
                        ((Activity)v.getContext()).finish();
                        v.getContext().startActivity(((Activity) v.getContext()).getIntent());
                    }
                });
                alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                alertDialog.show();
            }
        });
        view.setTag(holder);
        //return the entire view
        return view;
    }
    public void removeGroup(int group) {
        //TODO: Remove the according group. Dont forget to remove the children aswell!
        Log.v("Adapter", "Removing group"+group);
        notifyDataSetChanged();
    }
    @Override
    //in this method you must set the text to see the children on the list
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View view, ViewGroup viewGroup) {
        ViewHolder holder = new ViewHolder();
        holder.childPosition = childPosition;
        holder.groupPosition = groupPosition;
        if (view == null) {
            view = inflater.inflate(R.layout.list_item_child, viewGroup,false);
        }
        TextView tvName = (TextView) view.findViewById(R.id.list_item_text_name);
        TextView textView = (TextView) view.findViewById(R.id.list_item_text_location);
        RatingBar ratingBar = (RatingBar)view.findViewById(R.id.pop_ratingbar);
        tvName.setText(mParent.get(groupPosition).getArrayName().get(childPosition));
        textView.setText(mParent.get(groupPosition).getArrayChildren().get(childPosition));
        ratingBar.setRating(mParent.get(groupPosition).getArrayRate().get(childPosition));
        LayerDrawable stars = (LayerDrawable) ratingBar.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(Color.parseColor("#FDA32B"), PorterDuff.Mode.SRC_ATOP);
        stars.getDrawable(1).setColorFilter(Color.parseColor("#FDA32B"), PorterDuff.Mode.SRC_ATOP);
        view.setTag(holder);
        //return the entire view
        return view;
    }
    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }
    @Override
    public void registerDataSetObserver(DataSetObserver observer) {
        /* used to make the notifyDataSetChanged() method work */
        super.registerDataSetObserver(observer);
    }

    protected class ViewHolder {
        protected int childPosition;
        protected int groupPosition;
        protected Button button;
    }
}