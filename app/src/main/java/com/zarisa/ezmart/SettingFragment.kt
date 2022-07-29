package com.zarisa.ezmart

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.zarisa.ezmart.databinding.FragmentSettingBinding
import com.zarisa.ezmart.model.*
import com.zarisa.ezmart.ui.MainActivity

class SettingFragment : Fragment() {
    private lateinit var binding: FragmentSettingBinding
    private lateinit var sharedPref: SharedPreferences
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setupAppbar()
        binding = FragmentSettingBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPref = requireActivity().getSharedPreferences(EZ_SHARED_PREF, Context.MODE_PRIVATE)
        initTheme()
    }

    //    activity?.setTheme(R.style.Theme_EZmart)
    private fun initTheme() {
        sharedPref.getInt(APP_THEME, AUTO_THEME).let {
            when (it) {
                DARK_THEME -> binding.btgTheme.check(R.id.btn_dark_theme)
                LIGHT_THEME -> binding.btgTheme.check(R.id.btn_light_theme)
                else -> binding.btgTheme.check(R.id.btn_auto_theme)
            }
        }
        binding.btgTheme.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (isChecked) {
                val theme = when (checkedId) {
                    R.id.btn_light_theme -> {
                        sharedPref.edit().putInt(APP_THEME, LIGHT_THEME).apply()
                        AppCompatDelegate.MODE_NIGHT_NO
                    }
                    R.id.btn_dark_theme -> {
                        sharedPref.edit().putInt(APP_THEME, DARK_THEME).apply()
                        AppCompatDelegate.MODE_NIGHT_YES
                    }
                    else -> {
                        sharedPref.edit().putInt(APP_THEME, AUTO_THEME).apply()
                        AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
                    }
                }
                AppCompatDelegate.setDefaultNightMode(theme)
            }
        }
    }

    private fun setupAppbar() {
        (requireActivity() as MainActivity).supportActionBar?.hide()
        setHasOptionsMenu(false)
        (requireActivity() as MainActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
    }
}