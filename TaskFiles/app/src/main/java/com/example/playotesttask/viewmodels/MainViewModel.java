package com.example.playotesttask.viewmodels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.playotesttask.managers.NetworkManager;
import com.example.playotesttask.remote.modelclasses.SearchResponse;
public class MainViewModel extends AndroidViewModel {

    private MutableLiveData<SearchResponse> searchResponseLiveData = new MutableLiveData<>();
    private NetworkManager networkManager;
    private String tag = "MainVM";

    public MainViewModel(@NonNull Application application) {
        super(application);
        networkManager = new NetworkManager();
    }

    public MutableLiveData<SearchResponse> getSearchLivedata(){
        return searchResponseLiveData;
    }

    public void getSearchResults(String keyword, Integer page){
        networkManager.getSearchResultResponse(keyword,page,searchResponseLiveData);
    }
}
