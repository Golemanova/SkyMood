package com.example.owner.skymood;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.owner.skymood.adapters.CustomPagerAdapter;
import com.example.owner.skymood.asyncTasks.APIDataGetterAsyncTask;
import com.example.owner.skymood.asyncTasks.GetHourlyTask;
import com.example.owner.skymood.asyncTasks.GetMoreInfoTask;
import com.example.owner.skymood.asyncTasks.GetWeeklyTask;
import com.example.owner.skymood.fragments.CurrentWeatherFragment;
import com.example.owner.skymood.fragments.HourlyWeatherFragment;
import com.example.owner.skymood.fragments.ICommunicator;
import com.example.owner.skymood.fragments.MoreInfoFragment;
import com.example.owner.skymood.model.SearchedLocation;

public class MainActivity extends AppCompatActivity implements ICommunicator {

    public static final int REQUEST_CODE_SEARCHED_LOCATIONS = 5;
    public static final int NUMBER_OF_PAGES = 3;
    public static final String DAY = "day";
    public static final String NIGHT = "night";
    private static final int CURRENT_WEATHER_FRAGMENT_INDEX = 0;
    private static final int HOURLY_WEATHER_FRAGMENT_INDEX = 1;
    private static final int MORE_INFO_FRAGMENT_INDEX = 2;
    private static final long BACK_BUTTON_MIN_INTERVAL = 1000L;
    private static final long BACK_BUTTON_TOAST_DELAY = 1200L;

    private ViewPager pager;
    private Toolbar toolbar;
    private LinearLayout layout;
    private CustomPagerAdapter adapter;
    private Handler handler = new Handler();
    private long lastClick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //used for changing the background
        layout = (LinearLayout) findViewById(R.id.activity_main_container);

        //setting view_toolbar
        toolbar = (Toolbar) findViewById(R.id.main_activity_view_tool_bar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
        }

        //setting view pager adapter
        adapter = new CustomPagerAdapter(getSupportFragmentManager(), this);
        pager = (ViewPager) findViewById(R.id.activity_main_view_pager);
        pager.setOffscreenPageLimit(NUMBER_OF_PAGES);
        pager.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onBackPressed() {

        if (pager.getCurrentItem() == CURRENT_WEATHER_FRAGMENT_INDEX) {
            onBack();
        } else {
            pager.setCurrentItem(pager.getCurrentItem() - 1);
        }
    }

    @Override
    public void setInfo(String city, String code, String min, String max, String date) {

        HourlyWeatherFragment fragment = (HourlyWeatherFragment) adapter.getItem(HOURLY_WEATHER_FRAGMENT_INDEX);

        //start get hourly task
        GetHourlyTask getHour = new GetHourlyTask(this, fragment, fragment.getHourlyWeatherArray());
        getHour.execute(city, code);

        // start get weekly task
        GetWeeklyTask getWeek = new GetWeeklyTask(this, fragment, fragment.getWeeklyWeatherArray());
        getWeek.execute(city, code);

        // third fragment
        MoreInfoFragment moreInfoFragment = (MoreInfoFragment) adapter.getItem(MORE_INFO_FRAGMENT_INDEX);
        moreInfoFragment.setExternalInfo(city, code, date, min, max);

        //start get more info
        GetMoreInfoTask infoTask = new GetMoreInfoTask(this, moreInfoFragment);
        infoTask.execute(city, code);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int itemId = item.getItemId();
        switch (itemId) {
            case R.id.menu_main_item_sky_mood:
                //do nothing - we are already in this activity
                return true;
            case R.id.menu_main_item_searched_locations:
                Intent searchedLocationsActivity = new Intent(this, SearchedLocationsActivity.class);
                startActivityForResult(searchedLocationsActivity, REQUEST_CODE_SEARCHED_LOCATIONS);
                return true;
            case R.id.menu_main_item_my_locations:
                Intent myLocationsActivity = new Intent(this, MyLocationsActivity.class);
                startActivity(myLocationsActivity);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_CODE_SEARCHED_LOCATIONS && resultCode == Activity.RESULT_OK) {
            onSearchedLocationResult(data);
        }
    }

    @Override
    protected void onStop() {

        super.onStop();
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
    }

    public HourlyWeatherFragment getHourlyFragment() {

        return (HourlyWeatherFragment) adapter.getItem(HOURLY_WEATHER_FRAGMENT_INDEX);
    }

    public Toolbar getToolbar() {

        return this.toolbar;
    }

    public void changeBackground(String partOfDay) {

        if (partOfDay.equals(DAY)) {
            layout.setBackgroundResource(R.drawable.background_day);
        } else if (partOfDay.equals(NIGHT)) {
            layout.setBackgroundResource(R.drawable.background_night);
        }
    }

    private void onBack() {

        long currentClick = System.currentTimeMillis();
        long clickInterval = currentClick - lastClick;

        if (clickInterval > BACK_BUTTON_MIN_INTERVAL) {
            lastClick = currentClick;
            final Toast toast = Toast.makeText(this, R.string.lbl_press_back_btn_again_to_exit, Toast.LENGTH_LONG);
            toast.show();

            handler.postDelayed(new Runnable() {

                @Override
                public void run() {

                    toast.cancel();
                }
            }, BACK_BUTTON_TOAST_DELAY);
        } else {
            this.finish();
            System.exit(0);
        }
    }

    private void onSearchedLocationResult(Intent data) {

        if (data == null) {
            return;
        }

        String city = data.getStringExtra(SearchedLocationsActivity.CITY_DATA_TAG);
        String country = data.getStringExtra(SearchedLocationsActivity.COUNTRY_DATA_TAG);
        String countryCode = data.getStringExtra(SearchedLocationsActivity.COUNTRY_CODE_DATA_TAG);
        SearchedLocation searchedLocation = data.getParcelableExtra(SearchedLocationsActivity.SEARCHED_LOCATION_OBJECT_DATA_TAG);

        CurrentWeatherFragment fragment = (CurrentWeatherFragment) adapter.getItem(CURRENT_WEATHER_FRAGMENT_INDEX);
        if (fragment.isOnline()) {
            ImageView weatherImage = fragment.getWeatherImage();
            APIDataGetterAsyncTask task = new APIDataGetterAsyncTask(fragment, this, weatherImage);
            task.execute(countryCode, city, country);
        } else if (searchedLocation != null) {
            fragment.setInfoData(city, country, searchedLocation.getIcon(), searchedLocation.getTemp(),
                    searchedLocation.getMin(), searchedLocation.getMax(), searchedLocation.getCondition(),
                    searchedLocation.getFeelsLike(), searchedLocation.getLastUpdate());
        } else {
            Toast.makeText(this, R.string.lbl_something_went_wrong, Toast.LENGTH_SHORT).show();
        }
    }
}
