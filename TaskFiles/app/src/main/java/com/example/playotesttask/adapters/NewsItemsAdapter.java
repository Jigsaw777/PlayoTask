package com.example.playotesttask.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.example.playotesttask.R;
import com.example.playotesttask.remote.modelclasses.Hit;
import com.google.android.flexbox.FlexboxLayout;

import java.util.ArrayList;
import java.util.List;

public class NewsItemsAdapter extends RecyclerView.Adapter<NewsItemsAdapter.NewsItemsViewHolder> {

    private Context context;
    private List<Hit> hitResponseList = new ArrayList<>();
    private MutableLiveData<String> urlLiveData=new MutableLiveData<>();

    public NewsItemsAdapter(Context context){
        this.context = context;
    }

    public void setItems(List<Hit> hitResponseList){
        this.hitResponseList=hitResponseList;
        notifyDataSetChanged();
    }

    public MutableLiveData<String> getUrlLiveData(){
        return urlLiveData;
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
        FlexboxLayout tagLayout;

        NewsItemsViewHolder(View itemView) {
            super(itemView);
            tv_title=itemView.findViewById(R.id.tv_title);
            tv_date = itemView.findViewById(R.id.tv_date);
            tv_comment= itemView.findViewById(R.id.tv_comments);
            tv_points= itemView.findViewById(R.id.tv_points);
            tv_author= itemView.findViewById(R.id.tv_author);
            tagLayout=itemView.findViewById(R.id.tags_layout);
        }

        void setData(Hit data){
            tv_title.setText(data.getTitle());
            tv_date.setText(data.getCreatedAt());

            if(data.getNumComments()!=null){
            tv_comment.setText(data.getNumComments().toString());
            tv_comment.setVisibility(View.VISIBLE);}
            else{
                tv_comment.setVisibility(View.GONE);
            }

            if(data.getPoints()!=null){
            tv_points.setText(data.getPoints().toString());
            tv_points.setVisibility(View.VISIBLE);}
            else{
                tv_points.setVisibility(View.GONE);
            }

            tv_author.setText(data.getAuthor());
            itemView.setOnClickListener(v -> urlLiveData.postValue(data.getUrl()));
            displayTags(data.getTags());
        }

        void displayTags(List<String> tags){
            FlexboxLayout.LayoutParams params = new FlexboxLayout.LayoutParams(
                    FlexboxLayout.LayoutParams.WRAP_CONTENT, FlexboxLayout.LayoutParams.WRAP_CONTENT);
            for(int  i=0;i<tags.size();i++){
                TextView tv=new TextView(context);
                tv.setPadding(6,2,6,2);
                params.setMargins(6,2,6,2);
                tv.setBackgroundColor(Color.parseColor("#add8e6"));
                tv.setTextColor(Color.parseColor("#0000ff"));
                tv.setLayoutParams(params);
                tv.setText(tags.get(i));
                tagLayout.addView(tv);
            }
        }
    }
}
