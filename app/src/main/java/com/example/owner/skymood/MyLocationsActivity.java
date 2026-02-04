package com.example.owner.skymood;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.owner.skymood.adapters.MyCardViewAdapter;
import com.example.owner.skymood.model.MyLocation;
import com.example.owner.skymood.model.MyLocationManager;

import java.util.ArrayList;

public class MyLocationsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_locations);

        MyLocationManager manager = MyLocationManager.getInstance(this);

        //setting the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_my_locations_view_tool_bar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
        }

        //get my locations
        ArrayList<MyLocation> data = manager.getAllMyLocations();

        //create recycler view
        RecyclerView recycler = (RecyclerView) findViewById(R.id.activity_my_locations_rv_recycler);
        recycler.setLayoutManager(new LinearLayoutManager(this));

        //set my locations data
        MyCardViewAdapter adapter = new MyCardViewAdapter(this, data);
        recycler.setAdapter(adapter);
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
            Intent searchedLocationsActivity = new Intent(this, SearchedLocationsActivity.class);
            startActivity(searchedLocationsActivity);
            return true;
        } else if (itemId == R.id.menu_main_item_my_locations) {
            // do nothing - we are already in this activity
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
}
