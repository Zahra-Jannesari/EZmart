package com.zarisa.ezmart

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
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
        setUpSplashScreen()
        setUpBottomNavigation()
    }

    private fun setUpSplashScreen() {
//        val decorView: View = window.decorView
//        val uiOptions = arrayListOf(View.SYSTEM_UI_FLAG_LAYOUT_STABLE,View.SYSTEM_UI_FLAG_FULLSCREEN)
//
//        decorView.systemUiVisibility = uiOptions[1]


        window.statusBarColor =  resources.getColor(R.color.golden)
        binding.iconSplashScreen.let {
            it.alpha = 0f
            it.animate().setDuration(1500).alpha(1f).withEndAction {


//                decorView.systemUiVisibility = uiOptions[0]

                window.statusBarColor = resources.getColor(R.color.navy_blue)
                actionBar?.show()
                binding.root.setBackgroundColor(android.R.attr.windowBackground)
                binding.viewMainContent.root.visibility = View.VISIBLE
                binding.iconSplashScreen.visibility = View.GONE
                supportActionBar?.show()
                overridePendingTransition(
                    android.R.anim.fade_in,
                    android.R.anim.fade_out
                )
            }
        }
    }

    private fun setUpBottomNavigation() {
        setSupportActionBar(binding.viewMainContent.toolbar)
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