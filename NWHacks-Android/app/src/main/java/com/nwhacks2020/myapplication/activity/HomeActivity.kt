package com.nwhacks2020.myapplication.activity

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.nwhacks2020.myapplication.R
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.BitmapDescriptor
import android.graphics.BitmapFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.PolygonOptions
import android.graphics.Color



class HomeActivity : AppCompatActivity(), OnMapReadyCallback,
    GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMyLocationClickListener,
    ActivityCompat.OnRequestPermissionsResultCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var locationProvider: FusedLocationProviderClient
    private lateinit var lastLocation: Location

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        locationProvider = LocationServices.getFusedLocationProviderClient(this)
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
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED
        ) {
            mMap.setMyLocationEnabled(true)
            mMap.setOnMyLocationButtonClickListener(this)
            mMap.setOnMyLocationClickListener(this)
            // Zoom to current location
            val currLocationTask = locationProvider.lastLocation
            currLocationTask.addOnSuccessListener { location ->
                if (location != null) {
                    val currLocation = LatLng(location.latitude, location.longitude)
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(currLocation))
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currLocation, 15f))

                    // Tyler: Place marker
                    val markerOptions = MarkerOptions().position(currLocation)
                    // 2
                    mMap.addMarker(markerOptions)
                }
            }
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                0
            )
        }

        // Add a marker in Sydney and move the camera
//        val sydney = LatLng(-34.0, 151.0)
//        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }

    override fun onMyLocationButtonClick(): Boolean {
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        return false
    }

    override fun onMyLocationClick(loc: Location) {
        // Messy example of adding markers and polygons

        // Clear map
        mMap.clear()

        // Place marker
        val markerOptions = MarkerOptions().position(currLocation)
        markerOptions.icon(
            BitmapDescriptorFactory.fromBitmap(
                BitmapFactory.decodeResource(resources, R.mipmap.logo1)
            )
        )
        markerOptions.title("One bed")
        mMap.addMarker(markerOptions)

        // Change map type
        mMap.mapType = GoogleMap.MAP_TYPE_SATELLITE

        // Place polygon
        val polygon = PolygonOptions().clickable(true)
            .add(
                LatLng(loc.latitude, loc.longitude),
                LatLng(loc.latitude + Math.random() * 0.1, loc.longitude + Math.random() * 0.1),
                LatLng(loc.latitude + Math.random() * 0.2, loc.longitude)
            )
        polygon.strokeColor(Color.RED)
        polygon.fillColor(Color.BLUE)
        mMap.addPolygon(polygon)
    }

    fun addMarkers(places: List<Triple<Int, Int, String>>) {
        // Clear map
        mMap.clear()

        for (place in places) {
            // Create markerOptions with position
            val loc = LatLng(place.first.toDouble(), place.second.toDouble())
            val markerOptions = MarkerOptions().position(loc)

            // Set the icon (change second arg. of decodeResource)
            // TODO: Change R.mipmap.logo1 based on string
            markerOptions.icon(
                BitmapDescriptorFactory.fromBitmap(
                    BitmapFactory.decodeResource(resources, R.mipmap.logo1)
                )
            )

            // Set title, shows only when marker pressed
            markerOptions.title(place.third)

            // Update map
            mMap.addMarker(markerOptions)
        }
    }

    fun addPolygons(polygons: List<List<LatLng>>) {
        for (polygon in polygons) {
            // Create PolygonOptions with the LatLngs
            val polygonOptions = PolygonOptions().clickable(true)
            for (loc in polygon) {
                polygonOptions.add(loc)
            }

            // Color for stroke and filll
            polygonOptions.strokeColor(Color.RED)
            polygonOptions.fillColor(Color.BLUE)

            // Add polygons
            mMap.addPolygon(polygonOptions)
        }
    }
}
