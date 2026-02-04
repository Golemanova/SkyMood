package com.example.owner.skymood.fragments;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.example.owner.skymood.MainActivity;
import com.example.owner.skymood.R;
import com.example.owner.skymood.asyncTasks.APIDataGetterAsyncTask;
import com.example.owner.skymood.asyncTasks.AutoCompleteStringFillerAsyncTask;
import com.example.owner.skymood.asyncTasks.FindLocationAsyncTask;
import com.example.owner.skymood.asyncTasks.GetHourlyTask;
import com.example.owner.skymood.asyncTasks.GetWeeklyTask;
import com.example.owner.skymood.model.LocationPreference;
import com.example.owner.skymood.model.MyLocation;
import com.example.owner.skymood.model.MyLocationManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;


public class CurrentWeatherFragment extends Fragment implements Slidable {

    public static final String API_KEY = "9226ced37cb70c78";
    public static final String API_KEY_TWO = "f340bd0448a4dba2";

    private static final int MORNING_HOUR = 6;
    private static final int NIGHT_HOUR = 19;

    private TextView temperature;
    private TextView condition;
    private TextView feelsLike;
    private TextView lastUpdate;
    private TextView countryTextView;
    private TextView minTempTextView;
    private TextView maxTempTextView;
    private ImageView weatherImage;
    private ImageView addImage;
    private ImageView syncButton;
    private ImageView locationSearchButton;
    private ImageView citySearchButton;
    private AutoCompleteTextView writeCityEditText;
    private ProgressBar progressBar;
    private TextView chosenCityTextView;
    private Spinner spinner;

    private String city;
    private String country;
    private String countryCode;
    private HashMap<String, String> cities;
    private ArrayList<String> citiesSpinner;

    private Context context;
    private InputMethodManager keyboard;
    private LocationPreference locPref;
    private MyLocationManager manager;

    public CurrentWeatherFragment() {
        // Required empty public constructor
    }


    private void initViews(ViewGroup rootView) {

        syncButton = (ImageView) rootView.findViewById(R.id.fragment_current_weather_iv_sync);
        locationSearchButton = (ImageView) rootView.findViewById(R.id.fragment_current_weather_iv_gps_search);
        citySearchButton = (ImageView) rootView.findViewById(R.id.fragment_current_weather_iv_city_search);
        writeCityEditText = (AutoCompleteTextView) rootView.findViewById(R.id.fragment_current_weather_actv_search_city);
        temperature = (TextView) rootView.findViewById(R.id.fragment_current_weather_tv_temperature);
        countryTextView = (TextView) rootView.findViewById(R.id.fragment_current_weather_tv_country);
        condition = (TextView) rootView.findViewById(R.id.fragment_current_weather_tv_condition);
        minTempTextView = (TextView) rootView.findViewById(R.id.fragment_current_weather_tv_min_temp);
        maxTempTextView = (TextView) rootView.findViewById(R.id.fragment_current_weather_tv_max_temp);
        feelsLike = (TextView) rootView.findViewById(R.id.fragment_current_weather_tv_feels_like);
        lastUpdate = (TextView) rootView.findViewById(R.id.fragment_current_weather_tv_last_update);
        weatherImage = (ImageView) rootView.findViewById(R.id.fragment_current_weather_iv_weather_state);
        chosenCityTextView = (TextView) rootView.findViewById(R.id.fragment_current_weather_tv_chosen_city);
        progressBar = (ProgressBar) rootView.findViewById(R.id.fragment_current_weather_view_progress_bar);
        spinner = (Spinner) rootView.findViewById(R.id.fragment_current_weather_view_spinner_location);

        Toolbar toolbar = ((MainActivity) context).getToolbar();
        addImage = (ImageView) toolbar.findViewById(R.id.view_toolbar_iv_add_favourite);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_current_weather, container, false);

        //initializing components
        initViews(rootView);

        cities = new HashMap<>();

        //shared prefs
        locPref = LocationPreference.getInstance(context);
        manager = MyLocationManager.getInstance(context);

        //setting background
        setBackground();

