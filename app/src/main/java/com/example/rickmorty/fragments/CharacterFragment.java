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
import com.example.rickmorty.adapter.CharacterAdapter;
import com.example.rickmorty.api.CharacterService;
import com.example.rickmorty.api.RetrofitBuilder;
import com.example.rickmorty.models.Character;
import com.example.rickmorty.models.CharacterResponse;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CharacterFragment extends Fragment {

    private final String TAG = "CharacterFragment";
    private AppCompatActivity activity;
    private ArrayList<Character> characters;
    private CharacterAdapter characterAdapter;

    boolean canLoad = true;
    int limit = 0;
    int page = 1;
    private LinearLayoutManager linearLayoutManager;
    private ProgressBar pbLoading;
    private RecyclerView rvCharacters;

    public CharacterFragment() {
        // Required empty public constructor
    }

    public static CharacterFragment newInstance() {
        CharacterFragment fragment = new CharacterFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //ToDo: inicializar variables que no depende de la vista(layout)

        characters = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //ToDo: Inicializar viariables que no depende de la vista. Todos los componentes visuales (textView,Btn, EditsText)

        View view = inflater.inflate(R.layout.fragment_character, container, false);

        pbLoading = view.findViewById(R.id.pb_loading);

        rvCharacters = view.findViewById(R.id.rv_characters);

        //TODO: Agregar listener al RV

        //ArrayList --> Adapter <-- RecyclerView

        characterAdapter = new CharacterAdapter(activity);
        rvCharacters.setAdapter(characterAdapter);
        rvCharacters.setHasFixedSize(true);

        linearLayoutManager = new LinearLayoutManager(activity);

        rvCharacters.setLayoutManager(linearLayoutManager);

        characterAdapter.addCharacters(characters);

        setupRVScrollListener(rvCharacters);

        return view;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //ToDo: se hace la lógica

        getCharactersInfo(page);
    }


    public void setupRVScrollListener(RecyclerView recyclerView){
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                //dy = + -> personaje 0 a 20
                //dy = - -> personaje 20 a 0
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
                            getCharactersInfo(page);

                        }
                    }
                }
            }
        });
    }

    private void getCharactersInfo(int page) {

        canLoad = false;

        CharacterService characterService = RetrofitBuilder.createService(CharacterService.class);

        Call<CharacterResponse> response  =  characterService.getCharacters(page);

        response.enqueue(new Callback<CharacterResponse>() {
            @Override
            public void onResponse(@NonNull Call<CharacterResponse> call, @NonNull Response<CharacterResponse> response) {
                if(response.isSuccessful()){
                    CharacterResponse characterResponse = response.body();

                    ArrayList<Character> characters = characterResponse.getResults();
                    characterAdapter.addCharacters(characters);

                    showCharacter(true);
                } else {
                    Log.i(TAG, "onError: " + response.errorBody());
                }
                canLoad = true;
            }

            @Override
            public void onFailure(@NonNull Call<CharacterResponse> call, @NonNull Throwable t) {
                canLoad = true;
                throw  new  RuntimeException(t);
            }
        });
    }

    public void showCharacter(boolean setVisible){
        rvCharacters.setVisibility(setVisible ? View.VISIBLE : View.GONE);
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