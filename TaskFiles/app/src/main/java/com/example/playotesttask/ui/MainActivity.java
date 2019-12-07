package com.example.playotesttask.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.playotesttask.R;
import com.example.playotesttask.remote.modelclasses.SearchResponse;
import com.example.playotesttask.viewmodels.MainViewModel;

import javax.annotation.Nullable;

public class MainActivity extends AppCompatActivity {

    MainViewModel mainViewModel;
    private String tag="MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    public void init(){
        mainViewModel= ViewModelProviders.of(this).get(MainViewModel.class);
        mainViewModel.getSearchResults("sport").observe(this, new Observer<SearchResponse>() {
            @Override
            public void onChanged(SearchResponse searchResponse) {
                Log.v(tag,"calling search");
                Toast.makeText(MainActivity.this,searchResponse.getParams(),Toast.LENGTH_LONG).show();
            }
        });
    }
}
