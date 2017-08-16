package com.example.lenovo.gpslocation;

/**
 * Created by Lenovo on 6/26/2017.
 */
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
import org.w3c.dom.Text;
import java.util.List;
/**
 * Created by Lenovo on 6/4/2017.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private List<RecyclerViewListItems> recyclerlistItems;
    private Context context;
    public RecyclerViewAdapter(List<RecyclerViewListItems> recyclerlistItems, Context context) {
        this.recyclerlistItems = recyclerlistItems;
        this.context = context;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_list_item,parent,false);
        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final RecyclerViewListItems recyclerlistItem = recyclerlistItems.get(position);
        holder.category.setText(recyclerlistItem.getHead());
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,"You Clicked" + " " +recyclerlistItem.getHead(),Toast.LENGTH_LONG).show();
            }
        });
    }
    @Override
    public int getItemCount() {
        return recyclerlistItems.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView category;
        public LinearLayout linearLayout;
        public ViewHolder(View itemView) {
            super(itemView);
            category = (TextView)itemView.findViewById(R.id.category);
            linearLayout = (LinearLayout)itemView.findViewById(R.id.linearLayout);
        }
    }
}