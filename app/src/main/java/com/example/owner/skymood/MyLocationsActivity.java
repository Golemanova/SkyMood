package com.example.owner.skymood;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.owner.skymood.adapters.MyCardViewAdapter;
import com.example.owner.skymood.model.MyLocation;
import com.example.owner.skymood.model.MyLocationManager;

import java.util.ArrayList;

public class MyLocationsActivity extends AppCompatActivity {

    CardView cardView;
    RecyclerView recycler;
    ArrayList<MyLocation> data;
    MyLocationManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_locations);
        manager = MyLocationManager.getInstance(this);

        //setting view_toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_my_locations_view_tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        data = manager.getAllMyLocations();
        this.recycler = (RecyclerView) findViewById(R.id.activity_my_locations_rv_recycler);
        this.recycler.setLayoutManager(new LinearLayoutManager(this));
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
        Intent intent = null;
        switch (item.getItemId()) {
            case R.id.menu_main_item_sky_mood:
                intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                return true;
            case R.id.menu_main_item_searched_locations:
                intent = new Intent(this, SearchedLocationsActivity.class);
                startActivity(intent);
                return true;
            case R.id.menu_main_item_my_locations:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
