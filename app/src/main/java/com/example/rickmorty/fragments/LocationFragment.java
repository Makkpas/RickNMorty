package com.example.rickmorty.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.example.rickmorty.R;
import com.example.rickmorty.adapter.LocationAdapter;
import com.example.rickmorty.api.LocationService;
import com.example.rickmorty.api.RetrofitBuilder;
import com.example.rickmorty.models.Location;
import com.example.rickmorty.models.LocationResponse;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LocationFragment extends Fragment {

    private final String TAG = "LocationFragment";
    private AppCompatActivity activity;
    private ArrayList<Location> locations;

    boolean canLoad = true;
    int page = 1;
    private RecyclerView rvLocations;
    private LinearLayoutManager linearLayoutManager;
    private LocationAdapter locationAdapter;
    private ProgressBar pbLoading;


    public LocationFragment() {
        // Required empty public constructor
    }

    public static LocationFragment newInstance() {
        LocationFragment fragment = new LocationFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        locations = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_location, container, false);

        pbLoading = view.findViewById(R.id.pb_loading);

        rvLocations = view.findViewById(R.id.rv_locations);

        //ArrayList --> Adapter <-- RecyclerView

        locationAdapter = new LocationAdapter(activity);
        rvLocations.setAdapter(locationAdapter);
        rvLocations.setHasFixedSize(true);

        linearLayoutManager = new LinearLayoutManager(activity);
        rvLocations.setLayoutManager(linearLayoutManager);

        locationAdapter.addLocations(locations);
        
        setupRVScrollListener(rvLocations);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getLocationsInfo(page);
    }


    private void setupRVScrollListener(RecyclerView recyclerView) {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (dy > 0){

                    //Total de items
                    int totalItems = linearLayoutManager.getItemCount();

                    //Items que ya se mostraron
                    int pastItem = linearLayoutManager.findFirstVisibleItemPosition();

                    //Items que se estan monstrando
                    int visibleItem = linearLayoutManager.getChildCount();

                    if (canLoad){
                        if ((pastItem + visibleItem) >= totalItems){
                            page++;
                            pbLoading.setVisibility(View.VISIBLE);
                            getLocationsInfo(page);

                        }
                    }
                }
            }
        });
    }


    private void getLocationsInfo(int page) {

        canLoad = false;

        LocationService locationService = RetrofitBuilder.createService(LocationService.class);

        Call<LocationResponse>  response = locationService.getLocations(page);

        response.enqueue(new Callback<LocationResponse>() {
            @Override
            public void onResponse(Call<LocationResponse> call, Response<LocationResponse> response) {
                if(response.isSuccessful()){
                    LocationResponse locationResponse = response.body();

                    ArrayList<Location> locations = locationResponse.getResults();
                    locationAdapter.addLocations(locations);

                    showLocation(true);
                }else {
                    Log.i(TAG, "onResponse: "+ response.errorBody());
                }
                canLoad = true;
            }

            @Override
            public void onFailure(Call<LocationResponse> call, Throwable t) {
                canLoad = true;
                throw  new  RuntimeException(t);
            }
        });
    }

    private void showLocation(boolean setVisible) {
        rvLocations.setVisibility(setVisible ? View.VISIBLE : View.GONE);
        pbLoading.setVisibility(setVisible ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (AppCompatActivity) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        activity = null;
    }
}