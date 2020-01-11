package com.nwhacks2020.myapplication.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.nwhacks2020.myapplication.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Conditionally route depending on logged in user status
        if (true) {
            // Intent to HomeActivity
        } else {
            // Intent to AppLoginActivity
        }
    }
}
