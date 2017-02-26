package com.example.owner.skymood.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.owner.skymood.R;

/**
 * Created by owner on 05/04/2016.
 */
public class HourlyWeekViewHolder extends RecyclerView.ViewHolder {

    private ImageView icon;
    private TextView hour;
    private TextView condition;
    private TextView temp;

    public HourlyWeekViewHolder(View itemView) {
        super(itemView);
        this.icon = (ImageView) itemView.findViewById(R.id.hour_icon);
        this.hour = (TextView) itemView.findViewById(R.id.hour_hour);
        this.condition = (TextView) itemView.findViewById(R.id.hour_condition);
        this.temp = (TextView) itemView.findViewById(R.id.hour_temp);
    }

    public ImageView getIcon() {
        return icon;
    }

    public TextView getHour() {
        return hour;
    }

    public TextView getCondition() {
        return condition;
    }

    public TextView getTemp() {
        return temp;
    }
}
