package com.example.owner.skymood

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.owner.skymood.adapters.MyCardViewAdapter
import com.example.owner.skymood.model.MyLocationManager

class MyLocationsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_locations)

        val manager = MyLocationManager.getInstance(this)

        //setting the toolbar
        val toolbar = findViewById<View?>(R.id.activity_my_locations_view_tool_bar) as Toolbar?
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        //get my locations
        val data = manager.allMyLocations

        //create recycler view
        val recycler = findViewById<View?>(R.id.activity_my_locations_rv_recycler) as RecyclerView
        recycler.setLayoutManager(LinearLayoutManager(this))

        //set my locations data
        val adapter = MyCardViewAdapter(this, data)
        recycler.setAdapter(adapter)
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
            val searchedLocationsActivity = Intent(this, SearchedLocationsActivity::class.java)
            startActivity(searchedLocationsActivity)
            true
        }

        R.id.menu_main_item_my_locations -> {
            // do nothing - we are already in this activity
            true
        }

        else -> super.onOptionsItemSelected(item)
    }
}
