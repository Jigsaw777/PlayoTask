package com.example.playotesttask.adapters;

import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.playotesttask.remote.modelclasses.Hit;
import com.example.playotesttask.remote.modelclasses.SearchResponse;

import java.util.List;

public class NewsItemsAdapter extends RecyclerView.Adapter {

    private Context context;
    private List<Hit> hitResponseList;

    public NewsItemsAdapter(Context context){
        this.context = context;
    }

    public void setItems(List<Hit> hitResponseList){
        this.hitResponseList=hitResponseList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class NewsItemsViewHolder extends RecyclerView.ViewHolder{

    }
}
