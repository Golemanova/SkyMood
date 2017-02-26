package com.example.owner.skymood.fragments;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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

import com.example.owner.skymood.R;
import com.example.owner.skymood.SwipeViewActivity;
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


public class CurrentWeatherFragment extends Fragment implements Swideable {

    public static final String API_KEY =  "9226ced37cb70c78";
    public static final String API_KEY_TWO =  "f340bd0448a4dba2";

    private ProgressBar progressBar;
    private TextView chosenCityTextView;
    private Spinner spinner;
    private ImageView syncButton;
    private ImageView locationSearchButton;
    private ImageView citySearchButton;
    private AutoCompleteTextView writeCityEditText;
    private TextView temperature;
    private TextView condition;
    private TextView feelsLike;
    private TextView lastUpdate;
    private TextView countryTextView;
    private TextView minTempTextView;
    private TextView maxTempTextView;
    private ImageView weatherImage;
    private ImageView addImage;

    private String city;
    private String country;
    private String countryCode;
    private String cityToDisplay;
    private String minTemp;
    private String maxTemp;
    private String dateAndTime;
    private HashMap<String, String> cities;
    private ArrayList<String> autoCopleteNames;
    private ArrayAdapter adapterAutoComplete;
    ArrayList<String> citiesSpinner;

    private InputMethodManager keyboard;
    private Context context;
    private LocationPreference locPref;
    private MyLocationManager manager;

