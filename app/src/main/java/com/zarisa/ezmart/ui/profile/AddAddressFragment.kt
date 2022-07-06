package com.zarisa.ezmart.ui.profile

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority.PRIORITY_LOW_POWER
import com.google.android.material.snackbar.Snackbar
import com.zarisa.ezmart.R
import com.zarisa.ezmart.databinding.FragmentAddAddressBinding
import com.zarisa.ezmart.ui.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddAddressFragment : Fragment() {
    lateinit var binding: FragmentAddAddressBinding
    val viewModel: ProfileViewModel by activityViewModels()
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    @RequiresApi(Build.VERSION_CODES.N)
    val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                showLocation()
            }
            permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                showLocation()
            }
            else -> {
                Toast.makeText(
                    requireContext(),
                    "اجازه دسترسی به لوکیشن داده نشد. امکان دریافت موقعیت وجود ندارد.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddAddressBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnGetCurrentLocation.setOnClickListener {
            getLocationPermission()
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        }

    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun getLocationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            when {
                //if user already granted the permission
                ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED -> {
                    showLocation()
                }
                //if user already denied the permission once
                ActivityCompat.shouldShowRequestPermissionRationale(
                    requireActivity(),
                    Manifest.permission.CAMERA
                ) -> {
                    showRationSnackbar()
                }
                else -> {
                    launchPermissions()
                }
            }
        }
    }

    private fun showRationSnackbar() {
        val snackbar = Snackbar.make(
            binding.root,
            R.string.permission_required,
            Snackbar.LENGTH_LONG
        ).setAction(R.string.got_it) {
            launchPermissions()
        }
        snackbar.view.layoutDirection = View.LAYOUT_DIRECTION_RTL
        snackbar.show()
    }

    private fun launchPermissions() {
        locationPermissionRequest.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }

    private fun showLocation() {
        Toast.makeText(requireContext(), "permission granted", Toast.LENGTH_SHORT).show()
        if (ActivityCompat.checkSelfPermission(
                requireContext(), Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(
                requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        if (!(activity as MainActivity).isLocationEnabled()) {
            Toast.makeText(requireContext(), "لطفا لوکیشن خود را روشن کنید.", Toast.LENGTH_SHORT)
                .show()
        }
        fusedLocationClient.getCurrentLocation(PRIORITY_LOW_POWER, null)
            .addOnSuccessListener { location: Location? ->

                location?.let {
                    viewModel.addLatLong(it.latitude, it.longitude)
                }
            }
    }
}