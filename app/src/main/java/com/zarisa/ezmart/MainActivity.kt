package com.zarisa.ezmart

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.zarisa.ezmart.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupSplashScreen()
        setSupportActionBar(binding.viewMainContent.toolbar)
        setupNavigationComponents()
    }

    private fun setupSplashScreen() {
        window.statusBarColor = ContextCompat.getColor(this, R.color.golden)
        binding.iconSplashScreen.let {
            it.alpha = 0f
            it.animate().setDuration(1500).alpha(1f).withEndAction {
                window.statusBarColor = ContextCompat.getColor(this, R.color.navy_blue)
                binding.root.setBackgroundColor(android.R.attr.windowBackground)
                binding.viewMainContent.root.visibility = View.VISIBLE
                binding.iconSplashScreen.visibility = View.GONE
                overridePendingTransition(
                    android.R.anim.fade_in,
                    android.R.anim.fade_out
                )
            }
        }
    }

    private fun setupNavigationComponents() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_main) as NavHostFragment
        val navController = navHostFragment.navController
        findViewById<BottomNavigationView>(R.id.bottom_nav)
            .setupWithNavController(navController)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.homeFragment, R.id.categoriesFragment
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
    }
}