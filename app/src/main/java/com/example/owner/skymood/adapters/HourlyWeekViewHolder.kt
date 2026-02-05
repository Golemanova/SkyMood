package com.example.owner.skymood.adapters

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.owner.skymood.R

/**
 * Created by owner on 05/04/2016.
 */
class HourlyWeekViewHolder(itemView: View) : ViewHolder(itemView) {

    val icon: ImageView = itemView.findViewById<View?>(R.id.row_hour_iv_icon) as ImageView
    val hour: TextView = itemView.findViewById<View?>(R.id.row_hour_tv_hour) as TextView
    val condition: TextView = itemView.findViewById<View?>(R.id.row_week_tv_condition) as TextView
    val temp: TextView = itemView.findViewById<View?>(R.id.row_hour_tv_temp) as TextView
}
