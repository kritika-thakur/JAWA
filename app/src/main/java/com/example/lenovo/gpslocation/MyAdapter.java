package com.example.lenovo.gpslocation;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.squareup.picasso.Picasso;
import java.util.List;
/**
 * Created by Lenovo on 6/4/2017.
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private List<ListItem> listItems;
    private Context context;
    public MyAdapter(List<ListItem> listItems, Context context) {
        this.listItems = listItems;
        this.context = context;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item,parent,false);
        return new ViewHolder(v);
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final ListItem listItem = listItems.get(position);
        holder.location_name.setText(listItem.getPlace_name());
        holder.location_address.setText(listItem.getPlace_add());
        holder.ratingBar.setText(listItem.getRating());
        Picasso.with(context).load(listItem.getPlace_icon()).into(holder.imageView);
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,"You Clicked" + " " +listItem.getPlace_name(),Toast.LENGTH_LONG).show();
            }
        });
    }
    @Override
    public int getItemCount() {
        return listItems.size();
    }
        public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView location_name;
        public TextView location_address;
        public ImageView imageView;
        public TextView ratingBar;
        public LinearLayout linearLayout;
        public ViewHolder(View itemView) {
        super(itemView);
        location_name = (TextView)itemView.findViewById(R.id.location_name);
        location_address = (TextView)itemView.findViewById(R.id.location_address);
        imageView = (ImageView) itemView.findViewById(R.id.imageView);
        ratingBar = (TextView) itemView.findViewById(R.id.ratingBar);
        linearLayout = (LinearLayout)itemView.findViewById(R.id.linearLayout);
        }
    }
}