package com.example.playotesttask.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.playotesttask.R;
import com.example.playotesttask.remote.modelclasses.Hit;

import java.util.ArrayList;
import java.util.List;

public class NewsItemsAdapter extends RecyclerView.Adapter<NewsItemsAdapter.NewsItemsViewHolder> {

    private Context context;
    private List<Hit> hitResponseList = new ArrayList<>();

    public NewsItemsAdapter(Context context){
        this.context = context;
    }

    public void setItems(List<Hit> hitResponseList){
        this.hitResponseList=hitResponseList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NewsItemsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NewsItemsViewHolder(LayoutInflater.from(context).inflate(R.layout.news_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull NewsItemsViewHolder holder, int position) {
        holder.setData(hitResponseList.get(position));
    }


    @Override
    public int getItemCount() {
        return hitResponseList.size();
    }

    class NewsItemsViewHolder extends RecyclerView.ViewHolder{

        TextView tv_title,tv_date,tv_comment,tv_points, tv_author;

        NewsItemsViewHolder(View itemView) {
            super(itemView);
            tv_title=itemView.findViewById(R.id.tv_title);
            tv_date = itemView.findViewById(R.id.tv_date);
            tv_comment= itemView.findViewById(R.id.tv_comments);
            tv_points= itemView.findViewById(R.id.tv_points);
            tv_author= itemView.findViewById(R.id.tv_author);
        }

        void setData(Hit data){
            tv_title.setText(data.getTitle());
            tv_date.setText(data.getCreatedAt());
            tv_comment.setText(data.getNumComments().toString());
            tv_points.setText(data.getPoints().toString());
            tv_author.setText(data.getAuthor());
        }
    }
}
