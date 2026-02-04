package com.example.owner.skymood.adapters;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

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
        this.icon = (ImageView) itemView.findViewById(R.id.row_hour_iv_icon);
        this.hour = (TextView) itemView.findViewById(R.id.row_hour_tv_hour);
        this.condition = (TextView) itemView.findViewById(R.id.row_week_tv_condition);
        this.temp = (TextView) itemView.findViewById(R.id.row_hour_tv_temp);
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
