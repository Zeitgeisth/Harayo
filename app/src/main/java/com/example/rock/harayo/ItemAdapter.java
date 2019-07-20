package com.example.rock.harayo;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by User on 7/21/2019.
 */

public class ItemAdapter extends  RecyclerView.Adapter<ItemAdapter.itemViewHolder>{
    public ArrayList<Item> itemList = new ArrayList<>();

    public ItemAdapter(ArrayList<Item> itemList, Context context) {
        this.itemList = itemList;
        this.context = context;
    }

    public Context context;

    @NonNull
    @Override
    public itemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_item_card, parent, false);
        return new itemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull itemViewHolder holder, int position) {
        Item current = itemList.get(position);
        holder.name.setText(current.name);
        holder.status.setText(current.status);
        holder.call.setText(current.call);

        Glide.with(context).load(Constants.BASE_URL + current.imageUrl).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class itemViewHolder extends  RecyclerView.ViewHolder{
        ImageView imageView;
        TextView name, status, call;

        public itemViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.single_image);
            name = itemView.findViewById(R.id.single_name);
            status = itemView.findViewById(R.id.single_status);
            call = itemView.findViewById(R.id.single_call);

        }
    }
}
