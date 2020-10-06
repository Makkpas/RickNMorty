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
import android.widget.ProgressBar;

import com.example.rickmorty.R;
import com.example.rickmorty.adapter.EpisodeAdapter;
import com.example.rickmorty.api.EpisodeService;
import com.example.rickmorty.api.RetrofitBuilder;
import com.example.rickmorty.models.Episode;
import com.example.rickmorty.models.EpisodeResponse;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EpisodeFragment extends Fragment {


    private final String TAG = "EpisodeFragment";
    private AppCompatActivity activity;

    boolean canLoad = true;
    int limit = 0;
    int page = 1;
    private ProgressBar pbLoading;
    private RecyclerView rvEpisodes;
    private LinearLayoutManager linearLayoutManager;
    private EpisodeAdapter episodeAdapter;
    private ArrayList<Episode> episodes;

    public EpisodeFragment() {
        // Required empty public constructor
    }

    public static EpisodeFragment newInstance() {
        EpisodeFragment fragment = new EpisodeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        episodes = new ArrayList<>();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_episode, container, false);

        pbLoading = view.findViewById(R.id.pb_loading);

        rvEpisodes = view.findViewById(R.id.rv_episodes);

        episodeAdapter = new EpisodeAdapter(activity);
        rvEpisodes.setAdapter(episodeAdapter);
        rvEpisodes.setHasFixedSize(true);

        linearLayoutManager = new LinearLayoutManager(activity);

        rvEpisodes.setLayoutManager(linearLayoutManager);

        episodeAdapter.addEpisodes(episodes);

        setupRVScrollListener(rvEpisodes);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getEpisodesInfo(page);
    }

    public void setupRVScrollListener(RecyclerView recyclerView){
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
                            getEpisodesInfo(page);

                        }
                    }
                }
            }
        });
    }

    private void getEpisodesInfo(int page) {

        canLoad = false;

        EpisodeService episodeService = RetrofitBuilder.createService(EpisodeService.class);

        Call<EpisodeResponse> response = episodeService.getEpisodes(page);

        response.enqueue(new Callback<EpisodeResponse>() {
            @Override
            public void onResponse(Call<EpisodeResponse> call, Response<EpisodeResponse> response) {
                if (response.isSuccessful()){
                    EpisodeResponse episodeResponse = response.body();

                    ArrayList<Episode> episodes = episodeResponse.getResults();
                    episodeAdapter.addEpisodes(episodes);
                    showEpisode(true);
                }else {
                    Log.i(TAG, "onError: " + response.errorBody());
                }
                canLoad = true;

            }

            @Override
            public void onFailure(Call<EpisodeResponse> call, Throwable t) {
                canLoad = true;
                throw  new  RuntimeException(t);
            }
        });


    }

    private void showEpisode(boolean setVisible) {
        rvEpisodes.setVisibility(setVisible ? View.VISIBLE : View.GONE);
        pbLoading.setVisibility(setVisible ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}