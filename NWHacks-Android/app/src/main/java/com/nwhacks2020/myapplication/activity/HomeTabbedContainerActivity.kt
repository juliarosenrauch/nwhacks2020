package com.nwhacks2020.myapplication.activity

import android.Manifest
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.nwhacks2020.myapplication.R


class HomeTabbedContainerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_tabbed_container)

        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        // Set to home view
        openFragment(HomeMapsFragment(this))

        // TODO: This is temporary since I made this the starting page, needs to be removed
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            0
        )

        bottomNavigation.selectedItemId = R.id.navigation_home
        bottomNavigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.navigation_home -> {
                    openFragment(HomeMapsFragment(this))
                    false
                }
                R.id.navigation_group -> {
                    openFragment(InfoFragment())
                    false
                }
                R.id.navigation_info -> {
                    openFragment(InfoFragment())
                    false
                }
                R.id.navigation_alerts -> {
                    openFragment(InfoFragment())
                    false
                }
                R.id.navigation_assist -> {
                    openFragment(OfferFormActivityFragment())
                    false
                }
                else -> { false }
            }
        }
    }

    private fun openFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.home_view_container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}
