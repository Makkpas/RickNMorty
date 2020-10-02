package com.example.rickmorty;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.rickmorty.api.CharacterService;
import com.example.rickmorty.api.RetrofitBuilder;
import com.example.rickmorty.models.Character;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CharacterActivity extends AppCompatActivity {

    private String TAG = "Character";
    private ImageView ivCover;
    private TextView tvName;
    private TextView tvStatus;
    private TextView tvSpecies;
    private TextView tvLocation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character);

        Toolbar tToolbar = findViewById(R.id.t_toolbar);

        ivCover = findViewById(R.id.iv_cover);


        tvStatus = findViewById(R.id.tv_status);
        tvSpecies = findViewById(R.id.tv_species);
        tvLocation = findViewById(R.id.tv_location);

        Intent intent = getIntent();

        if (intent != null){
            int characterId = intent.getIntExtra(getString(R.string.character_id), 0);
            String  characterName = intent.getStringExtra(getString(R.string.character_name));

            Log.i(TAG, "onCreate: Name " + characterName);

            if(characterId > 0){

                tToolbar.setTitle(characterName);
                setSupportActionBar(tToolbar);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setDisplayShowHomeEnabled(true);
                //TODO: service para optener la info del personaje
                //TODO: Layout para mostrar la info

                gerCharacterInfo(characterId);
            }
        }
    }

    private void gerCharacterInfo(int characterId) {

        Glide.with(this)
                .load("https://rickandmortyapi.com/api/character/avatar/"+characterId+".jpeg")
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(ivCover);

        CharacterService characterService = RetrofitBuilder.createService(CharacterService.class);

        Call<Character> characterCall =  characterService.getCharacter(characterId);

        characterCall.enqueue(new Callback<Character>() {
            @Override
            public void onResponse(Call<Character> call, Response<Character> response) {
                if(response.isSuccessful()){
                    //TODO: cargar info en el layout

                    Character character = response.body();

                    if(character != null){
                        tvStatus.setText(character.getStatus());
                        tvSpecies.setText(character.getName());
                        tvLocation.setText(character.getLocation().getName());
                    }


                }else {
                    Log.i(TAG, response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(Call<Character> call, Throwable t) {
                throw  new RuntimeException(t);
            }
        });
    }
}