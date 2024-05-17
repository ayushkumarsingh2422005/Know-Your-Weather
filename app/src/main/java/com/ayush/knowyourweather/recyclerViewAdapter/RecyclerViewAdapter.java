package com.ayush.knowyourweather.recyclerViewAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ayush.knowyourweather.R;
import com.ayush.knowyourweather.WeatherData.WeatherData;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<WeatherData> list;
    public RecyclerViewAdapter(Context context, List<WeatherData> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hourly_data, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        holder.rec
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView rec_time, rec_humidity, rec_temp, rec_temp_desc;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            rec_time = itemView.findViewById(R.id.rec_time);
            rec_temp = itemView.findViewById(R.id.rec_temp);
            rec_humidity = itemView.findViewById(R.id.rec_humidity);
            rec_temp_desc = itemView.findViewById(R.id.rec_temp_desc);
        }

        @Override
        public void onClick(View view) {

        }
    }
}
