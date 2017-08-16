package com.example.lenovo.gpslocation;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v7.app.AlertDialog;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by ideafoundation on 30/06/17.
 */

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> {
    private ArrayList<RecyclerViewListItems> android;
    private Context context;
    public DatabaseHandler db;

    public DataAdapter(Context context,ArrayList<RecyclerViewListItems> android) {
        this.android = android;
        this.context = context;
    }

    @Override
    public DataAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.needs_layout, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DataAdapter.ViewHolder viewHolder, int i) {
        final RecyclerViewListItems recycle_model = android.get(i);
        viewHolder.tv_android.setText(android.get(i).getAndroid_version_name());
        viewHolder.img_android.setBackgroundColor(android.get(i).getMarker_Color());
        //Picasso.with(context).load(android.get(i).getAndroid_image_url()).resize(240, 120).into(viewHolder.img_android);
        viewHolder.img_delete.setOnClickListener(new View.OnClickListener() {
            public void onClick(final View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(v.getContext());
                alertDialog.setTitle("Delete Item");
                alertDialog.setMessage("Are you sure you want delete this?");
                alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int which) {
                        db = new DatabaseHandler(v.getContext(), null, null, 20);
                        db.delete_need(recycle_model.getID());
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
    }

    @Override
    public int getItemCount() {
        return android.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView tv_android;
        private ImageView img_android;
        private ImageView img_delete;
        public ViewHolder(View view) {
            super(view);
            tv_android = (TextView)view.findViewById(R.id.tv_android);
            img_android = (ImageView) view.findViewById(R.id.img_android);
            img_delete = (ImageView) view.findViewById(R.id.img_delete);
        }
    }

}