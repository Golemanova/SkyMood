package com.example.owner.skymood

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.owner.skymood.adapters.CustomPagerAdapter
import com.example.owner.skymood.asyncTasks.GetMoreInfoTask
import com.example.owner.skymood.asyncTasks.GetWeeklyTask
import com.example.owner.skymood.databinding.ActivityMainBinding
import com.example.owner.skymood.fragments.current.CurrentWeatherFragment
import com.example.owner.skymood.fragments.hourly.HourlyWeatherFragment
import com.example.owner.skymood.fragments.ICommunicator
import com.example.owner.skymood.fragments.MoreInfoFragment
import com.example.owner.skymood.model.SearchedLocation
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity(), ICommunicator {
    private lateinit var binding: ActivityMainBinding

    lateinit var toolbar: Toolbar
        private set
    private lateinit var adapter: CustomPagerAdapter
    private val handler: Handler = Handler()
    private var lastClick: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (binding.activityMainViewPager.currentItem == CURRENT_WEATHER_FRAGMENT_INDEX) {
                    onBack()
                } else {
                    binding.activityMainViewPager.setCurrentItem(binding.activityMainViewPager.currentItem - 1)
                }
            }
        })

        //setting view_toolbar
        toolbar = binding.mainActivityViewToolBar.root as Toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        //setting view pager adapter
        adapter = CustomPagerAdapter(supportFragmentManager)
        binding.activityMainViewPager.setOffscreenPageLimit(NUMBER_OF_PAGES)
        binding.activityMainViewPager.setAdapter(adapter)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun setInfo(city: String?, code: String?, min: String?, max: String?, date: String?) {
        val fragment = adapter.getItem(HOURLY_WEATHER_FRAGMENT_INDEX) as HourlyWeatherFragment

        // start get weekly task
        val getWeek = GetWeeklyTask(this, fragment, fragment.weeklyWeatherArray)
        getWeek.execute(city, code)

        // third fragment
        val moreInfoFragment = adapter.getItem(MORE_INFO_FRAGMENT_INDEX) as MoreInfoFragment
        moreInfoFragment.setExternalInfo(city, code, date, min, max)

        //start get more info
        val infoTask = GetMoreInfoTask(moreInfoFragment)
        infoTask.execute(city, code)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val itemId = item.itemId
        return when (itemId) {
            R.id.menu_main_item_sky_mood -> {
                //do nothing - we are already in this activity
                true
            }

            R.id.menu_main_item_searched_locations -> {
                val searchedLocationsActivity = Intent(this, SearchedLocationsActivity::class.java)
                startActivityForResult(searchedLocationsActivity, REQUEST_CODE_SEARCHED_LOCATIONS)
                true
            }

            R.id.menu_main_item_my_locations -> {
                val myLocationsActivity = Intent(this, MyLocationsActivity::class.java)
                startActivity(myLocationsActivity)
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_SEARCHED_LOCATIONS && resultCode == RESULT_OK) {
            onSearchedLocationResult(data)
        }
    }

    override fun onStop() {
        super.onStop()
        handler.removeCallbacksAndMessages(null)
    }

    val hourlyFragment: HourlyWeatherFragment
        get() = adapter.getItem(HOURLY_WEATHER_FRAGMENT_INDEX) as HourlyWeatherFragment

    fun changeBackground(partOfDay: String) {
        val isNight = partOfDay == NIGHT
        val background = if (isNight) R.drawable.background_night else R.drawable.background_day
        binding.activityMainContainer.setBackgroundResource(background)
    }

    private fun onBack() {
        val currentClick = System.currentTimeMillis()
        val clickInterval = currentClick - lastClick

        if (clickInterval > BACK_BUTTON_MIN_INTERVAL) {
            lastClick = currentClick
            val toast =
                Toast.makeText(this, R.string.lbl_press_back_btn_again_to_exit, Toast.LENGTH_LONG)
            toast.show()

            handler.postDelayed({ toast.cancel() }, BACK_BUTTON_TOAST_DELAY)
        } else {
            this.finish()
            exitProcess(0)
        }
    }

    private fun onSearchedLocationResult(data: Intent?) {
        if (data == null) {
            return
        }

        val city = data.getStringExtra(SearchedLocationsActivity.CITY_DATA_TAG)
        val country = data.getStringExtra(SearchedLocationsActivity.COUNTRY_DATA_TAG)
        val countryCode = data.getStringExtra(SearchedLocationsActivity.COUNTRY_CODE_DATA_TAG)
        val searchedLocation =
            data.getParcelableExtra<SearchedLocation?>(SearchedLocationsActivity.SEARCHED_LOCATION_OBJECT_DATA_TAG)

        val fragment = adapter.getItem(CURRENT_WEATHER_FRAGMENT_INDEX) as CurrentWeatherFragment
        if (fragment.isOnline && city != null) {
            fragment.updateWeatherInfo(city)
        } else if (searchedLocation != null) {
            fragment.setInfoData(
                city,
                country,
                searchedLocation.icon,
                searchedLocation.temp,
                searchedLocation.min,
                searchedLocation.max,
                searchedLocation.condition,
                searchedLocation.feelsLike,
                searchedLocation.lastUpdate
            )
        } else {
            Toast.makeText(this, R.string.lbl_something_went_wrong, Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        const val REQUEST_CODE_SEARCHED_LOCATIONS: Int = 5
        const val NUMBER_OF_PAGES: Int = 3
        const val DAY: String = "day"
        const val NIGHT: String = "night"
        private const val CURRENT_WEATHER_FRAGMENT_INDEX = 0
        private const val HOURLY_WEATHER_FRAGMENT_INDEX = 1
        private const val MORE_INFO_FRAGMENT_INDEX = 2
        private const val BACK_BUTTON_MIN_INTERVAL = 1000L
        private const val BACK_BUTTON_TOAST_DELAY = 1200L
    }
}
