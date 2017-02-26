package com.example.owner.skymood;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.owner.skymood.model.SearchedLocation;
import com.example.owner.skymood.model.SearchedLocationManager;

import java.util.ArrayList;

public class SearchedLocationsActivity extends AppCompatActivity implements View.OnClickListener{

    public static final String CITY = "city";
    public static final String COUNTRY = "fragment_current_weather_tv_country";
    public static final String COUNTRY_CODE = "countryCode";
    public static final String SEARCHED_LOCATION_OBJECT = "SearchedLocation object";
    private Button location1;
    private Button location2;
    private Button location3;
    private Button location4;
    private Button location5;
    private ArrayList<SearchedLocation> locations;
    private SearchedLocationManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searched_locations);

        manager = SearchedLocationManager.getInstance(this);
        locations = manager.getAllSearchedLocations();
        //setting view_toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_searched_locations_view_tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        int[] buttonIDs = new int[] {R.id.activity_searched_locations_btn_location_one,
                R.id.activity_searched_locations_btn_location_two,
                R.id.activity_searched_locations_btn_location_three,
                R.id.activity_searched_locations_btn_location_four,
                R.id.activity_searched_locations_btn_location_five };
        //initializing buttons
        for(int i=0; i<locations.size(); i++){
            Button location = (Button) findViewById(buttonIDs[i]);
            SearchedLocation loc = locations.get(i);
            location.setVisibility(View.VISIBLE);
            location.setText(loc.getCity() + ", " + loc.getCountry());
            location.setTag(loc);
        }
        location1 = (Button) findViewById(R.id.activity_searched_locations_btn_location_one);
        location1.setOnClickListener(this);
        location2 = (Button) findViewById(R.id.activity_searched_locations_btn_location_two);
        location2.setOnClickListener(this);
        location3 = (Button) findViewById(R.id.activity_searched_locations_btn_location_three);
        location3.setOnClickListener(this);
        location4 = (Button) findViewById(R.id.activity_searched_locations_btn_location_four);
        location4.setOnClickListener(this);
        location5 = (Button) findViewById(R.id.activity_searched_locations_btn_location_five);
        location5.setOnClickListener(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = null;
        switch (item.getItemId()) {
            case R.id.menu_main_item_sky_mood:
                intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                return true;
            case R.id.menu_main_item_searched_locations:
                return true;
            case R.id.menu_main_item_my_locations:
                intent = new Intent(this, MyLocationsActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View v) {
        SearchedLocation object = (SearchedLocation) v.getTag();
        String city = object.getCity();
        String country = object.getCountry();
        String countryCode = object.getCode();

        Intent returnIntent = new Intent();
        returnIntent.putExtra(CITY, city);
        returnIntent.putExtra(COUNTRY, country);
        returnIntent.putExtra(COUNTRY_CODE, countryCode);
        returnIntent.putExtra(SEARCHED_LOCATION_OBJECT, object);

        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }

}
