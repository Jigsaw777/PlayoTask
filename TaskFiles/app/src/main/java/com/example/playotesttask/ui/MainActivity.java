package com.example.playotesttask.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
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
    ProgressBar pb_loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        observeOpenUrl();
    }

    public void init() {
        mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        observeSearch();

        searchWord = findViewById(R.id.et_search_word);
        search_image = findViewById(R.id.search_image);
        rv_news_items = findViewById(R.id.rv_news_items);
        pb_loading = findViewById(R.id.pb_loading);
        newsItemsAdapter = new NewsItemsAdapter(this);
        rv_news_items.setLayoutManager(new LinearLayoutManager(this));
        rv_news_items.setAdapter(newsItemsAdapter);

        search_image.setOnClickListener(v -> {
            pb_loading.setVisibility(View.VISIBLE);
            mainViewModel.getSearchResults(searchWord.getText().toString().trim());
        });
    }

    public void observeSearch() {
        mainViewModel.getSearchLivedata().observe(this, searchObserver);
    }

    public void observeOpenUrl() {
        newsItemsAdapter.getUrlLiveData().observe(this, openUrl);
    }

    private Observer<SearchResponse> searchObserver = searchResponse -> {
        if (searchResponse != null) {
            newsItemsAdapter.setItems(searchResponse.getHits());
            pb_loading.setVisibility(View.GONE);
        }
    };

    private Observer<String> openUrl = s -> {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(s));
        startActivity(browserIntent);
    };
}
