package com.example.owner.skymood;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.LinearLayout;

import com.example.owner.skymood.adapters.CustomPagerAdapter;
import com.example.owner.skymood.asyncTasks.APIDataGetterAsyncTask;
import com.example.owner.skymood.asyncTasks.GetHourlyTask;
import com.example.owner.skymood.asyncTasks.GetMoreInfoTask;
import com.example.owner.skymood.asyncTasks.GetWeeklyTask;
import com.example.owner.skymood.fragments.CurrentWeatherFragment;
import com.example.owner.skymood.fragments.HourlyWeatherFragment;
import com.example.owner.skymood.fragments.ICommunicatior;
import com.example.owner.skymood.fragments.MoreInfoFragment;
import com.example.owner.skymood.model.SearchedLocation;

public class SwipeViewActivity extends AppCompatActivity implements ICommunicatior{

    public static final int NUMBER_OF_PAGES = 3;
    public static final String DAY = "day";
    public static final String NIGHT = "night";
    public static final int REQUEST_CODE_SEARCHED_LOCATIONS = 5;

    CustomPagerAdapter adapter;
    ViewPager pager;
    Toolbar toolbar;

    private LinearLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe_view);

        //used for changing the background
        layout = (LinearLayout) findViewById(R.id.swipe_view_activity);

        //setting toolbar
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        //setting pager adapter
        adapter = new CustomPagerAdapter(getSupportFragmentManager(), this);
        pager = (ViewPager) findViewById(R.id.pager);
        pager.setOffscreenPageLimit(3);
        pager.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (pager.getCurrentItem() == 0) {
            super.onBackPressed();
        } else {
            pager.setCurrentItem(pager.getCurrentItem() - 1);
        }

    }

    @Override
    public void setInfo(String city, String code, String min, String max, String date) {
        HourlyWeatherFragment fragment = (HourlyWeatherFragment) adapter.getItem(1);

        //start get hourly task
        GetHourlyTask getHour = new GetHourlyTask(this, fragment, fragment.getHourlyWeatherArray());
        getHour.execute(city, code);

        // start get weekly task
        GetWeeklyTask getWeek = new GetWeeklyTask(this, fragment, fragment.getWeeklyWeatherArray());
        getWeek.execute(city, code);

        // thirt fragment
        android.support.v4.app.Fragment fragment2 = adapter.getItem(2);
        ((MoreInfoFragment)fragment2).setExternalInfo(city, code, date, min, max);
        GetMoreInfoTask infoTask = new GetMoreInfoTask(this, fragment2);
        infoTask.execute(city, code);
    }

    public  HourlyWeatherFragment getHourlyFragment(){
        return (HourlyWeatherFragment)adapter.getItem(1);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = null;
        switch (item.getItemId()) {
            case R.id.sky_mood:
                return true;
            case R.id.searched_locations:
                intent = new Intent(this, SearchedLocationsActivity.class);
                startActivityForResult(intent, REQUEST_CODE_SEARCHED_LOCATIONS);
                startActivity(intent);
                return true;
            case R.id.my_locations:
                intent = new Intent(this, MyLocationsActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_SEARCHED_LOCATIONS) {
            if(resultCode == Activity.RESULT_OK){
                String city = data.getStringExtra(SearchedLocationsActivity.CITY);
                String country = data.getStringExtra(SearchedLocationsActivity.COUNTRY);
                String countryCode = data.getStringExtra(SearchedLocationsActivity.COUNTRY_CODE);
                SearchedLocation object = data.getParcelableExtra(SearchedLocationsActivity.SEARCHED_LOCATION_OBJECT);

                CurrentWeatherFragment fragment = (CurrentWeatherFragment)adapter.getItem(0);
                if(fragment.isOnline()) {
                    APIDataGetterAsyncTask task = new APIDataGetterAsyncTask(fragment, this, fragment.getWeatherImage());
                    task.execute(countryCode, city, country);
                } else {
                    fragment.setInfoData(city, country, object.getIcon(), object.getTemp(), object.getMin(), object.getMax(),
                            object.getCondition(), object.getFeelsLike(), object.getLastUpdate());
                }
            }
        }
    }

    public Toolbar getToolbar(){
        return this.toolbar;
    }


    public void changeBackground(String partOfDay){
        if(partOfDay == DAY){
            layout.setBackgroundResource(R.drawable.backgr_day);
        } else if(partOfDay == NIGHT){
            layout.setBackgroundResource(R.drawable.night_backgr);
        }
    }

}
