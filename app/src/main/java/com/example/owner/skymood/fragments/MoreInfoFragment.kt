package com.example.owner.skymood.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.owner.skymood.MainActivity
import com.example.owner.skymood.databinding.FragmentMoreInfoBinding

class MoreInfoFragment : Fragment() {
    private lateinit var binding: FragmentMoreInfoBinding
    private var city: String? = null
    private var code: String? = null

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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMoreInfoBinding.inflate(inflater, container, false)
        activity = context as? MainActivity
        return binding.root
    }

    fun setData() {
        binding.fragmentMoreInfoTvTempValue.text = "$tempTxt℃"
        binding.fragmentMoreInfoTvRealFeelValue.text = "$feelsTxt℃"
        binding.fragmentMoreInfoTvMinValue.text = "$minTxt℃"
        binding.fragmentMoreInfoTvMaxValue.text = "$maxTxt℃"
        binding.fragmentMoreInfoTvUvIndexValue.text = uvTxt
        binding.fragmentMoreInfoTvHumidityValue.text = humidityTxt
        binding.fragmentMoreInfoTvPressureValue.text = "$pressureTxt hPa"
        binding.fragmentMoreInfoTvVisibilityValue.text = "$visibilityTxt km"
        binding.fragmentMoreInfoTvSunriseValue.text = sunriseTxt
        binding.fragmentMoreInfoTvSunsetValue.text = sunsetTxt
        binding.fragmentMoreInfoTvMoonAgeValue.text = moonAgeTxt.toString()
        binding.fragmentMoreInfoTvMoonValue.text = "$moonIlluminatedTxt % illuminated"
        binding.fragmentMoreInfoTvWindsSpeedValue.text = "$windsSpeedTxt kmh"
        binding.fragmentMoreInfoTvConditionValue.text = conditionTxt
        binding.fragmentMoreInfoTvDate.text = dateTxt
        binding.fragmentMoreInfoTvDay.text = city
        binding.fragmentMoreInfoTvPhaseOfMoonValue.text = moonPhaseTxt
        
        if (binding.fragmentMoreInfoTvConditionValue.height > binding.fragmentMoreInfoTvCondition.height) {
            binding.fragmentMoreInfoTvCondition.height = binding.fragmentMoreInfoTvConditionValue.height
        }
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
