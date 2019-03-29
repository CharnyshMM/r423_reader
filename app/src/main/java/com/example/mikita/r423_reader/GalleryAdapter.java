package com.example.mikita.r423_reader;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;

import java.net.URI;
import java.util.List;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.MyViewHolder> {

    public List<GalleryImage> recyclerItems;
    protected Context context;
    protected OnItemClickListener onItemClickListener;

    public GalleryAdapter(Context context, List<GalleryImage> feedItems, GalleryAdapter.OnItemClickListener listener){
        this.recyclerItems=feedItems;
        this.onItemClickListener = listener;
        this.context = context;
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

        holder.imageView.setImageDrawable(item.getDrawableImage());
//        Glide
//                .with(context)
//                .load(item.getImageUrl())
//                .thumbnail(0.5f)
//                .crossFade()
//                .into(holder.imageView);
        //holder.setOnClickListener(onItemClickListener, item);

    }

    @Override
    public int getItemCount() {
        return recyclerItems.size();
    }

    public interface OnItemClickListener{
        void onItemClick(GalleryImage item);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;

        public MyViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.gallery__images__item);

        }

        public void setOnClickListener(final OnItemClickListener listener, final GalleryImage item) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
        }
    }
}
