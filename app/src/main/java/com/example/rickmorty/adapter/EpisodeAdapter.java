package com.example.rickmorty.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rickmorty.R;
import com.example.rickmorty.fragments.EpisodeFragment;
import com.example.rickmorty.models.Character;
import com.example.rickmorty.models.Episode;

import java.util.ArrayList;

public class EpisodeAdapter extends RecyclerView.Adapter<EpisodeAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Episode> episodes;

    public EpisodeAdapter(Context context, ArrayList<Episode> episodes) {
        this.context = context;
        this.episodes = episodes;
    }

    public EpisodeAdapter(Context context) {
        this.context = context;
        this.episodes = new ArrayList<>();
    }

    //Carga el layout
    @NonNull
    @Override
    public EpisodeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_episode, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EpisodeAdapter.ViewHolder holder, int position) {

        Episode episode = episodes.get(position);


        holder.tvName.setText(episode.getName());
        holder.tvDate.setText(episode.getDate());
        holder.tvEpisode.setText(episode.getEpisode());

    }

    @Override
    public int getItemCount() {
        return episodes != null ? episodes.size() : 0;
    }

    public  void addEpisodes(ArrayList<Episode> episodes){
        this.episodes.addAll(episodes);
        notifyDataSetChanged();
    }


    // Es clase se encarga de añadir los elementos del layout
    public class ViewHolder extends RecyclerView.ViewHolder{

        private final TextView tvName;
        private final TextView tvDate;
        private final TextView tvEpisode;

        public ViewHolder(@NonNull View view) {
            super(view);

            tvName = view.findViewById(R.id.tv_name);
            tvDate = view.findViewById(R.id.tv_date);
            tvEpisode = view.findViewById(R.id.tv_episode);

        }

    }

}
