package com.example.owner.skymood.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.owner.skymood.R
import com.example.owner.skymood.adapters.MyCardViewAdapter.CardViewHolder
import com.example.owner.skymood.model.LocationPreference
import com.example.owner.skymood.model.MyLocation
import com.example.owner.skymood.model.MyLocationManager
import com.example.owner.skymood.model.MyLocationManager.Companion.getInstance

/**
 * Created by owner on 08/04/2016.
 */
class MyCardViewAdapter(
    private val context: Context,
    private val data: ArrayList<MyLocation>
) : RecyclerView.Adapter<CardViewHolder?>() {

    private var lastCheckedPosition: Int = -1
    var pref: LocationPreference = LocationPreference.getInstance(context)
    var manager: MyLocationManager = getInstance(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val inflater = LayoutInflater.from(context)
        val root = inflater.inflate(R.layout.row_my_location, parent, false)
        return CardViewHolder(root)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val location = data[position]
        holder.city.text = location.city
        holder.country.text = location.country
        holder.code.text = location.code


        if (pref.city != null && location.city == pref.city && location.country == pref.country) {
            holder.radio.isChecked = true
        } else {
            holder.radio.isChecked = position == lastCheckedPosition
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class CardViewHolder(itemView: View) : ViewHolder(itemView) {
        var city: TextView = itemView.findViewById<View?>(R.id.row_my_location_tv_city) as TextView
        var country: TextView =
            itemView.findViewById<View?>(R.id.row_my_location_tv_country) as TextView
        var code: TextView = itemView.findViewById<View?>(R.id.row_my_location_tv_code) as TextView
        var radio: RadioButton =
            itemView.findViewById<View?>(R.id.row_my_location_view_radio_btn) as RadioButton
        var erase: ImageView =
            itemView.findViewById<View?>(R.id.row_my_location_iv_delete) as ImageView

        init {
            // onclick for setting location as default
            radio.setOnClickListener {
                lastCheckedPosition = adapterPosition
                notifyItemRangeChanged(0, data.size)
                val city = city.text.toString()
                pref.setPreferredLocation(
                    city = city,
                    country = country.text.toString(),
                    countryCode = code.text.toString(),
                    icon = null,
                    temperature = null,
                    minTemp = null,
                    maxTemp = null,
                    condition = null,
                    feelsLike = null,
                    lastUpdate = null
                )
                Toast.makeText(context, "$city was set as default location", Toast.LENGTH_SHORT)
                    .show()
            }

            erase.setOnClickListener {
                val position = adapterPosition
                manager.deleteMyLocation(data[position])
                removeAt(position)
                if (position < lastCheckedPosition) {
                    lastCheckedPosition--
                } else if (position == lastCheckedPosition) {
                    pref.removeInfo()
                }
            }
        }
    }

    fun removeAt(position: Int) {
        data.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, data.size)
    }
}
