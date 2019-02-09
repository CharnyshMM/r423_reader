package com.example.mikita.r423_reader;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ContentsListAdapter extends RecyclerView.Adapter<ContentsListAdapter.MyViewHolder> {
    public List<Chapter> getFeedItems() {
        return feedItems;
    }

    public void setFeedItems(List<Chapter> feedItems) {
        this.feedItems = feedItems;
        this.notifyDataSetChanged();
    }

    protected List<Chapter> feedItems;
    protected Context context;
    private OnItemClickListener onItemClickListener;


    public ContentsListAdapter(Context context, List<Chapter>feedItems, OnItemClickListener listener){
        this.feedItems=feedItems;
        this.onItemClickListener = listener;
        this.context = context;
    }

    @NonNull
    @Override
    public ContentsListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.contents_list_item, parent,false);
        MyViewHolder holder=new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ContentsListAdapter.MyViewHolder holder, int position) {
        Chapter item = feedItems.get(position);

        holder.Title.setText(item.getText());
        holder.setOnClickListener(onItemClickListener, item);
    }

    @Override
    public int getItemCount() {
        return feedItems.size();
    }

    public void addItem(Chapter item) {
        feedItems.add(item);
        this.notifyDataSetChanged();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView Title;

        public MyViewHolder(View itemView) {
            super(itemView);
            Title = (TextView) itemView.findViewById(R.id.contents_list_item__title);
        }

        public void setOnClickListener(final OnItemClickListener listener, final Chapter item) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
        }
    }

    public interface OnItemClickListener{
        void onItemClick(Chapter item);
    }
}
