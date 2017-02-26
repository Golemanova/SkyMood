package com.example.owner.skymood.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.owner.skymood.R;
import com.example.owner.skymood.model.HourlyWeather;

import java.util.ArrayList;
import java.util.zip.Inflater;

/**
 * Created by owner on 04/04/2016.
 */
public class HourlyAdapter extends RecyclerView.Adapter<HourlyWeekViewHolder> {

    private Context context;
    private ArrayList<HourlyWeather> weathers;

    public HourlyAdapter(Context context, ArrayList<HourlyWeather> weathers){
        this.context = context;
        this.weathers = weathers;
    }


    @Override
    public HourlyWeekViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View row = inflater.inflate(R.layout.hour_row, parent, false);
        return new HourlyWeekViewHolder(row);
    }

    @Override
    public void onBindViewHolder(HourlyWeekViewHolder holder, int position) {
        HourlyWeather weather = weathers.get(position);
        holder.getHour().setText(weather.getHour() + ":00");
        holder.getTemp().setText(weather.getTemp() + " ℃");
        holder.getIcon().setImageBitmap(weather.getIcon());

    }

    @Override
    public int getItemCount() {
        if(weathers.size() > 24)
            return 24;
        return weathers.size();
    }

}
