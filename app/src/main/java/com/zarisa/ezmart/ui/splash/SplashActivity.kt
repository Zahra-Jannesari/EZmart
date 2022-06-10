package com.zarisa.ezmart.ui.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.zarisa.ezmart.R
import com.zarisa.ezmart.databinding.ActivitySplashScreenBinding
import com.zarisa.ezmart.ui.MainActivity
import java.net.UnknownHostException

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    lateinit var binding: ActivitySplashScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        setupSplashScreen()
    }

    private fun setupSplashScreen() {
        window.statusBarColor = ContextCompat.getColor(this, R.color.golden)
        binding.iconSplashScreen.let {
            it.alpha = 0f
            it.animate().setDuration(1500).alpha(1f).let { animation ->
                if (isInternetAvailable())
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
        if (isInternetAvailable())
            intentToMainActivity()
        else
            view.visibility = View.VISIBLE
    }

    private fun isInternetAvailable(): Boolean {
        try {
            val command = "ping -c 1 google.com"
            return Runtime.getRuntime().exec(command).waitFor() == 0
        } catch (e: UnknownHostException) {
            Log.d("TAG", "isInternetAvailable: false")
        } catch (e: Exception) {
            Log.d("TAG", "unknown exception")
        }
        return false
    }
}