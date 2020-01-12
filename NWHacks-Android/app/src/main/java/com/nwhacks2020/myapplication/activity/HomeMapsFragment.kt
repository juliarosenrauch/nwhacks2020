package com.nwhacks2020.myapplication.activity

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolygonOptions
import android.graphics.Color
import com.nwhacks2020.myapplication.R
import com.nwhacks2020.myapplication.models.Offer
import com.nwhacks2020.myapplication.services.AppService


class HomeMapsFragment(val parentActivity: Activity) : Fragment(), OnMapReadyCallback,
    GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMyLocationClickListener,
    ActivityCompat.OnRequestPermissionsResultCallback {

    private lateinit var mMap: GoogleMap
    private var mapFragment: SupportMapFragment? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        val view = inflater.inflate(R.layout.fragment_home_maps, container, false)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        if (mapFragment == null) {
            mapFragment = SupportMapFragment()
            (mapFragment as SupportMapFragment).getMapAsync(this)
        }

        childFragmentManager.beginTransaction().replace(R.id.map, mapFragment as SupportMapFragment).commit()

        // Refresh btn
        view.findViewById<Button>(R.id.map_refresh_btn).setOnClickListener {
            refresh()
        }

        return view
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        // Get current location
        if (ContextCompat.checkSelfPermission(parentActivity, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED
        ) {
            initializeMap()
        } else {
            ActivityCompat.requestPermissions(
                parentActivity,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                0
            )
        }
    }

    private fun initializeMap() {
        mMap.setMyLocationEnabled(true)
        mMap.setOnMyLocationButtonClickListener(this)
        mMap.setOnMyLocationClickListener(this)
        moveToCurrentLocation()
        refresh()
    }

    private fun refresh() {
        Log.i("Maps", "Refreshing")
        mMap.clear()
        placeMarkers()
        placePolygon()
    }

    private fun placeMarkers() {
        AppService.getService().getOffers { offers ->
            offers.map { offer ->
                Log.i("Offer retrieved", offer.text)
                val loc = LatLng(offer.latitude, offer.longitude)
                val markerOptions = MarkerOptions().position(loc)
                // Set the icon (change second arg. of decodeResource)
                // TODO: different icons for different types
                when (offer.type) {
                    Offer.foodType -> {
                        markerOptions.icon(
                            BitmapDescriptorFactory.fromBitmap(
                                BitmapFactory.decodeResource(resources, R.mipmap.food_marker)
                            )
                        )
                    }
                    Offer.shelterType -> {
                        markerOptions.icon(
                            BitmapDescriptorFactory.fromBitmap(
                                BitmapFactory.decodeResource(resources, R.mipmap.shelter_marker)
                            )
                        )
                    }
                    Offer.waterType -> {
                        markerOptions.icon(
                            BitmapDescriptorFactory.fromBitmap(
                                BitmapFactory.decodeResource(resources, R.mipmap.water_marker)
                            )
                        )
                    }
                    Offer.sleepType -> {
                        markerOptions.icon(
                                BitmapDescriptorFactory.fromBitmap(
                                        BitmapFactory.decodeResource(resources, R.mipmap.sleep_marker)
                                )
                        )
                    }
                }
                // Set title, shows only when marker pressed
                markerOptions.title(offer.text)
                // Update map
                mMap.addMarker(markerOptions)
            }
        }
    }

    private fun placePolygon() {
        // TODO: Create hard-coded polygon near UBC, shaded red for demo purposes
        // Create PolygonOptions with the LatLngs
        val polygonOptions = PolygonOptions().clickable(true)

        polygonOptions.add(LatLng(49.254474, -123.249725))
        polygonOptions.add(LatLng(49.252095, -123.246397))
        polygonOptions.add(LatLng(49.254115, -123.241145))
        polygonOptions.add(LatLng(49.256619, -123.243078))
        polygonOptions.add(LatLng(49.255918, -123.244411))
        polygonOptions.add(LatLng(49.255860, -123.245007))
        polygonOptions.add(LatLng(49.255160, -123.247225))
        polygonOptions.add(LatLng(49.255335, -123.247999))

        // Color for stroke and filll
        polygonOptions.strokeColor(Color.RED)
        polygonOptions.fillColor(Color.argb(123, 255, 0, 0))

        // Add polygons
        mMap.addPolygon(polygonOptions)
    }

    private fun moveToCurrentLocation() {
        AppService.getService().getLocation(parentActivity) { location ->
            val currLocation = LatLng(location.latitude, location.longitude)
            mMap.moveCamera(CameraUpdateFactory.newLatLng(currLocation))
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currLocation, 15f))
        }
    }

    override fun onMyLocationButtonClick(): Boolean {
        moveToCurrentLocation()
        return true
    }

    override fun onMyLocationClick(loc: Location) {
    }
}
