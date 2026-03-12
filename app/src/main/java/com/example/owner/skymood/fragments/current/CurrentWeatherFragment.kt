package com.example.owner.skymood.fragments.current

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
import com.example.owner.skymood.asyncTasks.AutoCompleteStringFillerAsyncTask
import com.example.owner.skymood.asyncTasks.FindLocationAsyncTask
import com.example.owner.skymood.asyncTasks.GetWeeklyTask
import com.example.owner.skymood.databinding.FragmentCurrentWeatherBinding
import com.example.owner.skymood.fragments.current.CurrentWeatherViewModel
import com.example.owner.skymood.model.LocationPreference
import com.example.owner.skymood.model.MyLocation
import com.example.owner.skymood.model.MyLocationManager
import com.example.owner.skymood.model.SearchedLocation
import com.example.owner.skymood.model.SearchedLocationManager
import java.util.Calendar

class CurrentWeatherFragment : Fragment() {

    private lateinit var binding: FragmentCurrentWeatherBinding

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

        locPref = LocationPreference.Companion.getInstance(requireContext())
        manager = MyLocationManager.Companion.getInstance(requireContext())
        searchedLocationManager = SearchedLocationManager.Companion.getInstance(requireContext())

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

                            viewModel.fetchWeather(city)
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
                viewModel.fetchWeather(city!!)
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
            val weeklyTask = GetWeeklyTask(requireContext(), fr, fr.weeklyWeatherArray)

            //first: check shared prefs
            if (locPref.isSetLocation) {
                setCity(locPref.city, locPref.country)
                countryCode = locPref.countryCode
                country = locPref.country
                viewModel.fetchWeather(city!!)
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
            binding.fragmentCurrentWeatherTvChosenCity.text = it.cityName
            binding.fragmentCurrentWeatherTvCountry.visibility = View.VISIBLE
            binding.fragmentCurrentWeatherTvCountry.text = it.country
            addImage.visibility = View.VISIBLE

            binding.fragmentCurrentWeatherTvTemperature.text = it.tempC
            binding.fragmentCurrentWeatherTvCondition.text = it.conditionText
            binding.fragmentCurrentWeatherTvFeelsLike.text = it.feelsLikeC
            binding.fragmentCurrentWeatherTvMinTemp.text = it.minTempC
            binding.fragmentCurrentWeatherTvMaxTemp.text = it.maxTempC
            binding.fragmentCurrentWeatherTvLastUpdate.text = it.lastUpdate
            binding.fragmentCurrentWeatherIvWeatherState.setImageResource(it.imageRes)

            val mainActivity = requireActivity() as MainActivity
            mainActivity.changeBackground(if (it.isNight) MainActivity.Companion.NIGHT else MainActivity.Companion.DAY)

            mainActivity.setInfo(it.cityName, countryCode, it.minTempC, it.maxTempC, it.lastUpdate)
            if (locPref.isSetLocation && city == locPref.city && countryCode == locPref.countryCode) {
                //insert in shared prefs
                locPref.setPreferredLocation(
                    city = it.cityName,
                    country = it.country,
                    countryCode = countryCode,
                    icon = it.imageRes.toString(),
                    temperature = it.tempC,
                    minTemp = it.minTempC,
                    maxTemp = it.maxTempC,
                    condition = it.conditionText,
                    feelsLike = it.feelsLikeC,
                    lastUpdate = it.lastUpdate
                )
            } else {
                //insert into DB
                val loc = SearchedLocation(
                    city = it.cityName,
                    temp = it.tempC,
                    condition = it.conditionText,
                    country = it.country,
                    code = countryCode,
                    max = it.maxTempC,
                    min = it.minTempC,
                    lastUpdate = it.lastUpdate,
                    icon = it.imageRes.toString(),
                    feelsLike = it.feelsLikeC
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
            binding.fragmentCurrentWeatherTvTemperature.text = locPref.temperature
            binding.fragmentCurrentWeatherTvMinTemp.text = locPref.minTemp
            binding.fragmentCurrentWeatherTvMaxTemp.text = locPref.maxTemp
            binding.fragmentCurrentWeatherTvCondition.text = locPref.condition
            binding.fragmentCurrentWeatherTvFeelsLike.text = locPref.feelsLike
            binding.fragmentCurrentWeatherTvLastUpdate.text = locPref.lastUpdate

            if (locPref.icon!!.contains("night")) {
                (context as MainActivity).changeBackground(MainActivity.Companion.NIGHT)
            } else {
                (context as MainActivity).changeBackground(MainActivity.Companion.DAY)
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
            viewModel.fetchWeather(city)
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
        val partOfDay = if (isDay) MainActivity.Companion.DAY else MainActivity.Companion.NIGHT
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
        viewModel.fetchWeather(city)
    }

    companion object {
        private const val MORNING_HOUR = 6
        private const val NIGHT_HOUR = 19
    }
}