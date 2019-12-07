package com.example.playotesttask.managers;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.playotesttask.remote.modelclasses.SearchResponse;

import retrofit2.Response;
import rx.Observer;
import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class NetworkManager {

    private RetrofitManager retrofitManager;
    private String TAG="NetworkManager";


    public NetworkManager(){
        retrofitManager=new RetrofitManager();
    }

    public MutableLiveData<SearchResponse> getSearchResultResponse(String keyword, final MutableLiveData<SearchResponse> searchResponseMutableLiveData){
        retrofitManager.getSearchResults().getSearchResultsQuery(keyword)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<SearchResponse>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        searchResponseMutableLiveData.postValue(null);
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(Response<SearchResponse> searchResponseResponse) {
                        if(searchResponseResponse.code()==200){
                            searchResponseMutableLiveData.postValue(searchResponseResponse.body());
                        }
                        else{
                            searchResponseMutableLiveData.postValue(null);
                        }
                    }
                });
        return searchResponseMutableLiveData;
    }

}
