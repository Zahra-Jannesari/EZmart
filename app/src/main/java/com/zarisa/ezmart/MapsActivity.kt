package com.zarisa.ezmart

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.zarisa.ezmart.databinding.ActivityMapsBinding

//, OnMapReadyCallback
class MapsActivity : AppCompatActivity() {
    private lateinit var map: GoogleMap

    //    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private var latLong: Bundle? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        latLong = intent.getBundleExtra("latLong")

//        val mapFragment = supportFragmentManager
//            .findFragmentById(R.id.map) as SupportMapFragment
//        mapFragment.getMapAsync(this)
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync { readyMap ->
            map = readyMap
            showLocationOnMap(latLong?.get("latLong") as LatLng)
        }
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
//    override fun onMapReady(googleMap: GoogleMap) {
//        mMap = googleMap
//
//        // Add a marker in Sydney and move the camera
//        if (latLon != null){
//            val userLocation = LatLng(latLon?.get("lat") as Double, latLon?.get("long") as Double)
//            mMap.addMarker(MarkerOptions().position(userLocation).title("موقعیت"))
//            mMap.moveCamera(CameraUpdateFactory.newLatLng(userLocation))
//        }
//    }
}