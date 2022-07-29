package com.zarisa.ezmart.ui.splash

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import com.zarisa.ezmart.R
import com.zarisa.ezmart.databinding.ActivitySplashScreenBinding
import com.zarisa.ezmart.model.*
import com.zarisa.ezmart.ui.MainActivity

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    lateinit var binding: ActivitySplashScreenBinding
    private val sharedPref by lazy {
        this.getSharedPreferences(
            EZ_SHARED_PREF,
            Context.MODE_PRIVATE
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        setTheme()
        setupSplashScreen()
    }

    private fun setTheme() {
        sharedPref.getInt(APP_THEME, AUTO_THEME).let {
            when (it) {
                DARK_THEME -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                LIGHT_THEME -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                else -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            }
        }
    }

    private fun setupSplashScreen() {
        window.statusBarColor = ContextCompat.getColor(this, R.color.golden)
        binding.iconSplashScreen.let {
            it.alpha = 0f
            it.animate().setDuration(1500).alpha(1f).let { animation ->
                if (isInternetAvailable(this))
                    animation.withEndAction {
                        intentToMainActivity()
                    }
                else {
                    binding.refreshConnection.let { layout ->
                        layout.visibility = View.VISIBLE
                        layout.setOnClickListener { view ->
                            onRefreshClick(view)
                        }
                    }
                }
            }
        }
    }

    private fun intentToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        finish()
    }

    private fun onRefreshClick(view: View) {
        view.visibility = View.INVISIBLE
        if (isInternetAvailable(this))
            intentToMainActivity()
        else
            view.visibility = View.VISIBLE
    }

    @SuppressLint("MissingPermission")
    fun isInternetAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork ?: return false
            val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false
            return when {
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                else -> false
            }
        } else {
            @Suppress("DEPRECATION") val networkInfo =
                connectivityManager.activeNetworkInfo ?: return false
            @Suppress("DEPRECATION")
            return networkInfo.isConnected
        }
    }
//    private fun isInternetAvailable(): Boolean {
//        try {
//            val command = "ping -c 1 google.com"
//            return Runtime.getRuntime().exec(command).waitFor() == 0
//        } catch (e: UnknownHostException) {
//            Log.d("TAG", "isInternetAvailable: false")
//        } catch (e: Exception) {
//            Log.d("TAG", "unknown exception")
//        }
//        return false
//    }
}