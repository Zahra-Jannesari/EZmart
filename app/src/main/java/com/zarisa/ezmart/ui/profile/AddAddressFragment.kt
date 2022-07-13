package com.zarisa.ezmart.ui.profile

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority.PRIORITY_LOW_POWER
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar
import com.zarisa.ezmart.R
import com.zarisa.ezmart.databinding.FragmentAddAddressBinding
import com.zarisa.ezmart.model.ADDRESSES
import com.zarisa.ezmart.model.EZ_SHARED_PREF
import com.zarisa.ezmart.ui.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@RequiresApi(Build.VERSION_CODES.N)
@AndroidEntryPoint
class AddAddressFragment : Fragment() {
    lateinit var binding: FragmentAddAddressBinding
    val viewModel: ProfileViewModel by activityViewModels()
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var sharedPref: SharedPreferences
    private lateinit var map: GoogleMap
    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                getCurrentLocation()
            }
            permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                getCurrentLocation()
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

    private fun setupAppbar() {
        (requireActivity() as MainActivity).supportActionBar?.hide()
        setHasOptionsMenu(false)
        (requireActivity() as MainActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setupAppbar()
        binding = FragmentAddAddressBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initVariables()
        onClicks()
    }

    private fun initVariables() {
        sharedPref = requireActivity().getSharedPreferences(EZ_SHARED_PREF, Context.MODE_PRIVATE)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
    }

    private fun onClicks() {
        binding.btnAddAddress.setOnClickListener {
            val eTAddress = binding.editTextAddress
            if (eTAddress.text.isNullOrBlank())
                eTAddress.error = "برای افزایش دقت لطفا آدرس را وارد کنید."
            else saveNewAddressInSharedPref(eTAddress)
        }
        binding.btnGetCurrentLocation.setOnClickListener {
            if (checkGooglePlayServices()) getLocationPermission()
        }
    }

    private fun saveNewAddressInSharedPref(eTAddress: EditText) {
        val newAddresses = mutableSetOf("${eTAddress.text},${viewModel.newLatLong}")
        sharedPref.getStringSet(ADDRESSES, emptySet())?.let {
            newAddresses.addAll(it)
        }
        sharedPref.edit().putStringSet(ADDRESSES, newAddresses).apply()
        eTAddress.setText("")
        viewModel.newLatLong = ""
        Toast.makeText(requireContext(), "آدرس جدید ذخیره شد.", Toast.LENGTH_SHORT).show()
    }

    private fun checkGooglePlayServices(): Boolean {
        val availability = GoogleApiAvailability.getInstance()
        val resultCode = availability.isGooglePlayServicesAvailable(requireContext())
        if (resultCode != ConnectionResult.SUCCESS) {
            Toast.makeText(
                requireContext(),
                "متاسفانه امکان ارائه این قابلیت بر روی تلفن شما میسر نیست.",
                Toast.LENGTH_SHORT
            ).show()
            return false
        }
        return true
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun getLocationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            when {
                //if user already granted the permission
                ContextCompat.checkSelfPermission(
                    requireContext(), Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED -> getCurrentLocation()
                //if user already denied the permission once
                ActivityCompat.shouldShowRequestPermissionRationale(
                    requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION
                ) -> showRationSnackbar()
                else -> launchPermissions()
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

    @SuppressLint("MissingPermission")
    private fun getCurrentLocation() {
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
            Toast.makeText(
                requireContext(),
                "لطفا لوکیشن خود را روشن کنید.",
                Toast.LENGTH_SHORT
            ).show()
        }
        fusedLocationClient.getCurrentLocation(PRIORITY_LOW_POWER, null)
            .addOnSuccessListener { location: Location? ->
                location?.let {
                    viewModel.newLatLong = "$it.latitude,$it.longitude"
                    showCurrentLocationOnMap(LatLng(it.latitude, it.longitude))
                }
            }
    }

    private fun showCurrentLocationOnMap(latLng: LatLng) {
        binding.lMap.visibility = View.VISIBLE
        val mapFragment = childFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync { readyMap ->
            map = readyMap
            showLocationOnMap(latLng)
        }
        binding.tvUserLat.text = "عرض جغرافیایی: ${latLng.latitude}"
        binding.tvUserLong.text = "طول جغرافیایی: ${latLng.longitude}"
    }

    private fun showLocationOnMap(latLng: LatLng) {
        map.setMinZoomPreference(6.0f)
        map.setMaxZoomPreference(14.0f)
        map.addMarker(
            MarkerOptions()
                .position(latLng)
                .title("موقعیت")
//                .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker))
                .zIndex(2.0f)
        )
        map.moveCamera(CameraUpdateFactory.newLatLng(latLng))
    }
}