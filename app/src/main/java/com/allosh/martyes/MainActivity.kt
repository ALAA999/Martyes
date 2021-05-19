package com.allosh.martyes

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.allosh.martyes.databinding.ActivityMainBinding
import com.allosh.martyes.util.UIUtil

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var icons: Array<ImageView>
    private lateinit var dots: Array<ImageView>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        icons = arrayOf(binding.home, binding.search, binding.statistics, binding.profile)
        dots = arrayOf(binding.dotOne, binding.dotTwo, binding.dotThree, binding.dotFour)

        navController = findNavController(R.id.nav_host_fragment_activity_main)
        UIUtil.setOnClickListeners(arrayOf(binding.homeLayout, binding.searchLayout, binding.statisticsLayout, binding.profileLayout), this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.home_layout -> {
                navController.navigate(R.id.navigation_home)
                setNavBarTints(0)
            }
            R.id.search_layout -> {
                navController.navigate(R.id.navigation_search)
                setNavBarTints(1)
            }
            R.id.statistics_layout -> {
                navController.navigate(R.id.navigation_statistics)
                setNavBarTints(2)
            }
            R.id.profile_layout -> {
                navController.navigate(R.id.navigation_profile)
                setNavBarTints(3)
            }
        }
    }

    private fun setNavBarTints(index: Int) {
        for (icon in icons) {
            icon.setColorFilter(ContextCompat.getColor(this, R.color.gray))
        }
        for (dot in dots) {
            dot.visibility = View.INVISIBLE
        }
        icons[index].setColorFilter(ContextCompat.getColor(this, R.color.red))
        dots[index].visibility = View.VISIBLE
    }
}