package com.example.rickmorty.api;

import com.example.rickmorty.models.Location;
import com.example.rickmorty.models.LocationResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface LocationService {

    @Headers("Content-Type: application/json")
    @GET("location")
    //TODO: numero de pagina
    Call<LocationResponse> getLocations(@Query("page")int page);


    @Headers("Content-Type: application/json")
    @GET("location/{id}")
    Call<Location> getLocation(@Path("id") int id);
}
