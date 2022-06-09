package com.zarisa.ezmart.ui

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.zarisa.ezmart.R
import com.zarisa.ezmart.databinding.ActivityMainBinding
import com.zarisa.ezmart.model.NetworkStatus
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
//    val viewModel: ParentViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupActionBar()
        setupNavigationComponents()
//        bindNetworkStatusView()
    }
/*
    private fun bindNetworkStatusView() {
        viewModel.networkStatusLiveData.observe(this) {
            when (it) {
                NetworkStatus.LOADING -> {
                    binding.tvNetworkStatus.visibility = View.GONE
                    binding.root.setBackgroundColor(android.R.attr.windowBackground)
                    binding.imageViewNetworkStatus.let { StatusImage ->
                        StatusImage.visibility = View.VISIBLE
                        StatusImage.setImageResource(R.drawable.loading_animation)
                    }
                    binding.navHostFragment.visibility = View.INVISIBLE
                    binding.bottomNav.visibility = View.VISIBLE
                }
                NetworkStatus.ERROR -> {
                    binding.root.setBackgroundColor(ContextCompat.getColor(this, R.color.golden))
                    binding.imageViewNetworkStatus.let { StatusImage ->
                        StatusImage.visibility = View.VISIBLE
                        StatusImage.setImageResource(R.drawable.error_image)
                    }
                    binding.tvNetworkStatus.visibility = View.VISIBLE
                    binding.bottomNav.visibility = View.INVISIBLE
                    binding.navHostFragment.visibility = View.INVISIBLE
                }
                else -> {
                    binding.root.setBackgroundColor(android.R.attr.windowBackground)
                    binding.imageViewNetworkStatus.visibility = View.GONE
                    binding.bottomNav.visibility = View.VISIBLE
                    binding.navHostFragment.visibility = View.VISIBLE
                }
            }
        }
    }

 */


    private fun setupActionBar() {
        window.statusBarColor = ContextCompat.getColor(this, R.color.navy_blue)
        setSupportActionBar(binding.toolbar)
    }

    private fun setupNavigationComponents() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        findViewById<BottomNavigationView>(R.id.bottom_nav)
            .setupWithNavController(navController)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.homeFragment, R.id.categoriesFragment
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.categoriesFragment -> showHighLevelViews()
                R.id.homeFragment -> showHighLevelViews()
                else -> hideHighLevelViews()
            }
        }
    }

    private fun showHighLevelViews() {
        binding.bottomNav.visibility = View.VISIBLE
        binding.toolbar.visibility = View.VISIBLE
    }

    private fun hideHighLevelViews() {
        binding.bottomNav.visibility = View.GONE
        binding.toolbar.visibility = View.GONE
    }
}