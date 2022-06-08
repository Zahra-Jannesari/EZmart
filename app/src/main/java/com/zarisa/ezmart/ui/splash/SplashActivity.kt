package com.zarisa.ezmart.ui.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.zarisa.ezmart.R
import com.zarisa.ezmart.databinding.ActivitySplashScreenBinding
import com.zarisa.ezmart.ui.MainActivity

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
            it.animate().setDuration(1500).alpha(1f).withEndAction {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                finish()
            }
        }
    }
}
