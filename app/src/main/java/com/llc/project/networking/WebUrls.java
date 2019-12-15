package com.llc.project.networking;

import com.llc.project.model.RetrofitModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by User on 13-12-2019.
 */

public interface WebUrls {

    @GET("s/2iodh4vg0eortkl/facts.json")
    Call<RetrofitModel> getCountryData();
}