        //setting adapter to spinner
        citiesSpinner = new ArrayList<>();
        citiesSpinner.add("My Locations");
        citiesSpinner.add("Sofia");
        citiesSpinner.add("Burgas");
        citiesSpinner.add("Varna");
        citiesSpinner.add("Plovdiv");
        citiesSpinner.addAll(manager.getAllStringLocations());
        final ArrayAdapter adapter = new ArrayAdapter(context, R.layout.view_spinner, citiesSpinner);
        spinner.setAdapter(adapter);
        spinner.setSelection(1);

//        //listeners
//        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//
////                if (!(parent.getItemAtPosition(position)).equals("My Locations")) {
////                    if (isOnline()) {
////                        String[] location = ((String) parent.getItemAtPosition(position)).split(",");
////                        setCity(location[0]);
////                        country = location[1].trim();
////                        //countryCode from  DB
////                        APIDataGetterAsyncTask task = new APIDataGetterAsyncTask(CurrentWeatherFragment.this, context, weatherImage);
////                        countryCode = manager.selectCountryCode(city, country);
////                        task.execute(countryCode, city, country);
////                    } else {
////                        Toast.makeText(context, "NO INTERNET CONNECTION", Toast.LENGTH_SHORT).show();
////                    }
////                }
//                spinner.setSelection(0);
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });

        citySearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isOnline()) {
                    if (writeCityEditText.getVisibility() == View.GONE) {
                        changeVisibility(View.GONE);

                        Animation slide = new AnimationUtils().loadAnimation(context, android.R.anim.fade_in);
                        slide.setDuration(1000);
                        writeCityEditText.startAnimation(slide);
                        writeCityEditText.setVisibility(View.VISIBLE);
                        writeCityEditText.setFocusable(true);
                        writeCityEditText.requestFocus();

                        keyboard = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                        keyboard.showSoftInput(writeCityEditText, 0);
                    } else {
                        writeCityEditText.setVisibility(View.GONE);
                        keyboard.hideSoftInputFromWindow(writeCityEditText.getWindowToken(), 0);
                        changeVisibility(View.VISIBLE);
                    }
                } else {
                    Toast.makeText(context, "NO INTERNET CONNECTION", Toast.LENGTH_SHORT).show();
                }
            }
        });

        syncButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isOnline()) {
                    APIDataGetterAsyncTask task = new APIDataGetterAsyncTask(CurrentWeatherFragment.this, context, weatherImage);
                    task.execute(countryCode, city, country);
                } else {
                    Toast.makeText(getContext(), "NO INTERNET CONNECTION", Toast.LENGTH_SHORT).show();
                }
            }
        });

        locationSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isOnline()) {
                    findLocation();
                } else {
                    Toast.makeText(getContext(), "NO INTERNET CONNECTION", Toast.LENGTH_SHORT).show();
                }
            }
        });

        writeCityEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (writeCityEditText != null && !writeCityEditText.getText().toString().isEmpty()
                        && writeCityEditText.getText().toString().contains(",")) {
                    String location = writeCityEditText.getText().toString();
                    countryCode = cities.get(location);
                    String[] parts = location.split(",");
                    String city = parts[0];
                    country = parts[1].trim();
                    getWeatherInfoByCity(city);
                } else if (writeCityEditText.getText().toString().equals("")) {
                    writeCityEditText.setVisibility(View.GONE);
                    keyboard.hideSoftInputFromWindow(writeCityEditText.getWindowToken(), 0);
                    changeVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(getContext(), "You must specify a fragment_current_weather_tv_country", Toast.LENGTH_SHORT).show();
                    writeCityEditText.setVisibility(View.GONE);
                    keyboard.hideSoftInputFromWindow(writeCityEditText.getWindowToken(), 0);
                    changeVisibility(View.VISIBLE);
                }
                keyboard.hideSoftInputFromWindow(writeCityEditText.getWindowToken(), 0);
                return false;
            }
        });

        writeCityEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                int chars = writeCityEditText.getText().toString().length();
                if (chars >= 3) {
                    AutoCompleteStringFillerAsyncTask filler = new AutoCompleteStringFillerAsyncTask(CurrentWeatherFragment.this, context);
                    filler.execute(writeCityEditText.getText().toString());
                }
            }
        });

        //logic
        if (isOnline()) {
            APIDataGetterAsyncTask task = new APIDataGetterAsyncTask(this, context, weatherImage);
            HourlyWeatherFragment fr = ((MainActivity) context).getHourlyFragment();
            GetHourlyTask hourTask = new GetHourlyTask(context, fr, fr.getHourlyWeatherArray());
            GetWeeklyTask weeklyTask = new GetWeeklyTask(context, fr, fr.getWeeklyWeatherArray());

            //first: check shared prefs
            if (locPref.isSetLocation()) {
                setCity(locPref.getCity());
                countryCode = locPref.getCountryCode();
                country = locPref.getCountry();
                task.execute(countryCode, city, country);
                hourTask.execute(city, countryCode);
                weeklyTask.execute(city, countryCode);
            } else {
                //API autoIP
                findLocation();
            }
        } else {
            if (locPref.isSetLocation() && !locPref.hasNull()) {
                Toast.makeText(context, "NO INTERNET CONNECTION\nFor up to date info connect to Internet", Toast.LENGTH_LONG).show();
                setCity(locPref.getCity());
                country = locPref.getCountry();
                countryCode = locPref.getCountryCode();
                getWeatherInfoFromSharedPref();
            } else {
                feelsLike.setText("Please connect to Internet");
            }
        }

        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (city != null && country != null) {
                    MyLocation myLoc = new MyLocation(city, countryCode, country, city + ", " + country);
                    if (manager.selectMyLocation(myLoc) == null) {
                        manager.insertMyLocation(myLoc);
                        citiesSpinner.add(city + ", " + country);
                        adapter.notifyDataSetChanged();
                        Toast.makeText(context, "location inserted to MyLocations", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "location already exists", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        return rootView;
    }// end of onCreate

    @Override
    public void setContext(Context context) {

        this.context = context;
    }

    public void getWeatherInfoFromSharedPref() {

        chosenCityTextView.setVisibility(View.VISIBLE);
        chosenCityTextView.setText(locPref.getCity());
        countryTextView.setText(country);
        temperature.setText(locPref.getTemperature() + "°");
        minTempTextView.setText("⬇" + locPref.getMinTemp() + "°");
        maxTempTextView.setText("⬆" + locPref.getMaxTemp() + "°");
        condition.setText(locPref.getCondition());
        feelsLike.setText(locPref.getFeelsLike());
        lastUpdate.setText(locPref.getLastUpdate());

        if (locPref.getIcon().contains("night")) {
            ((MainActivity) context).changeBackground(MainActivity.NIGHT);
        } else {
            ((MainActivity) context).changeBackground(MainActivity.DAY);
        }
        Context con = weatherImage.getContext();
        weatherImage.setImageResource(context.getResources().getIdentifier(locPref.getIcon(), "drawable", con.getPackageName()));
    }

    public boolean isOnline() {

        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public void setCity(String city) {
        if(city != null) {
            this.city = city.replace(" ", "_");
            this.city = this.city.toLowerCase();
        }
    }

    public void getWeatherInfoByCity(String city) {

        if (city != null && !city.isEmpty()) {
            setCity(city);
            writeCityEditText.setText("");
            writeCityEditText.setVisibility(View.GONE);
            spinner.setVisibility(View.VISIBLE);
            syncButton.setVisibility(View.VISIBLE);
            locationSearchButton.setVisibility(View.VISIBLE);
            APIDataGetterAsyncTask task = new APIDataGetterAsyncTask(this, context, weatherImage);
            task.execute(countryCode, city, country);
        }
    }

    public void apiDataGetterAsyncTaskOnPreExecute() {

        chosenCityTextView.setVisibility(View.GONE);
        countryTextView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
    }

    public void apiDataGetterAsyncTaskOnPostExecute(String temp, String condition, String feelsLike,
                                                    String minTemp, String maxTemp, String dateAndTime, String lastUpdate, String cityToDisplay, String country) {

        this.progressBar.setVisibility(View.GONE);
        this.chosenCityTextView.setVisibility(View.VISIBLE);
        this.chosenCityTextView.setText(cityToDisplay);
        this.countryTextView.setVisibility(View.VISIBLE);
        this.countryTextView.setText(country);
        this.addImage.setVisibility(View.VISIBLE);

        if (temp != null) {
            this.temperature.setText(temp + "°");
            this.condition.setText(condition);
            this.feelsLike.setText(feelsLike);
            this.minTempTextView.setText("⬇" + minTemp + "°");
            this.maxTempTextView.setText("⬆" + maxTemp + "°");
            this.lastUpdate.setText(lastUpdate);

        } else {
            this.temperature.setText("");
            this.condition.setText("");
            this.lastUpdate.setText("");
            this.maxTempTextView.setText("");
            this.minTempTextView.setText("");
            this.feelsLike.setText("Sorry, there is no information.");
            this.lastUpdate.setText("This location does not exist\nor you have weak internet connection");
        }
    }

    public void autoCompleteStringFillerAsyncTaskOnPostExecute(ArrayAdapter adapterAutoComplete) {

        this.writeCityEditText.setAdapter(adapterAutoComplete);
    }

    public void setCities(HashMap<String, String> cities) {

        this.cities = cities;
    }

    public void findLocation() {

        FindLocationAsyncTask findLocation = new FindLocationAsyncTask(this, context, weatherImage);
        findLocation.execute();
    }

    public void changeVisibility(int visibility) {

        spinner.setVisibility(visibility);
        syncButton.setVisibility(visibility);
        locationSearchButton.setVisibility(visibility);
        weatherImage.setAdjustViewBounds(true);
    }

    private void setBackground() {

        int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        boolean isDay = hour >= MORNING_HOUR && hour <= NIGHT_HOUR;
        String partOfDay = isDay ? MainActivity.DAY : MainActivity.NIGHT;
        ((MainActivity) context).changeBackground(partOfDay);
    }

    public ImageView getWeatherImage() {

        return this.weatherImage;
    }

    public void setInfoData(String city, String country, String icon, String temp, String minTemp, String maxTemp,
                            String condition, String feelsLike, String lastUpdate) {

        this.chosenCityTextView.setVisibility(View.VISIBLE);
        this.chosenCityTextView.setText(city);
        this.countryTextView.setText(country);
        this.temperature.setText(temp + "°");
        this.minTempTextView.setText("⬇" + minTemp + "°");
        this.maxTempTextView.setText("⬆" + maxTemp + "°");
        this.condition.setText(condition);
        this.feelsLike.setText(feelsLike);
        this.lastUpdate.setText(lastUpdate);

        Context con = weatherImage.getContext();
        weatherImage.setImageResource(context.getResources().getIdentifier(icon, "drawable", con.getPackageName()));

    }

}
