package com.example.mikita.r423_reader;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.MyViewHolder> {

    public List<GalleryImage> recyclerItems;
    protected Context context;
    protected OnItemClickListener onItemClickListener;

    public GalleryAdapter(Context context, List<GalleryImage> feedItems, GalleryAdapter.OnItemClickListener listener){
        this.recyclerItems=feedItems;
        this.onItemClickListener = listener;
        this.context = context;
        this.onItemClickListener = listener;
    }

    @NonNull
    @Override
    public GalleryAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.gallery_image, parent,false);
        MyViewHolder holder=new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull GalleryAdapter.MyViewHolder holder, int position) {
        GalleryImage item = recyclerItems.get(position);
        try {
            InputStream stream = context.getAssets().open(item.getImageUrl());
            Drawable d = Drawable.createFromStream(stream, null);
            holder.imageView.setImageDrawable(d);
            holder.setOnClickListener(onItemClickListener, position);
        } catch (IOException e) {
            e.printStackTrace();
            holder.imageView.setImageResource(R.drawable.not_found);
        }
    }

    @Override
    public int getItemCount() {
        return recyclerItems.size();
    }

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;

        public MyViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.gallery__images__item);
        }

        public void setOnClickListener(final OnItemClickListener listener, final int position) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(position);
                }
            });
        }
    }
}
