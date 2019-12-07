package com.example.playotesttask.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.playotesttask.R;
import com.example.playotesttask.adapters.NewsItemsAdapter;
import com.example.playotesttask.remote.modelclasses.SearchResponse;
import com.example.playotesttask.viewmodels.MainViewModel;

public class MainActivity extends AppCompatActivity {

    MainViewModel mainViewModel;
    private String tag = "MainActivity";
    private EditText searchWord;
    private ImageView search_image;
    private RecyclerView rv_news_items;
    private NewsItemsAdapter newsItemsAdapter;
    ProgressBar pb_loading, pb_bottom;
    private MutableLiveData<Integer> pageNumber = new MutableLiveData<>();
    private Boolean isScrolling =false;
    private LinearLayoutManager linearLayoutManager;
    private int currItems,totItems,scrollOutItems;
    static int page=0;
    private LinearLayout empty_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        observeOpenUrl();
        observePageNumber();
    }

    public void init() {
        mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        observeSearch();

        searchWord = findViewById(R.id.et_search_word);
        search_image = findViewById(R.id.search_image);
        rv_news_items = findViewById(R.id.rv_news_items);
        pb_loading = findViewById(R.id.pb_loading);
        pb_bottom=findViewById(R.id.pb_bottom);
        newsItemsAdapter = new NewsItemsAdapter(this);
        linearLayoutManager=new LinearLayoutManager(this);
        empty_layout=findViewById(R.id.empty_layout);
        rv_news_items.setLayoutManager(linearLayoutManager);
        rv_news_items.setAdapter(newsItemsAdapter);

        search_image.setOnClickListener(v -> {
            page=0;
            pb_loading.setVisibility(View.VISIBLE);
            mainViewModel.getSearchResults(searchWord.getText().toString().trim(),page);
        });

        rv_news_items.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                    isScrolling =true;
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                currItems=linearLayoutManager.getChildCount();
                totItems=linearLayoutManager.getItemCount();
                scrollOutItems=linearLayoutManager.findFirstVisibleItemPosition();

                if(isScrolling && (currItems+scrollOutItems==totItems))
                {
                    isScrolling=false;
                    page=page+1;
                    pageNumber.postValue(page);
                }
            }
        });
    }

    public void observeSearch() {
        mainViewModel.getSearchLivedata().observe(this, searchObserver);
    }

    public void observeOpenUrl() {
        newsItemsAdapter.getUrlLiveData().observe(this, openUrl);
    }

    public void observePageNumber(){
        pageNumber.observe(this,pageNumberObserver);
    }

    private Observer<SearchResponse> searchObserver = searchResponse -> {
        if (searchResponse != null) {
            if(searchResponse.getHits().size() == 0){
                empty_layout.setVisibility(View.VISIBLE);
            }else {
                empty_layout.setVisibility(View.GONE);
                if (page == 0) {
                    newsItemsAdapter.setItems(searchResponse.getHits());
                } else {
                    newsItemsAdapter.addItems(searchResponse.getHits());
                }
            }
        }
        else{
            empty_layout.setVisibility(View.VISIBLE);
        }
        pb_loading.setVisibility(View.GONE);
        pb_bottom.setVisibility(View.GONE);
    };

    private Observer<String> openUrl = s -> {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(s));
        startActivity(browserIntent);
    };

    private Observer<Integer> pageNumberObserver = integer -> {
        pb_bottom.setVisibility(View.VISIBLE);
        mainViewModel.getSearchResults(searchWord.getText().toString().trim(),integer);
    };
}
