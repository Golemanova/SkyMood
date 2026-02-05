package com.example.owner.skymood.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.owner.skymood.MainActivity
import com.example.owner.skymood.R

class MoreInfoFragment : Fragment() {
    private var city: String? = null
    private var code: String? = null

    private lateinit var day: TextView
    private lateinit var date: TextView
    private lateinit var temp: TextView
    private lateinit var feels: TextView
    private lateinit var min: TextView
    private lateinit var max: TextView
    private lateinit var uv: TextView
    private lateinit var humidity: TextView
    private lateinit var pressure: TextView
    private lateinit var visibility: TextView
    private lateinit var sunrise: TextView
    private lateinit var sunset: TextView
    private lateinit var moonAge: TextView
    private lateinit var moonIlluminated: TextView
    private lateinit var windSpeed: TextView
    private lateinit var condition: TextView
    private lateinit var moonPhase: TextView
    lateinit var progress: ProgressBar

    lateinit var layout: LinearLayout

    private lateinit var textCon: TextView

    private var dayTxt: String? = null
    private var dateTxt: String? = null
    private var tempTxt: String? = null
    private var feelsTxt: String? = null
    private var minTxt: String? = null
    private var maxTxt: String? = null
    private var uvTxt: String? = null
    private var humidityTxt: String? = null
    private var pressureTxt: String? = null
    private var windsSpeedTxt: String? = null
    private var visibilityTxt: String? = null
    private var sunriseTxt: String? = null
    private var sunsetTxt: String? = null
    private var conditionTxt: String? = null
    private var moonPhaseTxt: String? = null
    private var moonAgeTxt = 0
    private var moonIlluminatedTxt = 0
    private var activity: MainActivity? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setRetainInstance(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val root = inflater.inflate(R.layout.fragment_more_info, container, false)

        this.day = root.findViewById<View?>(R.id.fragment_more_info_tv_day) as TextView
        this.date = root.findViewById<View?>(R.id.fragment_more_info_tv_date) as TextView
        this.temp = root.findViewById<View?>(R.id.fragment_more_info_tv_temp_value) as TextView
        this.feels =
            root.findViewById<View?>(R.id.fragment_more_info_tv_real_feel_value) as TextView
        this.min = root.findViewById<View?>(R.id.fragment_more_info_tv_min_value) as TextView
        this.max = root.findViewById<View?>(R.id.fragment_more_info_tv_max_value) as TextView
        this.uv = root.findViewById<View?>(R.id.fragment_more_info_tv_uv_index_value) as TextView
        this.pressure =
            root.findViewById<View?>(R.id.fragment_more_info_tv_pressure_value) as TextView
        this.humidity =
            root.findViewById<View?>(R.id.fragment_more_info_tv_humidity_value) as TextView
        this.visibility =
            root.findViewById<View?>(R.id.fragment_more_info_tv_visibility_value) as TextView
        this.sunrise =
            root.findViewById<View?>(R.id.fragment_more_info_tv_sunrise_value) as TextView
        this.sunset = root.findViewById<View?>(R.id.fragment_more_info_tv_sunset_value) as TextView
        this.moonIlluminated =
            root.findViewById<View?>(R.id.fragment_more_info_tv_moon_value) as TextView
        this.moonAge =
            root.findViewById<View?>(R.id.fragment_more_info_tv_moon_age_value) as TextView
        this.condition =
            root.findViewById<View?>(R.id.fragment_more_info_tv_condition_value) as TextView
        this.windSpeed =
            root.findViewById<View?>(R.id.fragment_more_info_tv_winds_speed_value) as TextView
        this.moonPhase =
            root.findViewById<View?>(R.id.fragment_more_info_tv_phase_of_moon_value) as TextView
        this.progress =
            root.findViewById<View?>(R.id.fragment_more_info_view_progress_bar) as ProgressBar
        this.layout = root.findViewById<View?>(R.id.fragment_more_info_container) as LinearLayout
        this.textCon = root.findViewById<View?>(R.id.fragment_more_info_tv_condition) as TextView

        activity = context as MainActivity?
        return root
    }

    fun setData() {
        this.temp.text = "$tempTxt℃"
        this.feels.text = "$feelsTxt℃"
        this.min.text = "$minTxt℃"
        this.max.text = "$maxTxt℃"
        this.uv.text = uvTxt
        this.humidity.text = humidityTxt
        this.pressure.text = "$pressureTxt hPa"
        this.visibility.text = "$visibilityTxt km"
        this.sunrise.text = sunriseTxt
        this.sunset.text = sunsetTxt
        this.moonAge.text = moonAgeTxt.toString() + ""
        this.moonIlluminated.text = "$moonIlluminatedTxt % illuminated"
        this.windSpeed.text = "$windsSpeedTxt kmh"
        this.condition.text = conditionTxt
        this.date.text = dateTxt
        this.day.text = city
        this.moonPhase.text = moonPhaseTxt
        if (this.condition.height > textCon.height) textCon.height = this.condition.height
    }

    fun setExternalInfo(city: String?, code: String?, date: String?, min: String?, max: String?) {
        this.city = city
        this.code = code
        this.minTxt = min
        this.maxTxt = max
        this.dateTxt = date
    }

    fun setTaskInfo(
        day: String?,
        temp: String?,
        feels: String?,
        uv: String?,
        humidity: String?,
        pressure: String?,
        windsSpeed: String?,
        visibility: String?,
        sunrise: String?,
        sunset: String?,
        condition: String?,
        moonPhase: String?,
        moonAge: Int,
        illuminate: Int
    ) {
        this.dayTxt = day
        this.tempTxt = temp
        this.feelsTxt = feels
        this.uvTxt = uv
        this.humidityTxt = humidity
        this.pressureTxt = pressure
        this.windsSpeedTxt = windsSpeed
        this.visibilityTxt = visibility
        this.sunriseTxt = sunrise
        this.sunsetTxt = sunset
        this.conditionTxt = condition
        this.moonPhaseTxt = moonPhase
        this.moonAgeTxt = moonAge
        this.moonIlluminatedTxt = illuminate
    }
}
