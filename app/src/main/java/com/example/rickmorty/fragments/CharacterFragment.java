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

    private AppCompatActivity activity;
    private ArrayList<Character> characters;
    private final String TAG = "CharacterFragment";
    private CharacterAdapter characterAdapter;

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

        RecyclerView rvCharacters = view.findViewById(R.id.rv_characters);

        //ArrayList --> Adapter <-- RecyclerView

        characterAdapter = new CharacterAdapter(activity);
        rvCharacters.setAdapter(characterAdapter);
        rvCharacters.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity);

        rvCharacters.setLayoutManager(linearLayoutManager);

        characterAdapter.addCharacters(characters);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //ToDo: se hace la lógica

        getCharactersInfo();
    }

    private void getCharactersInfo() {
        // TODO: cargar los datos del rest api
        CharacterService characterService = RetrofitBuilder.createService(CharacterService.class);

        Call<CharacterResponse> response  =  characterService.getCharacters();

        response.enqueue(new Callback<CharacterResponse>() {
            @Override
            public void onResponse(@NonNull Call<CharacterResponse> call, @NonNull Response<CharacterResponse> response) {
                if(response.isSuccessful()){
                    CharacterResponse characterResponse = response.body();

                    ArrayList<Character> characters = characterResponse.getResults();

                    for(Character character: characters){
                        Log.i(TAG, "Character: "+ character.getName());
                    }

                    characterAdapter.addCharacters(characters);
                } else {
                    Log.i(TAG, "onError: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(@NonNull Call<CharacterResponse> call, @NonNull Throwable t) {

                throw  new  RuntimeException(t);
            }
        });
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