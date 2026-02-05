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
import android.widget.AutoCompleteTextView
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.owner.skymood.MainActivity
import com.example.owner.skymood.R
import com.example.owner.skymood.asyncTasks.APIDataGetterAsyncTask
import com.example.owner.skymood.asyncTasks.AutoCompleteStringFillerAsyncTask
import com.example.owner.skymood.asyncTasks.FindLocationAsyncTask
import com.example.owner.skymood.asyncTasks.GetHourlyTask
import com.example.owner.skymood.asyncTasks.GetWeeklyTask
import com.example.owner.skymood.model.LocationPreference
import com.example.owner.skymood.model.MyLocation
import com.example.owner.skymood.model.MyLocationManager
import java.util.Calendar

class CurrentWeatherFragment : Fragment() {
    private lateinit var temperature: TextView
    private lateinit var condition: TextView
    private lateinit var feelsLike: TextView
    private lateinit var lastUpdate: TextView
    private lateinit var countryTextView: TextView
    private lateinit var minTempTextView: TextView
    private lateinit var maxTempTextView: TextView
    lateinit var weatherImage: ImageView
    private lateinit var addImage: ImageView
    private lateinit var syncButton: ImageView
    private lateinit var locationSearchButton: ImageView
    private lateinit var citySearchButton: ImageView
    private lateinit var writeCityEditText: AutoCompleteTextView
    private lateinit var progressBar: ProgressBar
    private lateinit var chosenCityTextView: TextView
    private lateinit var spinner: Spinner

    private var city: String? = null
    private var country: String? = null
    private var countryCode: String? = "0"
    private var cities: HashMap<String?, String?> = HashMap()
    private lateinit var citiesSpinner: ArrayList<String>
    private var keyboard: InputMethodManager? = null
    private lateinit var locPref: LocationPreference
    private lateinit var manager: MyLocationManager

    private fun initViews(rootView: ViewGroup) {
        syncButton =
            rootView.findViewById<View?>(R.id.fragment_current_weather_iv_sync) as ImageView
        locationSearchButton =
            rootView.findViewById<View?>(R.id.fragment_current_weather_iv_gps_search) as ImageView
        citySearchButton =
            rootView.findViewById<View?>(R.id.fragment_current_weather_iv_city_search) as ImageView
        writeCityEditText =
            rootView.findViewById<View?>(R.id.fragment_current_weather_actv_search_city) as AutoCompleteTextView
        temperature =
            rootView.findViewById<View?>(R.id.fragment_current_weather_tv_temperature) as TextView
        countryTextView =
            rootView.findViewById<View?>(R.id.fragment_current_weather_tv_country) as TextView
        condition =
            rootView.findViewById<View?>(R.id.fragment_current_weather_tv_condition) as TextView
        minTempTextView =
            rootView.findViewById<View?>(R.id.fragment_current_weather_tv_min_temp) as TextView
        maxTempTextView =
            rootView.findViewById<View?>(R.id.fragment_current_weather_tv_max_temp) as TextView
        feelsLike =
            rootView.findViewById<View?>(R.id.fragment_current_weather_tv_feels_like) as TextView
        lastUpdate =
            rootView.findViewById<View?>(R.id.fragment_current_weather_tv_last_update) as TextView
        weatherImage =
            rootView.findViewById<View?>(R.id.fragment_current_weather_iv_weather_state) as ImageView
        chosenCityTextView =
            rootView.findViewById<View?>(R.id.fragment_current_weather_tv_chosen_city) as TextView
        progressBar =
            rootView.findViewById<View?>(R.id.fragment_current_weather_view_progress_bar) as ProgressBar
        spinner =
            rootView.findViewById<View?>(R.id.fragment_current_weather_view_spinner_location) as Spinner

        val toolbar = (requireActivity() as MainActivity).toolbar
        addImage = toolbar.findViewById<View?>(R.id.view_toolbar_iv_add_favourite) as ImageView
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val rootView =
            inflater.inflate(R.layout.fragment_current_weather, container, false) as ViewGroup

        locPref = LocationPreference.getInstance(requireContext())
        manager = MyLocationManager.getInstance(requireContext())

        //initializing components
        initViews(rootView)

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
        spinner.adapter = adapter
        spinner.setSelection(1)

        //listeners
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {
                if ((parent?.getItemAtPosition(position))?.equals("My Locations") == false) {
                    if (isOnline) {
                        val locationsString = parent.getItemAtPosition(position) as String
                        val location = locationsString.split(",")
                        val city = if (location.isNotEmpty()) location[0] else ""
                        if (location.size > 1) country = location[0].trim()
                        setCity(city, country)

                        //countryCode from  DB
                        val task = APIDataGetterAsyncTask(
                            this@CurrentWeatherFragment,
                            requireContext(),
                            weatherImage
                        )
                        task.execute(countryCode, city, country)
                    } else {
                        Toast.makeText(context, "NO INTERNET CONNECTION", Toast.LENGTH_SHORT)
                            .show()
                    }

                }
                spinner.setSelection(0)
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                // do nothing
            }
        }


