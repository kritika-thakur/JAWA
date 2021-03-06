package com.example.lenovo.gpslocation;

/**
 * Created by Lenovo on 6/16/2017.
 */
import android.content.Context;
import android.database.DataSetObserver;
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
public class MyCustomAdapter extends BaseExpandableListAdapter{
    private LayoutInflater inflater;
    private ArrayList<Parent> mParent;
    public MyCustomAdapter(Context context, ArrayList<Parent> parent){
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
    public View getGroupView(int groupPosition, boolean b, View view, ViewGroup viewGroup) {
        ViewHolder holder = new ViewHolder();
        holder.groupPosition = groupPosition;
        if (view == null) {
            view = inflater.inflate(R.layout.list_item_parent, viewGroup,false);
        }
        TextView textView = (TextView) view.findViewById(R.id.list_item_text_view);
        textView.setText(getGroup(groupPosition).toString());
        view.setTag(holder);
        //return the entire view
        return view;
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