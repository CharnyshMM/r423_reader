package com.example.mikita.r423_reader;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class GalleryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public List<GalleryListItem> recyclerItems;
    protected Context context;
    protected OnItemClickListener onItemClickListener;

    public GalleryAdapter(Context context,
                          List<GalleryListItem> feedItems,
                          GalleryAdapter.OnItemClickListener listener){
        this.recyclerItems=feedItems;
        this.context = context;
        this.onItemClickListener = listener;
    }

    private static class HeaderViewHolder extends RecyclerView.ViewHolder{

        TextView textView;

        public HeaderViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = (TextView)itemView.findViewById(R.id.header_txt);
        }
    }

    private static class ImageViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;

        public ImageViewHolder(@NonNull View itemView){
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

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType){
            case GalleryListItem.TYPE_HEADER: {
                View itemView = inflater.inflate(R.layout.gallery_header_item, parent, false);
                return new HeaderViewHolder(itemView);
            }
            case GalleryListItem.TYPE_IMAGE: {
                View itemView = inflater.inflate(R.layout.gallery_image, parent, false);
                return new ImageViewHolder(itemView);
            }
            default:
                throw new IllegalStateException("unsupported item type");
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        int viewType = getItemViewType(position);
        switch (viewType){
            case GalleryListItem.TYPE_HEADER: {
                GalleryHeaderItem header = (GalleryHeaderItem) recyclerItems.get(position);
                HeaderViewHolder holder = (HeaderViewHolder)viewHolder;

                holder.textView.setText(header.getTitle());
                break;
            }
            case GalleryListItem.TYPE_IMAGE: {
                GalleryImageItem imageItem = (GalleryImageItem)recyclerItems.get(position);
                ImageViewHolder holder = (ImageViewHolder)viewHolder;

                try {
                    InputStream stream = context.getAssets().open(imageItem.getImage().getImageUrl());
                    Drawable d = Drawable.createFromStream(stream, null);
                    holder.imageView.setImageDrawable(d);
                    holder.setOnClickListener(onItemClickListener, position);
                } catch (IOException e) {
                    e.printStackTrace();
                    holder.imageView.setImageResource(R.drawable.not_found);
                }
                break;
            }
            default:
                throw new IllegalStateException("unsupported item type");
        }
    }

    @Override
    public int getItemCount() {
        return recyclerItems.size();
    }

    @Override
    public int getItemViewType(int position){
        return recyclerItems.get(position).getType();
    }

    public interface OnItemClickListener{
        void onItemClick(int position);
    }
}
