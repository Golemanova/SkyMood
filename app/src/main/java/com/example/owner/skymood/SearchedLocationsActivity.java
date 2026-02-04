package com.example.owner.skymood;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.owner.skymood.model.SearchedLocation;
import com.example.owner.skymood.model.SearchedLocationManager;

import java.util.ArrayList;

public class SearchedLocationsActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String CITY_DATA_TAG = "city_data_tag";
    public static final String COUNTRY_DATA_TAG = "country_data_tag";
    public static final String COUNTRY_CODE_DATA_TAG = "country_code_data_tag";
    public static final String SEARCHED_LOCATION_OBJECT_DATA_TAG = "searched_location_object_data_tag";
    private static final int LOCATION_BUTTONS_NUMBER = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searched_locations);

        SearchedLocationManager manager = SearchedLocationManager.getInstance(this);
        ArrayList<SearchedLocation> locations = manager.getAllSearchedLocations();

        //setting toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_searched_locations_view_tool_bar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
        }

        //initializing buttons
        initializeLocationButtons(locations);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int itemId = item.getItemId();
        if (itemId == R.id.menu_main_item_sky_mood) {
            Intent mainActivity = new Intent(this, MainActivity.class);
            startActivity(mainActivity);
            return true;
        } else if (itemId == R.id.menu_main_item_searched_locations) {
            //do nothing = we are already in this activity
            return true;
        } else if (itemId == R.id.menu_main_item_my_locations) {
            Intent myLocationsActivity = new Intent(this, MyLocationsActivity.class);
            startActivity(myLocationsActivity);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View locationButton) {

        SearchedLocation location = (SearchedLocation) locationButton.getTag();
        String city = location.getCity();
        String country = location.getCountry();
        String countryCode = location.getCode();

        Intent returnIntent = new Intent();
        returnIntent.putExtra(CITY_DATA_TAG, city);
        returnIntent.putExtra(COUNTRY_DATA_TAG, country);
        returnIntent.putExtra(COUNTRY_CODE_DATA_TAG, countryCode);
        returnIntent.putExtra(SEARCHED_LOCATION_OBJECT_DATA_TAG, location);

        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }

    private void initializeLocationButtons(ArrayList<SearchedLocation> locations) {

        ArrayList<Button> locationButtons = getLocationButtonsAndSetOnClickListener();

        if (locations == null || locations.size() <= 0
                || locationButtons == null || locationButtons.size() <= 0) {
            return;
        }
        for (int i = 0; i < locations.size() && i < locationButtons.size(); i++) {
            Button location = locationButtons.get(i);
            SearchedLocation searchedLocation = locations.get(i);
            location.setVisibility(View.VISIBLE);
            location.setText(searchedLocation.getCity() + ", " + searchedLocation.getCountry());
            location.setTag(searchedLocation);
        }
    }

    private ArrayList<Button> getLocationButtonsAndSetOnClickListener() {

        ArrayList<Button> locationButtons = new ArrayList<>(LOCATION_BUTTONS_NUMBER);
        setUpLocationButton(locationButtons, R.id.activity_searched_locations_btn_location_one);
        setUpLocationButton(locationButtons, R.id.activity_searched_locations_btn_location_two);
        setUpLocationButton(locationButtons, R.id.activity_searched_locations_btn_location_three);
        setUpLocationButton(locationButtons, R.id.activity_searched_locations_btn_location_four);
        setUpLocationButton(locationButtons, R.id.activity_searched_locations_btn_location_five);

        return locationButtons;
    }

    private void setUpLocationButton(ArrayList<Button> locationButtons, int buttonId) {

        Button locationBtn = (Button) findViewById(buttonId);
        locationBtn.setOnClickListener(this);
        locationButtons.add(locationBtn);
    }
}
