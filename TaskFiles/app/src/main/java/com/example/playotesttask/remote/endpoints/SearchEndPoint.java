package com.example.playotesttask.remote.endpoints;


import com.example.playotesttask.remote.modelclasses.SearchResponse;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

public interface SearchEndPoint {

    @GET("search")
    Observable<Response<SearchResponse>> getSearchResultsQuery(@Query("query")String input,@Query("page")Integer number);
}
