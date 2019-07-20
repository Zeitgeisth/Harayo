package com.example.rock.harayo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by rock on 7/20/2019.
 */

public class ImagesAdapter extends RecyclerView.Adapter<ImagesAdapter.imageViewHolder>  {

    public ArrayList<String> imageList = new ArrayList<>();
    public Context context;
    public ImagesAdapter(ArrayList<String> imageList, Context context) {
        this.imageList = imageList;
        this.context = context;
    }




    public void setImageList(ArrayList<String> imageList){
        this.imageList = new ArrayList<>(imageList);

    }

    @NonNull
    @Override
    public imageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_image, parent, false);
        return new imageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull imageViewHolder holder, int position) {



        if (imageList.size()==0){
            holder.imageView.setImageResource(R.drawable.ic_image_black_24dp);
        } else {
            File imgFile = new  File(imageList.get(position));
            if (imgFile.exists()) {
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                holder.imageView.setImageBitmap(myBitmap);

            }
        }
    }

    @Override
    public int getItemCount() {

        return imageList.size()==0?5: this.imageList.size();
    }



    public class imageViewHolder extends  RecyclerView.ViewHolder{
        ImageView imageView;

        public imageViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.singleImage);
        }
    }

}
