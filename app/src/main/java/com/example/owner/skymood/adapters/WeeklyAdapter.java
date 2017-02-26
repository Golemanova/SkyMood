package com.example.owner.skymood.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.owner.skymood.R;
import com.example.owner.skymood.model.WeeklyWeather;

import java.util.ArrayList;
import java.util.zip.Inflater;

/**
 * Created by owner on 05/04/2016.
 */
public class WeeklyAdapter extends RecyclerView.Adapter<HourlyWeekViewHolder> {

    private ArrayList<WeeklyWeather> weathers;
    private Context context;

    public WeeklyAdapter(ArrayList<WeeklyWeather> weathers, Context context) {
        this.weathers = weathers;
        this.context = context;
    }


    @Override
    public HourlyWeekViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.weekly_row, parent, false);
        return new HourlyWeekViewHolder(view);
    }

    @Override
    public void onBindViewHolder(HourlyWeekViewHolder holder, int position) {
        WeeklyWeather weather = weathers.get(position);
        holder.getIcon().setImageBitmap(weather.getIcon());
        holder.getTemp().setText(weather.getMax() + " / " + weather.getMin()+"°");
        holder.getCondition().setText(weather.getCondition());
        holder.getHour().setText(weather.getDay());
    }

    @Override
    public int getItemCount() {
        return weathers.size();
    }
}