    public CurrentWeatherFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_current_weather, container, false);

        //initializing components
        syncButton = (ImageView) rootView.findViewById(R.id.synchronize);
        locationSearchButton = (ImageView) rootView.findViewById(R.id.gpsSearch);
        citySearchButton = (ImageView) rootView.findViewById(R.id.citySearch);
        writeCityEditText = (AutoCompleteTextView) rootView.findViewById(R.id.writeCityEditText);
        writeCityEditText.setThreshold(3);
        temperature = (TextView) rootView.findViewById(R.id.temperatureTextView);
        countryTextView = (TextView) rootView.findViewById(R.id.country);
        condition = (TextView) rootView.findViewById(R.id.conditionTextView);
        minTempTextView = (TextView) rootView.findViewById(R.id.minTemp);
        maxTempTextView = (TextView) rootView.findViewById(R.id.maxTemp);
        feelsLike = (TextView) rootView.findViewById(R.id.feelsLikeTextView);
        lastUpdate = (TextView) rootView.findViewById(R.id.lastUpdateTextView);
        weatherImage = (ImageView) rootView.findViewById(R.id.weatherImageView);
        chosenCityTextView = (TextView) rootView.findViewById(R.id.chosenCity);
        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        spinner = (Spinner) rootView.findViewById(R.id.locationSpinner);
        Toolbar toolbar = ((SwipeViewActivity)context).getToolbar();
        addImage = (ImageView) toolbar.findViewById(R.id.add_favourite1);
        cities = new HashMap<>();

        //shared prefs
        locPref = LocationPreference.getInstance(context);
        manager = MyLocationManager.getInstance(context);

        //setting background
        setBackground();

        //setting adapter to spinner
        citiesSpinner =  new ArrayList<>();
        citiesSpinner.add("My Locations");
        citiesSpinner.addAll(manager.getAllStringLocations());
        final ArrayAdapter adapter = new ArrayAdapter(context, R.layout.custom_spinner, citiesSpinner);
        spinner.setAdapter(adapter);

        //listeners
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!((String) parent.getItemAtPosition(position)).equals("My Locations")) {
                    if (isOnline()) {
                        String[] location = ((String) parent.getItemAtPosition(position)).split(",");
                        setCity(location[0]);
                        country = location[1].trim();
                        //countryCode from  DB
                        APIDataGetterAsyncTask task = new APIDataGetterAsyncTask(CurrentWeatherFragment.this, context, weatherImage);
                        countryCode = manager.selectCuntryCode(city, country);
                        task.execute(countryCode, city, country);
                    } else {
                        Toast.makeText(context, "NO INTERNET CONNECTION", Toast.LENGTH_SHORT).show();
                    }
                }
                spinner.setSelection(0);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

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
                }else if(writeCityEditText.getText().toString().equals("")){
                    writeCityEditText.setVisibility(View.GONE);
                    keyboard.hideSoftInputFromWindow(writeCityEditText.getWindowToken(), 0);
                    changeVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(getContext(), "You must specify a country", Toast.LENGTH_SHORT).show();
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
                if(chars >= 3){
                    AutoCompleteStringFillerAsyncTask filler = new AutoCompleteStringFillerAsyncTask(CurrentWeatherFragment.this, context);
                    filler.execute(writeCityEditText.getText().toString());
                }
            }
        });

        //logic
        if(isOnline()){
            APIDataGetterAsyncTask task = new APIDataGetterAsyncTask(this, context, weatherImage);
            HourlyWeatherFragment fr = ((SwipeViewActivity)context).getHourlyFragment();
            GetHourlyTask hourTask = new GetHourlyTask(context, fr, fr.getHourlyWeatherArray());
            GetWeeklyTask weeklyTask = new GetWeeklyTask(context, fr, fr.getWeeklyWeatherArray());

            //first: check shared prefs
            if(locPref.isSetLocation()){
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
            if(locPref.isSetLocation() && !locPref.hasNull()){
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
                if(city != null && country != null) {
                    MyLocation myloc = new MyLocation(city, countryCode, country, city + ", " + country);
                    if (manager.selectMyLocation(myloc) == null) {
                        manager.insertMyLocation(myloc);
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

    public void getWeatherInfoFromSharedPref(){
        chosenCityTextView.setVisibility(View.VISIBLE);
        chosenCityTextView.setText(locPref.getCity());
        countryTextView.setText(country);
        temperature.setText(locPref.getTemperature() + "°");
        minTempTextView.setText("⬇" + locPref.getMinTemp() + "°");
        maxTempTextView.setText("⬆" + locPref.getMaxTemp() + "°");
        condition.setText(locPref.getCondition());
        feelsLike.setText(locPref.getFeelsLike());
        lastUpdate.setText(locPref.getLastUpdate());

        if(locPref.getIcon().contains("night")){
            ((SwipeViewActivity)context).changeBackground(SwipeViewActivity.NIGHT);
        } else {
            ((SwipeViewActivity)context).changeBackground(SwipeViewActivity.DAY);
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

    public void setCity(String city){
        this.city = city.replace(" ", "_");
        this.city.toLowerCase();
        this.cityToDisplay = city.toUpperCase();
    }

    public void getWeatherInfoByCity(String city){
        if(city != null && !city.isEmpty()) {
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

    public void apiDataGetterAsyncTaskOnPreExecute(){
        chosenCityTextView.setVisibility(View.GONE);
        countryTextView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
    }

    public void apiDataGetterAsyncTaskOnPostExecute(String temp, String condition, String feelsLike,
                                    String minTemp, String maxTemp, String dateAndTime, String lastUpdate, String cityToDisplay, String country){
        this.progressBar.setVisibility(View.GONE);
        this.chosenCityTextView.setVisibility(View.VISIBLE);
        this.chosenCityTextView.setText(cityToDisplay);
        this.countryTextView.setVisibility(View.VISIBLE);
        this.countryTextView.setText(country);
        this.addImage.setVisibility(View.VISIBLE);

        if(temp != null) {
            this.temperature.setText(temp + "°");
            this.condition.setText(condition);
            this.feelsLike.setText(feelsLike);
            this.minTempTextView.setText("⬇" + minTemp + "°");
            this.minTemp = minTemp;
            this.maxTempTextView.setText("⬆" + maxTemp + "°");
            this.maxTemp = maxTemp;
            this.lastUpdate.setText(lastUpdate);
            this.dateAndTime = dateAndTime;


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

    public void autoCompleteStringFillerAsyncTaskOnPostExecute(ArrayAdapter adapterAutoComplete){
        this.writeCityEditText.setAdapter(adapterAutoComplete);
        this.adapterAutoComplete = adapterAutoComplete;
    }

    public void setCities(HashMap<String, String> cities){
        this.cities = cities;
    }

    public void findLocation(){
        FindLocationAsyncTask findLocation = new FindLocationAsyncTask(this, context, weatherImage);
        findLocation.execute();
    }

    public void changeVisibility(int visibility){
        spinner.setVisibility(visibility);
        syncButton.setVisibility(visibility);
        locationSearchButton.setVisibility(visibility);
        weatherImage.setAdjustViewBounds(true);
    }

    private void setBackground(){
        int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        if (hour >= 6 && hour <= 19) {
            ((SwipeViewActivity)context).changeBackground(SwipeViewActivity.DAY);
        } else {
            ((SwipeViewActivity)context).changeBackground(SwipeViewActivity.NIGHT);
        }
    }

    public ImageView getWeatherImage(){
        return this.weatherImage;
    }

    public void setInfoData(String city, String country, String icon, String temp, String minTemp, String maxTemp,
                            String condition, String feelsLike, String lastUpdate){

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

