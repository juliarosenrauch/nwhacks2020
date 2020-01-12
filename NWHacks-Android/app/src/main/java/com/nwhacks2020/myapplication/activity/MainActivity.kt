package com.nwhacks2020.myapplication.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.nwhacks2020.myapplication.R
import com.nwhacks2020.myapplication.services.AppService


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Get current location
        // Get current location
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED
        ) {
            // Has permission
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                0
            )
        }
        // See if there's a sign in - this currently doesn't work
        val user = AppService.getService().getSignedInUser(this)
        // Conditionally route depending on logged in user status
        val intent: Intent = if (user == null) {
            Log.i("MAINACTIVITY", "User not found")
            Intent(applicationContext, AppLoginActivity::class.java)
        } else {
            Log.i("MAINACTIVITY", "User found")
            Intent(applicationContext, HomeMapsFragment::class.java)
        }
        startActivity(intent)
    }
}
