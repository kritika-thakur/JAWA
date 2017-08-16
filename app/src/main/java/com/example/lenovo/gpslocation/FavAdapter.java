package com.example.lenovo.gpslocation;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
/**
 * Created by Administrator on 6/26/2017.
 */
public class FavAdapter extends RecyclerView.Adapter<FavAdapter.MyViewHolder> {
    private List<Fav> moviesList;
    public DatabaseHandler db;
    private Context context;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, genre;
        RatingBar year;
        ImageView btn_del;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            genre = (TextView) view.findViewById(R.id.genre);
            year = (RatingBar)view.findViewById(R.id.year);
            btn_del = (ImageView)view.findViewById(R.id.btn_del);

            LayerDrawable stars = (LayerDrawable) year.getProgressDrawable();
            stars.getDrawable(2).setColorFilter(Color.parseColor("#128c7e"), PorterDuff.Mode.SRC_ATOP);
        }
    }
    public FavAdapter(List<Fav> moviesList)  {
        this.moviesList = moviesList;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fav_list_row, parent, false);
        return new MyViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final Fav fav_model = moviesList.get(position);
        holder.title.setText(fav_model.getTitle());
        holder.genre.setText(fav_model.getGenre());
        if(fav_model.getYear()!=null)
        holder.year.setRating(Float.parseFloat(fav_model.getYear()));
        holder.btn_del.setOnClickListener(new View.OnClickListener() {
            public void onClick(final View v) {

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(v.getContext());

                alertDialog.setTitle("Delete Item");
                alertDialog.setMessage("Are you sure you want delete this?");
                alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int which) {
                        db = new DatabaseHandler(v.getContext(), null, null, 20);
                        db.updatefav(0,fav_model.getId());
//                notifyItemRemoved(fav_model.getId());
                        ((Activity)v.getContext()).finish();
                        v.getContext().startActivity(((Activity) v.getContext()).getIntent());
                        Toast.makeText(v.getContext(), "You clicked on YES", Toast.LENGTH_SHORT).show();
                    }
                });
                alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(v.getContext(), "You clicked on NO", Toast.LENGTH_SHORT).show();
                        dialog.cancel();
                    }
                });

                // Showing Alert Message
                alertDialog.show();
            }
        });
    }
    @Override
    public int getItemCount() {
        return moviesList.size();
    }
}