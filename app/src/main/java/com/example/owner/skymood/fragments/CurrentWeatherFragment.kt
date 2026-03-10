package com.example.owner.skymood.fragments

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Toast
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.owner.skymood.MainActivity
import com.example.owner.skymood.R
import com.example.owner.skymood.R.drawable
import com.example.owner.skymood.asyncTasks.AutoCompleteStringFillerAsyncTask
import com.example.owner.skymood.asyncTasks.FindLocationAsyncTask
import com.example.owner.skymood.asyncTasks.GetHourlyTask
import com.example.owner.skymood.asyncTasks.GetWeeklyTask
import com.example.owner.skymood.databinding.FragmentCurrentWeatherBinding
import com.example.owner.skymood.model.LocationPreference
import com.example.owner.skymood.model.MyLocation
import com.example.owner.skymood.model.MyLocationManager
import com.example.owner.skymood.model.SearchedLocation
import com.example.owner.skymood.model.SearchedLocationManager
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class CurrentWeatherFragment : Fragment() {

    private lateinit var binding: FragmentCurrentWeatherBinding

    lateinit var weatherImage: ImageView

    private lateinit var addImage: ImageView
    private var city: String? = null
    private var country: String? = null
    private var countryCode: String? = "0"
    private var cities: HashMap<String?, String?> = HashMap()
    private lateinit var citiesSpinner: ArrayList<String>
    private var keyboard: InputMethodManager? = null
    private lateinit var locPref: LocationPreference
    private lateinit var manager: MyLocationManager
    private lateinit var searchedLocationManager: SearchedLocationManager

    private val viewModel: CurrentWeatherViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCurrentWeatherBinding.inflate(inflater, container, false)
        weatherImage = binding.fragmentCurrentWeatherIvWeatherState

        locPref = LocationPreference.getInstance(requireContext())
        manager = MyLocationManager.getInstance(requireContext())
        searchedLocationManager = SearchedLocationManager.getInstance(requireContext())

        val toolbar = (requireActivity() as MainActivity).toolbar
        addImage = toolbar.findViewById(R.id.view_toolbar_iv_add_favourite)

        //setting background
        setBackground()

        //setting adapter to spinner
        citiesSpinner = arrayListOf(
            "My Locations",
            "Sofia",
            "Burgas",
            "Varna",
            "Plovdiv"
        )
        citiesSpinner.addAll(manager.allStringLocations)
        val adapter: ArrayAdapter<*> =
            ArrayAdapter<Any?>(requireContext(), R.layout.view_spinner, citiesSpinner.toList())
        binding.fragmentCurrentWeatherViewSpinnerLocation.adapter = adapter
        binding.fragmentCurrentWeatherViewSpinnerLocation.setSelection(1)

        //listeners
        binding.fragmentCurrentWeatherViewSpinnerLocation.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?, view: View?, position: Int, id: Long
                ) {
                    if ((parent?.getItemAtPosition(position))?.equals("My Locations") == false) {
                        if (isOnline) {
                            val locationsString = parent.getItemAtPosition(position) as String
                            val location = locationsString.split(",")
                            val city = if (location.isNotEmpty()) location[0] else ""
                            if (location.size > 1) country = location[1].trim()
                            setCity(city, country)

                            viewModel.fetchWeather(city, API_KEY)
                        } else {
                            Toast.makeText(context, "NO INTERNET CONNECTION", Toast.LENGTH_SHORT)
                                .show()
                        }

                    }
                    binding.fragmentCurrentWeatherViewSpinnerLocation.setSelection(0)
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                    // do nothing
                }
            }


        binding.fragmentCurrentWeatherIvCitySearch.setOnClickListener {
            if (isOnline) {
                if (binding.fragmentCurrentWeatherActvSearchCity.isGone) {
                    changeVisibility(View.GONE)

                    val slide: Animation = AnimationUtils.loadAnimation(context, android.R.anim.fade_in)
                    slide.duration = 1000
                    binding.fragmentCurrentWeatherActvSearchCity.startAnimation(slide)
                    binding.fragmentCurrentWeatherActvSearchCity.visibility = View.VISIBLE
                    binding.fragmentCurrentWeatherActvSearchCity.isFocusable = true
                    binding.fragmentCurrentWeatherActvSearchCity.requestFocus()

                    keyboard = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    keyboard?.showSoftInput(binding.fragmentCurrentWeatherActvSearchCity, 0)
                } else {
                    binding.fragmentCurrentWeatherActvSearchCity.visibility = View.GONE
                    keyboard?.hideSoftInputFromWindow(
                        binding.fragmentCurrentWeatherActvSearchCity.windowToken,
                        0
                    )
                    changeVisibility(View.VISIBLE)
                }
            } else {
                Toast.makeText(context, "NO INTERNET CONNECTION", Toast.LENGTH_SHORT).show()
            }
        }

        binding.fragmentCurrentWeatherIvSync.setOnClickListener {
            if (isOnline && city != null) {
                viewModel.fetchWeather(city!!, API_KEY)
            } else {
                Toast.makeText(context, "NO INTERNET CONNECTION", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        binding.fragmentCurrentWeatherIvGpsSearch.setOnClickListener {
            if (isOnline) {
                findLocation()
            } else {
                Toast.makeText(requireContext(), "NO INTERNET CONNECTION", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        binding.fragmentCurrentWeatherActvSearchCity.setOnEditorActionListener { _, _, _ ->
            if (binding.fragmentCurrentWeatherActvSearchCity.text.toString()
                    .isNotEmpty() && binding.fragmentCurrentWeatherActvSearchCity.text.toString()
                    .contains(",")
            ) {
                val location = binding.fragmentCurrentWeatherActvSearchCity.text.toString()
                val parts: Array<String?> =
                    location.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                val city = parts[0]
                country = parts[1]!!.trim { it <= ' ' }
                getWeatherInfoByCity(city, country)
            } else if (binding.fragmentCurrentWeatherActvSearchCity.text.toString() == "") {
                binding.fragmentCurrentWeatherActvSearchCity.visibility = View.GONE
                keyboard!!.hideSoftInputFromWindow(
                    binding.fragmentCurrentWeatherActvSearchCity.windowToken,
                    0
                )
                changeVisibility(View.VISIBLE)
            } else {
                Toast.makeText(
                    context,
                    "You must specify a fragment_current_weather_tv_country",
                    Toast.LENGTH_SHORT
                ).show()
                binding.fragmentCurrentWeatherActvSearchCity.visibility = View.GONE
                keyboard?.hideSoftInputFromWindow(
                    binding.fragmentCurrentWeatherActvSearchCity.windowToken,
                    0
                )
                changeVisibility(View.VISIBLE)
            }
            keyboard?.hideSoftInputFromWindow(
                binding.fragmentCurrentWeatherActvSearchCity.windowToken,
                0
            )
            false
        }

        binding.fragmentCurrentWeatherActvSearchCity.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                val chars = binding.fragmentCurrentWeatherActvSearchCity.text.toString().length
                if (chars >= 3) {
                    val filler =
                        AutoCompleteStringFillerAsyncTask(
                            this@CurrentWeatherFragment,
                            requireContext()
                        )
                    filler.execute(binding.fragmentCurrentWeatherActvSearchCity.text.toString())
                }
            }
        })

        //logic
        if (this.isOnline) {
            val fr = (context as MainActivity).hourlyFragment
            val hourTask = GetHourlyTask(requireContext(), fr, fr.hourlyWeatherArray)
            val weeklyTask = GetWeeklyTask(requireContext(), fr, fr.weeklyWeatherArray)

            //first: check shared prefs
            if (locPref.isSetLocation) {
                setCity(locPref.city, locPref.country)
                countryCode = locPref.countryCode
                country = locPref.country
                viewModel.fetchWeather(city!!, API_KEY)
                hourTask.execute(city, countryCode)
                weeklyTask.execute(city, countryCode)
            } else {
                //API autoIP
                findLocation()
            }
        } else {
            if (locPref.isSetLocation && !locPref.hasNull()) {
                Toast.makeText(
                    context,
                    "NO INTERNET CONNECTION\nFor up to date info connect to Internet",
                    Toast.LENGTH_LONG
                ).show()
                setCity(locPref.city, locPref.country)
                country = locPref.country
                countryCode = locPref.countryCode
                this.weatherInfoFromSharedPref
            } else {
                binding.fragmentCurrentWeatherTvFeelsLike.text = "Please connect to Internet"
            }
        }

        addImage.setOnClickListener {
            if (city != null) {
                val myLoc = MyLocation(city, countryCode, country, "$city, $country", null)
                if (manager.selectMyLocation(myLoc) == null) {
                    manager.insertMyLocation(myLoc)
                    citiesSpinner.add("$city, $country")
                    adapter.notifyDataSetChanged()
                    Toast.makeText(context, "location inserted to MyLocations", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    Toast.makeText(context, "location already exists", Toast.LENGTH_SHORT).show()
                }
            }
        }
        return binding.root
    } // end of onCreate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.isLoading.observe(viewLifecycleOwner) {
            if (it) {
                binding.fragmentCurrentWeatherTvChosenCity.visibility = View.GONE
                binding.fragmentCurrentWeatherTvCountry.visibility = View.GONE
                binding.fragmentCurrentWeatherViewProgressBar.visibility = View.VISIBLE
            }
        }

        viewModel.weatherData.observe(viewLifecycleOwner) {
            binding.fragmentCurrentWeatherViewProgressBar.visibility = View.GONE
            binding.fragmentCurrentWeatherTvChosenCity.visibility = View.VISIBLE
            binding.fragmentCurrentWeatherTvChosenCity.text = it.location.name
            binding.fragmentCurrentWeatherTvCountry.visibility = View.VISIBLE
            binding.fragmentCurrentWeatherTvCountry.text = country
            addImage.visibility = View.VISIBLE

            //TODO should come from somewhere else
            val maxTemp = "10"
            val minTemp = "-3"

            val cal = Calendar.getInstance()
            cal.add(Calendar.DATE, 0)
            val format = SimpleDateFormat("HH:mm, dd.MM.yyyy", Locale.getDefault())
            val dateAndTime = format.format(cal.getTime())
            val lastUpdate = "Last update: $dateAndTime"


            binding.fragmentCurrentWeatherTvTemperature.text = "${it.current.tempC}°"
            binding.fragmentCurrentWeatherTvCondition.text = it.current.condition.text
            binding.fragmentCurrentWeatherTvFeelsLike.text = "Feels like: ${it.current.feelsLikeC}°"
            binding.fragmentCurrentWeatherTvMinTemp.text = "⬇$minTemp°"
            binding.fragmentCurrentWeatherTvMaxTemp.text = "⬆$maxTemp°"
            binding.fragmentCurrentWeatherTvLastUpdate.text =lastUpdate

            val isNight = it.current.condition.icon.contains("night")
            weatherImage.setImageResource(
                getImageResource(
                    it.current.condition.code,
                    isNight
                )
            )

            val mainActivity = requireActivity() as MainActivity
            mainActivity.changeBackground(if (isNight) MainActivity.NIGHT else MainActivity.DAY)

            mainActivity.setInfo(city, countryCode, minTemp, maxTemp, dateAndTime)
            if (locPref.isSetLocation && city == locPref.city && countryCode == locPref.countryCode) {
                //insert in shared prefs
                locPref.setPreferredLocation(
                    city = city,
                    country = country,
                    countryCode = countryCode,
                    icon = it.current.condition.code.toString(),
                    temperature = it.current.tempC.toString(),
                    minTemp = minTemp,
                    maxTemp = maxTemp,
                    condition = it.current.condition.text,
                    feelsLike = it.current.feelsLikeC.toString(),
                    lastUpdate = it.current.feelsLikeC.toString()
                )
            } else {
                //insert into DB
                val loc = SearchedLocation(
                    city = city,
                    temp = it.current.tempC.toString(),
                    condition = it.current.condition.text,
                    country = country,
                    code = countryCode,
                    max = maxTemp,
                    min = minTemp,
                    lastUpdate = lastUpdate,
                    icon = it.current.condition.code.toString(),
                    feelsLike = it.current.feelsLikeC.toString()
                )
                searchedLocationManager.insertSearchedLocation(loc)
            }
        }

        viewModel.error.observe(viewLifecycleOwner) {
            binding.fragmentCurrentWeatherTvFeelsLike.text = "Sorry, something went wrong."
            binding.fragmentCurrentWeatherTvLastUpdate.text = it
        }
    }

    val weatherInfoFromSharedPref: Unit
        get() {
            binding.fragmentCurrentWeatherTvChosenCity.visibility = View.VISIBLE
            binding.fragmentCurrentWeatherTvChosenCity.text = locPref.city
            binding.fragmentCurrentWeatherTvCountry.text = country
            binding.fragmentCurrentWeatherTvTemperature.text = locPref.temperature + "°"
            binding.fragmentCurrentWeatherTvMinTemp.text = "⬇" + locPref.minTemp + "°"
            binding.fragmentCurrentWeatherTvMaxTemp.text = "⬆" + locPref.maxTemp + "°"
            binding.fragmentCurrentWeatherTvCondition.text = locPref.condition
            binding.fragmentCurrentWeatherTvFeelsLike.text = locPref.feelsLike
            binding.fragmentCurrentWeatherTvLastUpdate.text = locPref.lastUpdate

            if (locPref.icon!!.contains("night")) {
                (context as MainActivity).changeBackground(MainActivity.NIGHT)
            } else {
                (context as MainActivity).changeBackground(MainActivity.DAY)
            }
            val con = binding.fragmentCurrentWeatherIvWeatherState.context
            binding.fragmentCurrentWeatherIvWeatherState.setImageResource(
                requireContext().resources.getIdentifier(locPref.icon, "drawable", con.packageName)
            )
        }

    val isOnline: Boolean
        get() {
            val cm =
                requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val netInfo = cm.activeNetworkInfo
            return netInfo != null && netInfo.isConnectedOrConnecting
        }

    fun setCity(city: String?, country: String?) {
        this.city = city?.replace(" ", "_") ?: "Aytos"
        this.country = country ?: ""
    }

    fun getWeatherInfoByCity(city: String?, country: String?) {
        if (city != null && city.isNotEmpty()) {
            setCity(city, country)
            binding.fragmentCurrentWeatherActvSearchCity.setText("")
            binding.fragmentCurrentWeatherActvSearchCity.visibility = View.GONE
            binding.fragmentCurrentWeatherViewSpinnerLocation.visibility = View.VISIBLE
            binding.fragmentCurrentWeatherIvSync.visibility = View.VISIBLE
            binding.fragmentCurrentWeatherIvGpsSearch.visibility = View.VISIBLE
            viewModel.fetchWeather(city, API_KEY)
        }
    }

    fun autoCompleteStringFillerAsyncTaskOnPostExecute(adapterAutoComplete: ArrayAdapter<*>?) {
        binding.fragmentCurrentWeatherActvSearchCity.setAdapter(adapterAutoComplete)
    }

    fun setCities(cities: HashMap<String?, String?>) {
        this.cities = cities
    }

    fun findLocation() {
        val findLocation = FindLocationAsyncTask(
            this,
            requireContext(),
            binding.fragmentCurrentWeatherIvWeatherState
        )
        findLocation.execute()
    }

    fun changeVisibility(visibility: Int) {
        binding.fragmentCurrentWeatherViewSpinnerLocation.visibility = visibility
        binding.fragmentCurrentWeatherIvSync.visibility = visibility
        binding.fragmentCurrentWeatherIvGpsSearch.visibility = visibility
        binding.fragmentCurrentWeatherIvWeatherState.adjustViewBounds = true
    }

    private fun setBackground() {
        val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        val isDay = hour in MORNING_HOUR..NIGHT_HOUR
        val partOfDay = if (isDay) MainActivity.DAY else MainActivity.NIGHT
        (context as MainActivity).changeBackground(partOfDay)
    }

    fun setInfoData(
        city: String?,
        country: String?,
        icon: String?,
        temp: String?,
        minTemp: String?,
        maxTemp: String?,
        condition: String?,
        feelsLike: String?,
        lastUpdate: String?
    ) {
        binding.fragmentCurrentWeatherTvChosenCity.visibility = View.VISIBLE
        binding.fragmentCurrentWeatherTvChosenCity.text = city
        binding.fragmentCurrentWeatherTvCountry.text = country
        binding.fragmentCurrentWeatherTvTemperature.text = "$temp°"
        binding.fragmentCurrentWeatherTvMinTemp.text = "⬇$minTemp°"
        binding.fragmentCurrentWeatherTvMaxTemp.text = "⬆$maxTemp°"
        binding.fragmentCurrentWeatherTvCondition.text = condition
        binding.fragmentCurrentWeatherTvFeelsLike.text = feelsLike
        binding.fragmentCurrentWeatherTvLastUpdate.text = lastUpdate

        val con = binding.fragmentCurrentWeatherIvWeatherState.context
        binding.fragmentCurrentWeatherIvWeatherState.setImageResource(
            requireContext().resources.getIdentifier(icon, "drawable", con.packageName)
        )
    }

    fun updateWeatherInfo(city: String) {
        viewModel.fetchWeather(city, API_KEY)
    }

    private fun getImageResource(code: Int, isNight: Boolean) = when (code) {
        1000 -> if (isNight) drawable.sunny_night else drawable.sunny

        1003 -> if (isNight) drawable.partlycloudy_night else drawable.partlycloudy

        1006 -> if (isNight) drawable.mostlycloudy_night else drawable.mostlycloudy

        1009 -> if (isNight) drawable.cloudy_night else drawable.cloudy

        1030, 1135, 1147 -> if (isNight) drawable.fog_night else drawable.fog

        1063 -> if (isNight) drawable.chancerain_night else drawable.chancerain

        1072, 1066 -> if (isNight) drawable.chancesnow_night else drawable.chancesnow

        1069 -> if (isNight) drawable.chancesleet_night else drawable.chancesleet

        1273, 1276, 1279, 1282, 1087 -> if (isNight) drawable.tstorms_night else drawable.tstorms

        1114, 1210, 1213, 1216, 1219, 1222, 1225,
        1237, 1255, 1258, 1261, 1264, 1171, 1117 -> if (isNight) drawable.snow_night else drawable.snow

        1150, 1168, 1153 -> if (isNight) drawable.hazy_night else drawable.hazy

        1180, 1186, 1198, 1189, 1192, 1201,
        1240, 1243, 1246, 1183 -> if (isNight) drawable.rain_night else drawable.rain

        1207, 1249, 1252, 1204 -> if (isNight) drawable.sleet_night else drawable.sleet

        else -> drawable.icon_not_available
    }

    companion object {
        const val API_KEY: String = "5229b753f41a4812b74165454260402"

        private const val MORNING_HOUR = 6
        private const val NIGHT_HOUR = 19
    }
}

