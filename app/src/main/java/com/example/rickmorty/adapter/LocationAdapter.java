package com.example.rickmorty.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rickmorty.R;
import com.example.rickmorty.models.Location;

import java.util.ArrayList;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Location> locations;

    public LocationAdapter(Context context, ArrayList<Location> locations) {
        this.context = context;
        this.locations = locations;
    }

    public LocationAdapter(Context context) {
        this.context = context;
        this.locations = new ArrayList<>();
    }

    //Carga el layout
    @NonNull
    @Override
    public LocationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_location, parent, false);

        return new ViewHolder(view);
    }


    //Habilita los elementos
    @Override
    public void onBindViewHolder(@NonNull LocationAdapter.ViewHolder holder, int position) {

        Location location = locations.get(position);

        holder.tvName.setText(location.getName());
        holder.tvType.setText(location.getType());
        holder.tvDimension.setText(location.getDimension());
    }

    @Override
    public int getItemCount() {
        return locations != null ? locations.size() : 0;
    }


    public void addLocations(ArrayList<Location> locations){
        this.locations.addAll(locations);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvName;
        private final TextView tvType;
        private final TextView tvDimension;

        public ViewHolder(@NonNull View view) {
            super(view);

            tvName = view.findViewById(R.id.tv_name);
            tvType = view.findViewById(R.id.tv_type);
            tvDimension = view.findViewById(R.id.tv_dimension);

        }

    }
}