        citySearchButton.setOnClickListener {
            if (isOnline) {
                if (writeCityEditText.visibility == View.GONE) {
                    changeVisibility(View.GONE)

                    val slide: Animation = AnimationUtils.loadAnimation(context, android.R.anim.fade_in)
                    slide.duration = 1000
                    writeCityEditText.startAnimation(slide)
                    writeCityEditText.visibility = View.VISIBLE
                    writeCityEditText.setFocusable(true)
                    writeCityEditText.requestFocus()

                    keyboard = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    keyboard?.showSoftInput(writeCityEditText, 0)
                } else {
                    writeCityEditText.visibility = View.GONE
                    keyboard?.hideSoftInputFromWindow(writeCityEditText.windowToken, 0)
                    changeVisibility(View.VISIBLE)
                }
            } else {
                Toast.makeText(context, "NO INTERNET CONNECTION", Toast.LENGTH_SHORT).show()
            }
        }

        syncButton.setOnClickListener {
            if (isOnline) {
                val task =
                    APIDataGetterAsyncTask(
                        this@CurrentWeatherFragment,
                        requireContext(),
                        weatherImage
                    )
                task.execute(countryCode, city, country)
            } else {
                Toast.makeText(context, "NO INTERNET CONNECTION", Toast.LENGTH_SHORT).show()
            }
        }

        locationSearchButton.setOnClickListener {
            if (isOnline) {
                findLocation()
            } else {
                Toast.makeText(getContext(), "NO INTERNET CONNECTION", Toast.LENGTH_SHORT).show()
            }
        }

        writeCityEditText.setOnEditorActionListener { _, _, _ ->
            if (!writeCityEditText.text.toString().isEmpty() && writeCityEditText.text.toString()
                    .contains(",")
            ) {
                val location = writeCityEditText.text.toString()
                val parts: Array<String?> =
                    location.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                val city = parts[0]
                country = parts[1]!!.trim { it <= ' ' }
                getWeatherInfoByCity(city, country)
            } else if (writeCityEditText.text.toString() == "") {
                writeCityEditText.visibility = View.GONE
                keyboard!!.hideSoftInputFromWindow(writeCityEditText.windowToken, 0)
                changeVisibility(View.VISIBLE)
            } else {
                Toast.makeText(
                    context,
                    "You must specify a fragment_current_weather_tv_country",
                    Toast.LENGTH_SHORT
                ).show()
                writeCityEditText.visibility = View.GONE
                keyboard?.hideSoftInputFromWindow(writeCityEditText.windowToken, 0)
                changeVisibility(View.VISIBLE)
            }
            keyboard?.hideSoftInputFromWindow(writeCityEditText.windowToken, 0)
            false
        }

        writeCityEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                val chars = writeCityEditText.text.toString().length
                if (chars >= 3) {
                    val filler =
                        AutoCompleteStringFillerAsyncTask(
                            this@CurrentWeatherFragment,
                            requireContext()
                        )
                    filler.execute(writeCityEditText.text.toString())
                }
            }
        })

        //logic
        if (this.isOnline) {
            val task = APIDataGetterAsyncTask(this, requireContext(), weatherImage)
            val fr = (context as MainActivity).hourlyFragment
            val hourTask = GetHourlyTask(requireContext(), fr, fr.hourlyWeatherArray)
            val weeklyTask = GetWeeklyTask(requireContext(), fr, fr.weeklyWeatherArray)

            //first: check shared prefs
            if (locPref.isSetLocation) {
                setCity(locPref.city, locPref.country)
                countryCode = locPref.countryCode
                country = locPref.country
                task.execute(countryCode, city, country)
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
                feelsLike.text = "Please connect to Internet"
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
        return rootView
    } // end of onCreate

    val weatherInfoFromSharedPref: Unit
        get() {
            chosenCityTextView.visibility = View.VISIBLE
            chosenCityTextView.text = locPref.city
            countryTextView.text = country
            temperature.text = locPref.temperature + "°"
            minTempTextView.text = "⬇" + locPref.minTemp + "°"
            maxTempTextView.text = "⬆" + locPref.maxTemp + "°"
            condition.text = locPref.condition
            feelsLike.text = locPref.feelsLike
            lastUpdate.text = locPref.lastUpdate

            if (locPref.icon!!.contains("night")) {
                (context as MainActivity).changeBackground(MainActivity.NIGHT)
            } else {
                (context as MainActivity).changeBackground(MainActivity.DAY)
            }
            val con = weatherImage.context
            weatherImage.setImageResource(
                context!!.resources.getIdentifier(locPref.icon, "drawable", con.getPackageName())
            )
        }

    val isOnline: Boolean
        get() {
            val cm = context!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val netInfo = cm.activeNetworkInfo
            return netInfo != null && netInfo.isConnectedOrConnecting
        }

    fun setCity(city: String?, country: String?) {
        this.city = city?.replace(" ", "_") ?: "Aytos"
        this.country = country ?: ""
    }

    fun getWeatherInfoByCity(city: String?, country: String?) {
        if (city != null && !city.isEmpty()) {
            setCity(city, country)
            writeCityEditText.setText("")
            writeCityEditText.visibility = View.GONE
            spinner.visibility = View.VISIBLE
            syncButton.visibility = View.VISIBLE
            locationSearchButton.visibility = View.VISIBLE
            val task = APIDataGetterAsyncTask(this, requireContext(), weatherImage)
            task.execute(countryCode, city, country)
        }
    }

    fun apiDataGetterAsyncTaskOnPreExecute() {
        chosenCityTextView.visibility = View.GONE
        countryTextView.visibility = View.GONE
        progressBar.visibility = View.VISIBLE
    }

    fun apiDataGetterAsyncTaskOnPostExecute(
        temp: String?,
        condition: String?,
        feelsLike: String?,
        minTemp: String?,
        maxTemp: String?,
        dateAndTime: String?,
        lastUpdate: String?,
        cityToDisplay: String?,
        country: String?
    ) {
        this.progressBar.visibility = View.GONE
        this.chosenCityTextView.visibility = View.VISIBLE
        this.chosenCityTextView.text = cityToDisplay
        this.countryTextView.visibility = View.VISIBLE
        this.countryTextView.text = country
        this.addImage.visibility = View.VISIBLE

        if (temp != null) {
            this.temperature.text = "$temp°"
            this.condition.text = condition
            this.feelsLike.text = feelsLike
            this.minTempTextView.text = "⬇$minTemp°"
            this.maxTempTextView.text = "⬆$maxTemp°"
            this.lastUpdate.text = lastUpdate
        } else {
            this.temperature.text = ""
            this.condition.text = ""
            this.lastUpdate.text = ""
            this.maxTempTextView.text = ""
            this.minTempTextView.text = ""
            this.feelsLike.text = "Sorry, there is no information."
            this.lastUpdate.text =
                "This location does not exist\nor you have weak internet connection"
        }
    }

    fun autoCompleteStringFillerAsyncTaskOnPostExecute(adapterAutoComplete: ArrayAdapter<*>?) {
        this.writeCityEditText.setAdapter<ArrayAdapter<*>?>(adapterAutoComplete)
    }

    fun setCities(cities: HashMap<String?, String?>) {
        this.cities = cities
    }

    fun findLocation() {
        val findLocation = FindLocationAsyncTask(this, requireContext(), weatherImage)
        findLocation.execute()
    }

    fun changeVisibility(visibility: Int) {
        spinner.visibility = visibility
        syncButton.visibility = visibility
        locationSearchButton.visibility = visibility
        weatherImage.adjustViewBounds = true
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
        this.chosenCityTextView.visibility = View.VISIBLE
        this.chosenCityTextView.text = city
        this.countryTextView.text = country
        this.temperature.text = "$temp°"
        this.minTempTextView.text = "⬇$minTemp°"
        this.maxTempTextView.text = "⬆$maxTemp°"
        this.condition.text = condition
        this.feelsLike.text = feelsLike
        this.lastUpdate.text = lastUpdate

        val con = weatherImage.context
        weatherImage.setImageResource(
            requireContext().resources.getIdentifier(icon, "drawable", con.packageName)
        )
    }

    companion object {
        const val API_KEY: String = "5229b753f41a4812b74165454260402"

        private const val MORNING_HOUR = 6
        private const val NIGHT_HOUR = 19
    }
}
