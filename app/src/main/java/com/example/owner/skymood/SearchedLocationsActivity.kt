package com.example.owner.skymood

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.owner.skymood.model.SearchedLocation
import com.example.owner.skymood.model.SearchedLocationManager

class SearchedLocationsActivity : AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_searched_locations)

        val manager = SearchedLocationManager.getInstance(this)
        val locations = manager.allSearchedLocations

        //setting toolbar
        val toolbar =
            findViewById<View?>(R.id.activity_searched_locations_view_tool_bar) as Toolbar?
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        //initializing buttons
        initializeLocationButtons(locations)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.menu_main_item_sky_mood -> {
            val mainActivity = Intent(this, MainActivity::class.java)
            startActivity(mainActivity)
            true
        }

        R.id.menu_main_item_searched_locations -> {
            //do nothing = we are already in this activity
            true
        }

        R.id.menu_main_item_my_locations -> {
            val myLocationsActivity = Intent(this, MyLocationsActivity::class.java)
            startActivity(myLocationsActivity)
            true
        }

        else -> super.onOptionsItemSelected(item)
    }

    override fun onClick(locationButton: View) {
        val location = locationButton.tag as SearchedLocation
        val city = location.city
        val country = location.country
        val countryCode = location.code

        val returnIntent = Intent()
        returnIntent.putExtra(CITY_DATA_TAG, city)
        returnIntent.putExtra(COUNTRY_DATA_TAG, country)
        returnIntent.putExtra(COUNTRY_CODE_DATA_TAG, countryCode)
        returnIntent.putExtra(SEARCHED_LOCATION_OBJECT_DATA_TAG, location)

        setResult(RESULT_OK, returnIntent)
        finish()
    }

    private fun initializeLocationButtons(locations: java.util.ArrayList<SearchedLocation>?) {
        val locationButtons = this.locationButtonsAndSetOnClickListener

        if (locations == null || locations.isEmpty() || locationButtons.isEmpty()) {
            return
        }

        locationButtons.forEachIndexed { i, _ ->
            val location = locationButtons[i]
            val searchedLocation = locations[i]
            location.visibility = View.VISIBLE
            location.text = searchedLocation.city + ", " + searchedLocation.country
            location.tag = searchedLocation
        }
    }

    private val locationButtonsAndSetOnClickListener: ArrayList<Button>
        get() {
            val locationButtons: java.util.ArrayList<Button> =
                java.util.ArrayList<Button>(LOCATION_BUTTONS_NUMBER)
            setUpLocationButton(locationButtons, R.id.activity_searched_locations_btn_location_one)
            setUpLocationButton(locationButtons, R.id.activity_searched_locations_btn_location_two)
            setUpLocationButton(
                locationButtons,
                R.id.activity_searched_locations_btn_location_three
            )
            setUpLocationButton(locationButtons, R.id.activity_searched_locations_btn_location_four)
            setUpLocationButton(locationButtons, R.id.activity_searched_locations_btn_location_five)

            return locationButtons
        }

    private fun setUpLocationButton(locationButtons: java.util.ArrayList<Button>, buttonId: Int) {
        val locationBtn = findViewById<View?>(buttonId) as Button
        locationBtn.setOnClickListener(this)
        locationButtons.add(locationBtn)
    }

    companion object {
        const val CITY_DATA_TAG: String = "city_data_tag"
        const val COUNTRY_DATA_TAG: String = "country_data_tag"
        const val COUNTRY_CODE_DATA_TAG: String = "country_code_data_tag"
        const val SEARCHED_LOCATION_OBJECT_DATA_TAG: String = "searched_location_object_data_tag"
        private const val LOCATION_BUTTONS_NUMBER = 5
    }
}
