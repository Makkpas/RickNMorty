package com.example.rickmorty.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.rickmorty.CharacterActivity;
import com.example.rickmorty.R;

import java.util.ArrayList;

import com.example.rickmorty.models.Character;



public class CharacterAdapter extends RecyclerView.Adapter<CharacterAdapter.ViewHolder> implements ItemClickListener {

    private Context context;
    private ArrayList<Character> characters;

    public CharacterAdapter(Context context, ArrayList<Character> characters) {
        this.context = context;
        this.characters = characters;
    }

    public CharacterAdapter(Context context) {
        this.context = context;
        this.characters = new ArrayList<>();
    }


    // Carga el layout
    @NonNull
    @Override
    public CharacterAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_character, parent, false);

        return new ViewHolder(view, this);
    }


    //Habilita los elementos
    @Override
    public void onBindViewHolder(@NonNull CharacterAdapter.ViewHolder holder, int position) {

        Character character = characters.get(position);


        holder.tvName.setText(character.getName());
        holder.tvStatus.setText(character.getStatus());
        holder.tvSpecies.setText(character.getSpecies());
        holder.tvLocation.setText(character.getLocation().getName());

        Glide.with(context)
                .load(character.getImage())
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.ivCharacter);
    }

    @Override
    public int getItemCount() {
        return characters != null ? characters.size() : 0;
    }

    public  void addCharacters(ArrayList<Character> characters){
        this.characters.addAll(characters);
        notifyDataSetChanged();
    }

    // Es clase se encarga de añadir los elementos del layout
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private  CardView cvCardCharacter;

        private final ImageView ivCharacter;

        private final TextView tvName;
        private final TextView tvStatus;
        private final TextView tvSpecies;
        private final TextView tvLocation;

        private ItemClickListener listener;

        public ViewHolder(@NonNull View view, ItemClickListener listener) {
            super(view);

            this.listener = listener;
            cvCardCharacter = view.findViewById(R.id.cv_card_character);

            ivCharacter = view.findViewById(R.id.iv_character);

            tvName = view.findViewById(R.id.tv_name);
            tvStatus = view.findViewById(R.id.tv_status);
            tvSpecies = view.findViewById(R.id.tv_species);
            tvLocation = view.findViewById(R.id.tv_location);

            cvCardCharacter.setOnClickListener(this);


        }

        @Override
        public void onClick(View view) {
            listener.onClick(view, getLayoutPosition());
        }
    }

    @Override
    public void onClick(View view, int position) {
        //TODO: crear el activity que tiene la info

        Intent intent = new Intent(context, CharacterActivity.class);

        Character character = characters.get(position);
        intent.putExtra(context.getString(R.string.character_id), character.getId());
        intent.putExtra(context.getString(R.string.character_name), character.getName());

        context.startActivity(intent);
    }
}

interface ItemClickListener{
    void  onClick(View view, int position);
}