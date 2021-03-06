package com.zarisa.ezmart.ui

import android.content.Context
import android.content.SharedPreferences
import android.location.LocationManager
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.location.LocationManagerCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.zarisa.ezmart.R
import com.zarisa.ezmart.databinding.ActivityMainBinding
import com.zarisa.ezmart.model.APP_THEME
import com.zarisa.ezmart.model.EZ_SHARED_PREF
import dagger.hilt.android.AndroidEntryPoint

var appTheme = R.style.Theme_EZmart

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var sharedPref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        sharedPref = this.getSharedPreferences(EZ_SHARED_PREF, Context.MODE_PRIVATE)
        appTheme = sharedPref.getInt(APP_THEME, R.style.Theme_EZmart)
        setTheme(appTheme)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupActionBar()
        setupNavigationComponents()
    }

    private fun setupActionBar() {
        supportActionBar?.show()
        window.statusBarColor = ContextCompat.getColor(this, R.color.navy_blue)
//        window.decorView.layoutDirection = View.LAYOUT_DIRECTION_RTL
    }

    private fun setupNavigationComponents() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        findViewById<BottomNavigationView>(R.id.bottom_nav)
            .setupWithNavController(navController)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.homeFragment,
                R.id.categoriesFragment,
                R.id.shoppingFragment,
                R.id.profileFragment
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.categoriesFragment -> binding.bottomNav.visibility = View.VISIBLE
                R.id.homeFragment -> binding.bottomNav.visibility = View.VISIBLE
                R.id.shoppingFragment -> binding.bottomNav.visibility = View.VISIBLE
                R.id.profileFragment -> binding.bottomNav.visibility = View.VISIBLE
                else -> binding.bottomNav.visibility = View.GONE
            }
        }
    }

    fun isLocationEnabled(): Boolean {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return LocationManagerCompat.isLocationEnabled(locationManager)
    }
}