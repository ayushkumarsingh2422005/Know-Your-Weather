package com.ayush.knowyourweather.recyclerViewAdapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ayush.knowyourweather.R;
import com.ayush.knowyourweather.WeatherData.WeatherData;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private Context context;
    private List<WeatherData> list;
    public RecyclerViewAdapter(Context context, List<WeatherData> list) {
        this.context = context;
        this.list = list;
    }
    public void setDataChange( List<WeatherData> list) {
        list = new ArrayList<>();
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hourly_data, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override

    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.time.setText(list.get(position).getTime().split(" ")[1]);
        holder.temp.setText(list.get(position).getTemp()+"°");
        holder.minMax.setText(list.get(position).getTemp_min() + "°/" + list.get(position).getTemp_max()+"°");
        holder.pptPercent.setText(list.get(position).getPricipatation()+"%");
        String url = list.get(position).getImage();
        Picasso.get().load(url).resize(80,80).centerCrop().into(holder.icon);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView time,temp,minMax,pptPercent;
        public ImageView icon;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            time = itemView.findViewById(R.id.rec_time);
            temp = itemView.findViewById(R.id.rec_temp);
            minMax = itemView.findViewById(R.id.rec_temp_desc);
            pptPercent = itemView.findViewById(R.id.rec_humidity);
            icon = itemView.findViewById(R.id.imageView);
        }

        @Override
        public void onClick(View view) {

        }
    }
}
