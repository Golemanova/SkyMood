package com.example.owner.skymood.adapters

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.example.owner.skymood.MainActivity
import com.example.owner.skymood.fragments.CurrentWeatherFragment
import com.example.owner.skymood.fragments.HourlyWeatherFragment
import com.example.owner.skymood.fragments.MoreInfoFragment

/**
 * Created by Golemanovaa on 4.4.2016 г..
 */
class CustomPagerAdapter(
    fm: FragmentManager, private val context: Context?
) : FragmentStatePagerAdapter(fm) {
    private val fragmentsArray: Array<Fragment> = arrayOf(
        CurrentWeatherFragment(),
        HourlyWeatherFragment(),
        MoreInfoFragment()
    )

    override fun getItem(position: Int): Fragment = fragmentsArray[position]

    override fun getCount(): Int = MainActivity.NUMBER_OF_PAGES

    override fun getPageTitle(position: Int) = when (position) {
        0 -> "\uD83C\uDF08 CURRENT"
        1 -> "⛅ Hourly&Weekly"
        2 -> "⚡ MORE INFO"
        else -> ""
    }
}
